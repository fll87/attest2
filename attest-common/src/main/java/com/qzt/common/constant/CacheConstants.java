package com.qzt.common.constant;

/**
 * 缓存的key 常量
 *
 * @author
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 图形验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "valid_codes:";

    /**
     * 手机验证码 redis key
     */
    public static final String SMS_CODE_KEY = "sms_codes:";

    /**
     * 邮箱验证码 redis key
     */
    public static final String EMAIL_CODE_KEY = "email_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 流程模板 redis key
     */
    public static final String BPM_KEY = "bpm_key";

    /**
     * 认证服务类型 redis key
     */
    public static final String ATTEST_TYPE = "attest_type";

}
