package com.qzt.module.certification.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.GlobalErrorCodeConstants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.core.text.Convert;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.exception.file.FileNameLengthLimitExceededException;
import com.qzt.common.utils.*;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.file.MimeTypeUtils;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.flowpack.ProcessManager;
import com.qzt.flowpack.common.vo.Result;
import com.qzt.flowpack.model.process.StartProcessParam;
import com.qzt.flowpack.model.task.AttachInfo;
import com.qzt.flowpack.model.variable.Variable;
import com.qzt.flowpack.model.variable.VariableType;
import com.qzt.module.app.domain.AppAsset;
import com.qzt.module.app.mapper.AppAssetMapper;
import com.qzt.module.certification.constants.BpmConstant;
import com.qzt.module.certification.domain.BpmAttest;
import com.qzt.module.certification.domain.vo.BpmAttestAddVO;
import com.qzt.module.certification.mapper.BpmAttestMapper;
import com.qzt.module.certification.service.IBpmAttestService;
import com.qzt.module.system.domain.SysAttestType;
import com.qzt.module.system.domain.SysBpmTemplate;
import com.qzt.module.system.mapper.SysAttestTypeMapper;
import com.qzt.module.system.mapper.SysBpmTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 认证流程Service业务层处理
 *
 * @author kf
 * @date 2023-04-12
 */
@Service
public class BpmAttestServiceImpl implements IBpmAttestService {
    @Autowired
    private BpmAttestMapper bpmAttestMapper;
    @Autowired
    private SysBpmTemplateMapper sysBpmTemplateMapper;
    @Autowired
    private SysAttestTypeMapper sysAttestTypeMapper;
    @Autowired
    private AppAssetMapper appAssetMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    ProcessManager processManager;


    /**
     * 查询认证流程
     *
     * @param id 认证流程主键
     * @return 认证流程
     */
    @Override
    public BpmAttest selectById(Long id) {
        return bpmAttestMapper.selectById(id);
    }

    /**
     * 查询认证流程列表
     *
     * @param bpmAttest 认证流程
     * @return 认证流程
     */
    @Override
    public List<BpmAttest> selectList(BpmAttest bpmAttest) {
        return bpmAttestMapper.selectList(bpmAttest);
    }

    /**
     * 新增认证流程
     *
     * @param vo 认证申请对象
     * @return 结果
     */
    @Override
    @Transactional
    public int save(BpmAttestAddVO vo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long customerId = loginUser.getCustomerId();
        if(Objects.isNull(customerId)){
            throw new ServiceException(GlobalErrorCodeConstants.FORBIDDEN);
        }
        BpmAttest bpmAttest = BpmAttest.builder()
                .attestType(Convert.toLong(ToolUtil.decryptSm4(vo.getAttestType())))
                .customerId(customerId)
                .build();
        //判断是否有进行中的认证
        List<BpmAttest> oldList = bpmAttestMapper.selectList(BpmAttest.builder().customerId(bpmAttest.getCustomerId()).status(BpmConstant.STATUS_ON).build());
        if (StringUtils.isNotEmpty(oldList)) {
            throw new ServiceException(SysErrorCodeConstants.BMP_PROCESS_IS_ON);
        }
        //获取认证服务类别
        SysAttestType sysAttestType = selectAttestType(bpmAttest.getAttestType());
        if(Objects.isNull(sysAttestType)){
            throw new ServiceException(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        //设置流程参数
        List<Variable> variables = new ArrayList<>();
        Variable variable = new Variable("认证服务类别", VariableType.String);
        variable.setValue(sysAttestType.getName());
        variables.add(variable);
        Variable variable1 = new Variable("资产", VariableType.String);
        List<Long> assetIds = new ArrayList<>();
        for(String aid : vo.getAssets()){
            assetIds.add(Convert.toLong(ToolUtil.decryptSm4(aid)));
        }
        List<AppAsset> assets = appAssetMapper.selectAppAssetByIds(assetIds);
        variable1.setValue(JsonUtils.toJsonString(assets));
        variables.add(variable1);
        //设置流程附件
        List<AttachInfo> attachments = new ArrayList<>();
        for(MultipartFile file : vo.getFiles()){
            AttachInfo info = null;
            try {
                info = uploadBpmFile(file);
            } catch (Exception e) {
                throw new ServiceException(SysErrorCodeConstants.FILE_UPLOAD_FAIL);
            }
            attachments.add(info);
        }
        //启动流程
        StartProcessParam param = new StartProcessParam();
        param.setName(bpmAttest.getProcessName());
        param.setStarter(bpmAttest.getCreator());
        param.setKey(selectTemplateKey());
        param.setVariables(variables);
        param.setAttachments(attachments);
        Result<String> res = processManager.startProcess(param);
        if (0 != res.getStatus()) {
            throw MessageUtils.exception(SysErrorCodeConstants.BMP_PROCESS_START_FAIL,bpmAttest.getCreator(), res.getMsg());
        }
        bpmAttest.setCreateTime(DateUtils.getNowDate());
        bpmAttest.setCreator(loginUser.getUsername());
        return bpmAttestMapper.save(bpmAttest);
    }

    private AttachInfo uploadBpmFile(MultipartFile file) throws Exception{
        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        String fileName = FileUploadUtils.extractFilename(file);
        String absPath = FileUploadUtils.getAbsoluteFile(AttestConfig.getBpmPath(), fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath));

        AttachInfo info = new AttachInfo();
        info.setName(file.getOriginalFilename());
        info.setType(FileUploadUtils.getExtension(file));
        info.setUrl(absPath);
        return info;
    }
    /**
     * 获取模板KEY
     */
    public String selectTemplateKey() {
        JSONArray arr = redisCache.getCacheObject(CacheConstants.BPM_KEY);
        if (StringUtils.isNotNull(arr)) {
            List<SysBpmTemplate> list = arr.toList(SysBpmTemplate.class);
            return list.get(0).getTemplateKey();
        }
        List<SysBpmTemplate> list = sysBpmTemplateMapper.selectList(new SysBpmTemplate());
        if (StringUtils.isEmpty(list)) {
            throw new ServiceException(SysErrorCodeConstants.BMP_TEMPLATE_NOT_EXISTS);
        }
        redisCache.setCacheObject(CacheConstants.BPM_KEY, list);
        return list.get(0).getTemplateKey();
    }

    public SysAttestType selectAttestType(Long id) {
        JSONArray arr = redisCache.getCacheObject(CacheConstants.ATTEST_TYPE);
        if (StringUtils.isNotNull(arr)) {
            List<SysAttestType> list = arr.toList(SysAttestType.class);
            return list.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
        }
        List<SysAttestType> list = sysAttestTypeMapper.selectList(new SysAttestType());
        if(StringUtils.isEmpty(list)){
            throw new ServiceException(SysErrorCodeConstants.ATTEST_TYPE_IS_EMPTY);
        }
        return list.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * 修改认证流程
     *
     * @param bpmAttest 认证流程
     * @return 结果
     */
    @Override
    public int update(BpmAttest bpmAttest) {
        bpmAttest.setUpdateTime(DateUtils.getNowDate());
        return bpmAttestMapper.update(bpmAttest);
    }

    /**
     * 批量删除认证流程
     *
     * @param ids 需要删除的认证流程主键
     * @return 结果
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        return bpmAttestMapper.deleteByIds(ids);
    }

    /**
     * 删除认证流程信息
     *
     * @param id 认证流程主键
     * @return 结果
     */
    @Override
    public int deleteById(Long id) {
        return bpmAttestMapper.deleteById(id);
    }
}
