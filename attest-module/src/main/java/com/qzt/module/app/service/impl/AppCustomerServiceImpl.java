package com.qzt.module.app.service.impl;


import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.entity.SysRole;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.core.text.Convert;
import com.qzt.common.email.MailManager;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.sms.SMSManager;
import com.qzt.common.utils.DateUtils;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.app.domain.AppCustomer;
import com.qzt.module.app.mapper.AppCustomerMapper;
import com.qzt.module.app.service.IAppCustomerService;
import com.qzt.module.system.domain.SysCustomerUser;
import com.qzt.module.system.service.ISysCustomerUserService;
import com.qzt.module.system.service.ISysRoleService;
import com.qzt.module.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 企业信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-04-06
 */
@Service
public class AppCustomerServiceImpl implements IAppCustomerService
{
    private static final Logger log = LoggerFactory.getLogger(AppCustomerServiceImpl.class);


    @Autowired
    private AppCustomerMapper appCustomerMapper;

    @Autowired
    private ISysCustomerUserService customerUserService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private MailManager mailManager;
    @Autowired
    private RedisCache redisCache;

    private Lock lock = new ReentrantLock();

    /**
     * 查询企业信息
     * 
     * @param id 企业信息主键
     * @return 企业信息
     */
    @Override
    public AppCustomer selectAppCustomerById(Long id)
    {
        return appCustomerMapper.selectAppCustomerById(id);
    }

    /**
     * 查询企业信息列表
     * 
     * @param appCustomer 企业信息
     * @return 企业信息
     */
    @Override
    public List<AppCustomer> selectAppCustomerList(AppCustomer appCustomer)
    {
        return appCustomerMapper.selectAppCustomerList(appCustomer);
    }

