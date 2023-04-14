package com.qzt.common.constant;

import com.qzt.common.core.domain.ErrorCode;

/**
 * 企业前端 错误码枚举类
 *
 * app 系统，使用 2-001-000-000 段
 */
public interface AppErrorCodeConstants {
    ErrorCode FILE_ERROR = new ErrorCode(2001000000, "处理文件异常");
    // ========== 注册 模块 2001000000 ==========
    ErrorCode REG_COMPANYNAME_EXISTS = new ErrorCode(2001000001, "注册企业名称已存在");
    ErrorCode REG_SOCIALCODE_EXISTS = new ErrorCode(2001000002, "注册社会代码已存在");
    ErrorCode USER_ACCOUNT_EXISTS = new ErrorCode(2001000003, "注册用户名名称已存在");
    ErrorCode USER_MOBILE_EXISTS = new ErrorCode(2001000004, "注册手机号已经存在");
    ErrorCode USER_EMAIL_EXISTS = new ErrorCode(2001000005, "邮箱已经存在");
    ErrorCode REG_MOBILEUUID_EXISTS = new ErrorCode(2001000006, "注册手机uuid不存在或已失效");
    ErrorCode REG_MOBILECODE_EXISTS = new ErrorCode(2001000007, "注册手机验证码不存在");
    ErrorCode REG_EMAILUUID_EXISTS = new ErrorCode(2001000008, "注册邮箱uuid不存在或已失效");
    ErrorCode REG_EMAILCODE_EXISTS = new ErrorCode(2001000009, "注册邮箱验证码不存在");
    ErrorCode USER_ACCOUNT_SPECIAL_EXISTS = new ErrorCode(2001000010, "注册用户名名称包含了特殊字符已存在");

    ErrorCode UPDATE_COMPANYNAME_EXISTS = new ErrorCode(2001000011, "修改企业名称已存在");
    ErrorCode UPDATE_SOCIALCODE_EXISTS = new ErrorCode(2001000012, "修改社会代码已存在");
    ErrorCode UPDATE_MOBILEUUID_EXISTS = new ErrorCode(2001000013, "修改手机uuid不存在或已失效");
    ErrorCode UPDATE_MOBILECODE_EXISTS = new ErrorCode(2001000014, "修改手机验证码不存在");
    ErrorCode UPDATE_MOBILE_EXISTS = new ErrorCode(2001000015, "修改手机号已经存在");
    ErrorCode UPDATE_EMAILUUID_EXISTS = new ErrorCode(2001000016, "修改邮箱uuid不存在或已失效");
    ErrorCode UPDATE_EMAIL_EXISTS = new ErrorCode(2001000017, "修改邮箱已经存在");
    ErrorCode UPDATE_EMAILCODE_EXISTS = new ErrorCode(2001000018, "修改邮箱验证码不存在");


    //========== 资产管理 模块 2001001000 ==========
    ErrorCode ASSET_DATA_NOT_EXISTS = new ErrorCode(2001001001, "当前资产数据不存在");
    ErrorCode ASSET_SORT_IS_NULL = new ErrorCode(2001001002, "资产类型不是主机时，资产类别不能为空");
    ErrorCode IP_OR_URL_CHECK = new ErrorCode(2001001003, "Ip/url合法性错误");

}
