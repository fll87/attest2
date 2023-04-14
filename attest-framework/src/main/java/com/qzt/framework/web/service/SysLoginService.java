package com.qzt.framework.web.service;

import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.enums.BusinessStatus;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.*;
import com.qzt.common.utils.ip.IpUtils;
import com.qzt.framework.manager.AsyncManager;
import com.qzt.framework.manager.factory.AsyncFactory;
import com.qzt.framework.security.context.AuthenticationContextHolder;
import com.qzt.framework.security.context.SmsCodeAuthenticationToken;
import com.qzt.module.system.domain.SysOperLog;
import com.qzt.module.system.domain.vo.user.ForgotPwdVO;
import com.qzt.module.system.domain.vo.user.LoginVO;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysPasswordService passwordService;

    /**
     * 登录验证
     *
     * @param vo
     * @return 结果
     */
    public String login(LoginVO vo) {
        // 验证码校验
        String redisKey = UserConstants.USER_LOGIN_PWD.equals(vo.getType()) ? CacheConstants.CAPTCHA_CODE_KEY : CacheConstants.SMS_CODE_KEY;
        validateCode(vo.getUsername(), vo.getCode(), vo.getUuid(), redisKey, true);
        // 登录前置校验
        loginPreCheck(vo.getUsername());
        // 用户验证
        Authentication authentication = null;
        try {
            if (UserConstants.USER_LOGIN_PWD.equals(vo.getType())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(vo.getUsername(), vo.getPassword());
                AuthenticationContextHolder.setContext(authenticationToken);
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                authentication = authenticationManager.authenticate(authenticationToken);

            } else {
                SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(vo.getUsername());
                AuthenticationContextHolder.setContext(authenticationToken);
                authentication = authenticationManager.authenticate(authenticationToken);
            }

        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLoginLog(vo.getUsername(), Constants.LOGIN_FAIL, SysErrorCodeConstants.LOGIN_USER_NOT_EXISTS.getMsg()));
                throw new ServiceException(SysErrorCodeConstants.LOGIN_USER_NOT_EXISTS);
            } else{
                AsyncManager.me().execute(AsyncFactory.recordLoginLog(vo.getUsername(), Constants.LOGIN_FAIL, e.getMessage()));
                if (e instanceof ServiceException) {
                    throw e;
                } else if (e instanceof InternalAuthenticationServiceException && e.getCause() instanceof ServiceException) {
                    throw (ServiceException) e.getCause();
                } else {
                    throw new ServiceException(SysErrorCodeConstants.LOGIN_BAD_CREDENTIALS);
                }
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLoginLog(vo.getUsername(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        passwordService.clearLoginRecordCache(vo.getUsername());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @param redisKey
     * @param logFlag  是否记录登录日志
     * @return 结果
     */
    public void validateCode(String username, String code, String uuid, String redisKey, Boolean logFlag) {
        boolean bool = true;
        if (CacheConstants.CAPTCHA_CODE_KEY.equals(redisKey)) {
            bool = configService.selectCaptchaEnabled();
        } else if (CacheConstants.SMS_CODE_KEY.equals(redisKey)) {
            bool = configService.selectSmsEnabled();
        } else if (CacheConstants.EMAIL_CODE_KEY.equals(redisKey)) {
            bool = configService.selectEmailEnabled();
        }
        if (bool) {
            String verifyKey = redisKey + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                if (logFlag) {
                    AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.CAPTCHA_EXPIRED.getMsg()));
                }
                throw new ServiceException(SysErrorCodeConstants.CAPTCHA_EXPIRED);
            }
            if (!code.equalsIgnoreCase(captcha)) {
                if (logFlag && CacheConstants.SMS_CODE_KEY.equals(redisKey)) {
                    passwordService.validateSms(username);
                }else if (logFlag) {
                    AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.CAPTCHA_CODE_ERROR.getMsg()));
                }
                throw new ServiceException(SysErrorCodeConstants.CAPTCHA_CODE_ERROR);
            }
            redisCache.deleteObject(verifyKey);
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     */
    public void loginPreCheck(String username) {
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(username, Constants.LOGIN_FAIL, SysErrorCodeConstants.USER_IS_BLACK.getMsg()));
            throw new ServiceException(SysErrorCodeConstants.USER_IS_BLACK);
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 忘记密码
     *
     * @param vo
     * @return
     */
    public int forgotPwd(ForgotPwdVO vo) {
        long startTime = System.currentTimeMillis();
        // 验证码校验
        String redisKey = UserConstants.CODE_EMAIL.equals(vo.getType()) ? CacheConstants.EMAIL_CODE_KEY : CacheConstants.SMS_CODE_KEY;
        validateCode(vo.getUsername(), vo.getCode(), vo.getUuid(), redisKey, false);
        SysUser user = new SysUser();
        try {
            user = userService.checkUserEff(vo.getUsername());
            // 更新密码
            user.setPassword(SecurityUtils.encryptPassword(vo.getPassword()));
            user.setPwdUpdateTime(DateUtils.getNowDate());
            int rows = userService.resetPwd(user);
            if (rows > 0) {
                // 记录日志
                SysOperLog operLog = new SysOperLog();
                operLog.setOperName(user.getUsername());
                operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
                operLog.setOperIp(IpUtils.getIpAddr());
                operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
                operLog.setMethod("SysLoginService.forgotPwd()");
                operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
                operLog.setBusinessType(BusinessType.UPDATE.ordinal());
                operLog.setTitle("忘记密码");
                operLog.setOperatorType(user.getUserType());
                operLog.setCostTime(System.currentTimeMillis() - startTime);
            }
            return rows;
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordLoginLog(user.getUsername(), Constants.LOGIN_FAIL, SysErrorCodeConstants.LOGIN_FIND_PWD_ERROR.getMsg()));
            throw new ServiceException(SysErrorCodeConstants.LOGIN_FIND_PWD_ERROR);
        }
    }

}
