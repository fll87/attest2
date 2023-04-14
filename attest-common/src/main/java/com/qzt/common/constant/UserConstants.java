package com.qzt.common.constant;

/**
 * 用户常量信息
 *
 * @author
 */
public class UserConstants
{
    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 异常状态 */
    public static final String EXCEPTION = "1";

    /** 用户封禁状态 */
    public static final String USER_DISABLE = "1";

    /** 角色封禁状态 */
    public static final String ROLE_DISABLE = "1";

    /** 字典正常状态 */
    public static final String DICT_NORMAL = "0";

    /** 是否为系统默认（是） */
    public static final String YES = "1";

    /** 菜单类型（目录） */
    public static final String TYPE_DIR = "M";

    /** 菜单类型（菜单） */
    public static final String TYPE_MENU = "C";

    /** 菜单类型（按钮） */
    public static final String TYPE_BUTTON = "F";

    /** 密码登录 */
    public static final String USER_LOGIN_PWD = "1";

    /** 手机验证码登录 */
    public static final String USER_LOGIN_SMS = "2";

    /** 找回密码类型-邮箱 */
    public static final Integer CODE_EMAIL = 1;

    /** 找回密码类型-手机 */
    public static final Integer CODE_SMS = 2;

    /** ParentView组件标识 */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink组件标识 */
    public final static String INNER_LINK = "InnerLink";

    /** 校验是否唯一的返回标识 */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /** 用户类型 - 企业 */
    public final static Integer USER_TYPE_CUSTOMER = 1;
}
