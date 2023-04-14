package com.qzt.module.system.service.impl;

import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.utils.DateUtils;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.flowpack.TemplateManager;
import com.qzt.flowpack.common.vo.Result;
import com.qzt.flowpack.model.template.Template;
import com.qzt.module.system.domain.SysBpmTemplate;
import com.qzt.module.system.mapper.SysBpmTemplateMapper;
import com.qzt.module.system.service.ISysBpmTemplateService;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 流程模板Service业务层处理
 *
 * @author kf
 * @date 2023-04-11
 */
@Service
public class SysBpmTemplateServiceImpl implements ISysBpmTemplateService {
    @Autowired
    private SysBpmTemplateMapper sysBpmTemplateMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    TemplateManager templateManager;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingBpmTemplateCache();
    }

    /**
     * 加载模板key到缓存
     */
    public void loadingBpmTemplateCache() {
        List<SysBpmTemplate> list = sysBpmTemplateMapper.selectList(new SysBpmTemplate());
        redisCache.setCacheObject(CacheConstants.BPM_KEY, list);
    }

    /**
     * 查询流程模板
     *
     * @param id 流程模板主键
     * @return 流程模板
     */
    @Override
    public SysBpmTemplate selectById(Long id) {
        return sysBpmTemplateMapper.selectById(id);
    }

    /**
     * 查询流程模板列表
     *
     * @param sysBpmTemplate 流程模板
     * @return 流程模板
     */
    @Override
    public List<SysBpmTemplate> selectList(SysBpmTemplate sysBpmTemplate) {
        return sysBpmTemplateMapper.selectList(sysBpmTemplate);
    }

    /**
     * 新增流程模板
     *
     * @param sysBpmTemplate 流程模板
     * @return 结果
     */
    @Override
    @Transactional
    public int save(SysBpmTemplate sysBpmTemplate) throws IOException {
        String resourceName = AttestConfig.getProfile() + StringUtils.substringAfter(sysBpmTemplate.getPath(), Constants.RESOURCE_PREFIX);
        byte[] tempBytes = FileUtil.readAsByteArray(new File(resourceName));
        Result<Template> template = templateManager.createTemplate(sysBpmTemplate.getName(), resourceName, tempBytes);
        if (0 != template.getStatus()) {
            throw MessageUtils.exception(SysErrorCodeConstants.BMP_CREATE_TEMPLATE_FAIL, template.getMsg());
        }
        sysBpmTemplate.setTemplateId(template.getData().getId());
        sysBpmTemplate.setTemplateKey(template.getData().getKey());
        int rows = sysBpmTemplateMapper.save(sysBpmTemplate);
        if (rows > 0) {
            loadingBpmTemplateCache();
        }
        return rows;
    }

    /**
     * 修改流程模板
     *
     * @param sysBpmTemplate 流程模板
     * @return 结果
     */
    @Override
    @Transactional
    public int update(SysBpmTemplate sysBpmTemplate) throws IOException {
        String resourceName = AttestConfig.getProfile() + StringUtils.substringAfter(sysBpmTemplate.getPath(), Constants.RESOURCE_PREFIX);
        byte[] tempBytes = FileUtil.readAsByteArray(new File(resourceName));
        Result<Template> template = templateManager.updateTemplate(sysBpmTemplate.getTemplateKey(), sysBpmTemplate.getName(), resourceName, tempBytes);
        if (0 != template.getStatus()) {
            throw MessageUtils.exception(SysErrorCodeConstants.BMP_UPDATE_TEMPLATE_FAIL, template.getMsg());
        }
        sysBpmTemplate.setUpdateTime(DateUtils.getNowDate());
        int rows = sysBpmTemplateMapper.update(sysBpmTemplate);
        if (rows > 0) {
            loadingBpmTemplateCache();
        }
        return rows;
    }

    /**
     * 删除流程模板信息
     *
     * @param id 流程模板主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteById(Long id) {
        SysBpmTemplate sysBpmTemplate = sysBpmTemplateMapper.selectById(id);
        Result<String> template = templateManager.deleteTemplate(sysBpmTemplate.getTemplateKey());
        if (0 != template.getStatus()) {
            throw MessageUtils.exception(SysErrorCodeConstants.BMP_DELETE_TEMPLATE_FAIL, template.getMsg());
        }
        int rows = sysBpmTemplateMapper.deleteById(id);
        if (rows > 0) {
            loadingBpmTemplateCache();
        }
        return rows;
    }
}
