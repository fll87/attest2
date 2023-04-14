package com.qzt.framework.web.service;

import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.app.domain.AppCustomer;
import com.qzt.module.app.service.IAppCustomerService;
import com.qzt.module.system.domain.vo.user.RegisterVO;
import com.qzt.module.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册校验方法
 *
 * @author
 */
@Component
public class SysRegisterService {

    private static final Logger log = LoggerFactory.getLogger(SysRegisterService.class);

    @Autowired
    private IAppCustomerService appCustomerService;
    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    private Lock lock = new ReentrantLock();

    /**
     * 注册
     */
    public void register(RegisterVO reqVO) {
        //用户名特殊字符验证
        Pattern p = Pattern.compile( ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*");
        Matcher m = p.matcher(reqVO.getAccount());
        if (m.find()) {
            throw new ServiceException(AppErrorCodeConstants.USER_ACCOUNT_SPECIAL_EXISTS);
        }
        //验证密码 最少8位，包含大、小写字母、数字、特殊字符其中三种类型组合
        /*Pattern p = Pattern.compile("^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,20}$");
        Matcher m = p.matcher(reqVO.getPassword());
        if (!m.find()) {
            System.out.println("包含");
        }*/
        //验证手机验证码
        if(redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY+reqVO.getTelCodeUuid()) == null){
            throw new ServiceException(AppErrorCodeConstants.REG_MOBILEUUID_EXISTS);
        }else if(!redisCache.getCacheObject(CacheConstants.SMS_CODE_KEY+reqVO.getTelCodeUuid()).toString().equals(reqVO.getTelCode())){
            throw new ServiceException(AppErrorCodeConstants.REG_MOBILECODE_EXISTS);
        }
        //邮箱验证码
        if(redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY+reqVO.getEmailCodeUuid()) == null){
            throw new ServiceException(AppErrorCodeConstants.REG_EMAILUUID_EXISTS);
        }else if(!redisCache.getCacheObject(CacheConstants.EMAIL_CODE_KEY+reqVO.getEmailCodeUuid()).toString().equals(reqVO.getEmailCode())){
            throw new ServiceException(AppErrorCodeConstants.REG_EMAILCODE_EXISTS);
        }

        try {
            lock.lock();
            AppCustomer appCustomer = new AppCustomer();
            appCustomer.setName(reqVO.getCustomerName());
            appCustomer.setSocialCode(reqVO.getSocialCode());
            appCustomer.setAccount(reqVO.getAccount());
            appCustomer.setTel(reqVO.getTel());
            appCustomer.setEmail(reqVO.getEmail());
            //验证企业名称、社会代码、账号、手机号、邮箱 其中之一是否存在
            List<AppCustomer> list = appCustomerService.selectCustomerExists(appCustomer);
            for (AppCustomer app : list){
                if(reqVO.getCustomerName().equals(app.getName())){
                    throw new ServiceException(AppErrorCodeConstants.REG_COMPANYNAME_EXISTS);
                }
                if(reqVO.getSocialCode().equals(app.getSocialCode())){
                    throw new ServiceException(AppErrorCodeConstants.REG_SOCIALCODE_EXISTS);
                }
                if(reqVO.getAccount().equals(app.getAccount())){
                    throw new ServiceException(AppErrorCodeConstants.USER_ACCOUNT_EXISTS);
                }
                if(reqVO.getTel().equals(app.getTel())){
                    throw new ServiceException(AppErrorCodeConstants.USER_MOBILE_EXISTS);
                }
                if(reqVO.getEmail().equals(app.getEmail())){
                    throw new ServiceException(AppErrorCodeConstants.USER_EMAIL_EXISTS);
                }

            }
            //用户表验证手机号
            SysUser user = new SysUser();
            user.setUsername(reqVO.getAccount());
            user.setMobile(reqVO.getTel());
            user.setEmail(reqVO.getEmail());
            List<SysUser> userList = userService.selectUserUnique(user);
            for (SysUser u : userList){
                if(u.getUsername().equals(reqVO.getAccount()) || u.getMobile().equals(reqVO.getAccount()) || u.getEmail().equals(reqVO.getAccount())){
                    throw new ServiceException(AppErrorCodeConstants.USER_ACCOUNT_EXISTS);
                }else if(u.getMobile().equals(reqVO.getTel()) || u.getUsername().equals(reqVO.getTel()) || u.getEmail().equals(reqVO.getTel()) ){
                    throw new ServiceException(AppErrorCodeConstants.USER_MOBILE_EXISTS);
                }else if(u.getEmail().equals(reqVO.getEmail()) || u.getUsername().equals(reqVO.getEmail()) || u.getMobile().equals(reqVO.getEmail()) ){
                    throw new ServiceException(AppErrorCodeConstants.USER_EMAIL_EXISTS);
                }
            }
            //通过存文件
            String filePath = AttestConfig.getProfile() + "/register";
            try {
                String upload = FileUploadUtils.upload(filePath, reqVO.getPicture());
                appCustomer.setPictureUrl(upload);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("====处理注册上传文件异常===="+e);
                throw new ServiceException(AppErrorCodeConstants.FILE_ERROR);
            }
            //验证通过保存相关数据
            appCustomer.setType(reqVO.getType());
            appCustomer.setStatus(0);
            appCustomer.setLogonStatus(0);
            appCustomer.setAttestStatus(0);
            appCustomer.setAddress(reqVO.getAddress());
            appCustomer.setLegalPerson(reqVO.getLegalPerson());
            //sm4
            appCustomer.setLegalPersonNum(ToolUtil.encryptSm4(reqVO.getLegalPersonNum()));
            appCustomer.setRegdate(reqVO.getRegdate());
            appCustomer.setExpirationDate(reqVO.getExpirationDate());
            appCustomer.setBusinessScope(reqVO.getBusinessScope());
            appCustomer.setContacts(reqVO.getContacts());
            appCustomer.setCreateTime(new Date());
            appCustomer.setDeleted(0);
            appCustomer.setAccount(reqVO.getAccount());
            //sm4
            appCustomer.setPassword(ToolUtil.encryptSm4(reqVO.getPassword()));
            appCustomer.setTel(reqVO.getTel());
            appCustomer.setEmail(reqVO.getEmail());
            appCustomerService.insertAppCustomer(appCustomer);
        }finally {
            lock.unlock();
        }

    }

}