    /**
     * 新增企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
    @Override
    public int insertAppCustomer(AppCustomer appCustomer)
    {
        return appCustomerMapper.insertAppCustomer(appCustomer);
    }

    /**
     * 修改企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateAppCustomer(AppCustomer appCustomer)
    {

        Long id = null;
        try {
            id = Long.parseLong(ToolUtil.decryptSm4(appCustomer.getEid()));
        }catch (Exception e){
            throw new ServiceException(SysErrorCodeConstants.CUSTOMER_DATA_NOT_EXISTS);
        }
        try {
            lock.lock();
            //查询数据是否存在
            AppCustomer app = selectAppCustomerById(id);
            if(app == null){
                throw new ServiceException(SysErrorCodeConstants.CUSTOMER_DATA_NOT_EXISTS);
            }
            //判断是否更新了手机号，邮箱号,如果更新了，那么则需要验证码，如果没更新，那么则不需要验证码
            if(!app.getTel().equals(appCustomer.getTel())){
                //验证手机验证码
                if(redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY+appCustomer.getTelCodeUuid()) == null){
                    throw new ServiceException(AppErrorCodeConstants.UPDATE_MOBILEUUID_EXISTS);
                }else if(!redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY+appCustomer.getTelCodeUuid()).toString().equals(appCustomer.getTelCode())){
                    throw new ServiceException(AppErrorCodeConstants.UPDATE_MOBILECODE_EXISTS);
                }

            }
            if(!app.getEmail().equals(appCustomer.getEmail())){
                //邮箱验证码
                if(redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY+appCustomer.getEmailCodeUuid()) == null){
                    throw new ServiceException(AppErrorCodeConstants.UPDATE_EMAILUUID_EXISTS);
                }else if(!redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY+appCustomer.getEmailCodeUuid()).toString().equals(appCustomer.getEmailCode())){
                    throw new ServiceException(AppErrorCodeConstants.UPDATE_EMAILCODE_EXISTS);
                }
            }

            //验证企业名称和信用代码是否修改，如果修改则要验证是否存在
            if(!app.getName().equals(appCustomer.getName()) || !app.getSocialCode().equals(app.getSocialCode())
                    || !app.getTel().equals(appCustomer.getTel()) || !app.getEmail().equals(appCustomer.getEmail()) ){
                List<AppCustomer> list = appCustomerMapper.selectCustomerExists(appCustomer);
                for (AppCustomer customer : list){
                    if(customer.getId().intValue() != app.getId().intValue()){
                        //企业名称
                        if(appCustomer.getName().equals(customer.getName())){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_COMPANYNAME_EXISTS);
                        }
                        //信用代码
                        if(appCustomer.getSocialCode().equals(customer.getSocialCode()) ){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_SOCIALCODE_EXISTS);
                        }
                        //更新了那要判断手机号是否存在
                        if(!app.getTel().equals(appCustomer.getTel()) && appCustomer.getTel().equals(customer.getTel()) ){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_MOBILE_EXISTS);
                        }
                        //邮箱
                        if(!app.getEmail().equals(appCustomer.getEmail()) && appCustomer.getEmail().equals(customer.getEmail()) ){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_EMAIL_EXISTS);
                        }

                    }

                }
            }

            //用户表数据验证 手机号或邮箱 是否存在
            if(!app.getTel().equals(appCustomer.getTel()) || !app.getEmail().equals(appCustomer.getEmail()) ){
                SysUser user = new SysUser();
                user.setMobile(appCustomer.getTel());
                user.setEmail(appCustomer.getEmail());
                List<SysUser> userList = userService.selectUserUnique(user);
                for (SysUser u : userList){
                    //绑定的用户id不一样
                    if(app.getUser().getUserId().intValue() != u.getUserId().intValue()){
                        if(u.getUsername().equals(appCustomer.getTel()) || u.getMobile().equals(appCustomer.getTel()) || u.getEmail().equals(appCustomer.getTel()) ){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_MOBILE_EXISTS);
                        }else if(u.getEmail().equals(appCustomer.getEmail()) || u.getUsername().equals(appCustomer.getEmail()) || u.getMobile().equals(appCustomer.getEmail()) ){
                            throw new ServiceException(AppErrorCodeConstants.UPDATE_EMAIL_EXISTS);
                        }
                    }
                }
            }

            //验证都通过则更新数据
            //更新了手机或邮箱那么则更新用户表
            if(!app.getTel().equals(appCustomer.getTel()) || !app.getEmail().equals(appCustomer.getEmail()) ){
                SysUser user = new SysUser();
                user.setUserId(app.getUser().getUserId());
                user.setMobile(appCustomer.getTel());
                user.setEmail(appCustomer.getEmail());
                //企业角色
                SysRole role = roleService.selectRoleByRoleKey("customer");
                Long[] roleIds = {role.getRoleId()};
                user.setRoleIds(roleIds);
                userService.updateUser(user);
            }
            String filePath = AttestConfig.getProfile() + "/register";
            try {
                String upload = FileUploadUtils.upload(filePath, appCustomer.getPicture());
                appCustomer.setPictureUrl(upload);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("====处理企业修改上传文件异常===="+e);
                throw new ServiceException(AppErrorCodeConstants.FILE_ERROR);
            }

            appCustomer.setUpdateTime(DateUtils.getNowDate());
            return appCustomerMapper.updateAppCustomer(appCustomer);
        }finally {
            lock.unlock();
        }

    }

    /**
     * 批量删除企业信息
     * 
     * @param ids 需要删除的企业信息主键
     * @return 结果
     */
    @Override
    public int deleteAppCustomerByIds(String ids)
    {
        return appCustomerMapper.deleteAppCustomerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除企业信息信息
     * 
     * @param id 企业信息主键
     * @return 结果
     */
    @Override
    public int deleteAppCustomerById(Long id)
    {
        return appCustomerMapper.deleteAppCustomerById(id);
    }

    //查询注册企业填写数据是否存在
    @Override
    public List<AppCustomer> selectCustomerExists(AppCustomer app) {
        return appCustomerMapper.selectCustomerExists(app);
    }

    //审核企业
    @Override
    @Transactional
    public void updateLogonVerify(String eid, Integer logonStatus, String description) {
        Long id = null;
        try {
             id = Long.parseLong(ToolUtil.decryptSm4(eid));
        }catch (Exception e){
            throw new ServiceException(SysErrorCodeConstants.CUSTOMER_DATA_NOT_EXISTS);
        }
        if(logonStatus == 2 && StringUtils.isBlank(description)){
            throw new ServiceException(SysErrorCodeConstants.CUSTOMER_AUDIT_RESULT);
        }

        AppCustomer customer = selectAppCustomerById(id);
        if( customer == null ){
            throw new ServiceException(SysErrorCodeConstants.CUSTOMER_DATA_NOT_EXISTS);
        }
        AppCustomer app = new AppCustomer();
        app.setAuditUserId(SecurityUtils.getUserId());
        //审核通过则创建用户数据并进行相关绑定
        if(logonStatus == 1){
            //更新企业状态
            app.setId(customer.getId());
            app.setStatus(0);
            app.setLogonStatus(1);
            app.setAttestStatus(0);
            appCustomerMapper.updateAppCustomer(app);
            SysUser user = new SysUser();
            user.setUsername(customer.getAccount());
            user.setPassword(customer.getPassword());
            //用户昵称为账号
            user.setNickname(customer.getAccount());
            user.setMobile(customer.getTel());
            user.setEmail(customer.getEmail());
            user.setStatus(0);
            user.setUserType(1);
            user.setDeleted(0);
            SysRole role = roleService.selectRoleByRoleKey("customer");
            Long[] roleIds = {role.getRoleId()};
            user.setRoleIds(roleIds);
            Long userId = userService.insertUser(user);
            //关联表
            SysCustomerUser sysCustomerUser = new SysCustomerUser();
            sysCustomerUser.setCustomerId(customer.getId());
            sysCustomerUser.setUserId(userId);
            customerUserService.insertCustomerUser(sysCustomerUser);
        }else{
            app.setId(customer.getId());
            app.setStatus(0);
            app.setLogonStatus(2);
            app.setAttestStatus(0);
            appCustomerMapper.updateAppCustomer(app);
        }
        //发送短信和邮箱通知
        SMSManager.sendSMS(customer.getTel(),description);
        StringBuilder content = new StringBuilder();
        content.append("<p>您好！</p><br/>");
        content.append("<div style='padding-left:40px;'>您提交的企业审核资料不通过；");
        content.append(description);
        content.append("</div><br/>");
        content.append("<div style='padding-left:40px;'>以上内容由安全认证模块自动发送，请勿回复！！<br/></div>");
        // 发送邮件
        mailManager.sendComplexMail("审核通知",content.toString(),new String[]{customer.getEmail()});

    }

    @Override
    public AppCustomer getBindingInfo(Long userId) {
        return appCustomerMapper.getBindingInfo(userId);
    }
}
