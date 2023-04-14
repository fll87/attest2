package com.qzt.module.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qzt.common.validation.Mobile;
import com.qzt.common.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel("企业注册")
@Data
public class RegisterVO {
    /** 企业名称 */
    @ApiModelProperty(value = "企业名称", required = true)
    @NotBlank(message = "企业名称不能为空")
    @Size(min = 4, max = 128, message = "企业名称长度不能超过128个字符")
    @Xss(message = "企业名称不能包含脚本字符")
    private String customerName;
    /** 社会代码 */
    @ApiModelProperty(value = "社会代码", required = true)
    @NotBlank(message = "社会代码不能为空")
    @Size(min = 3, max = 64, message = "社会代码不能超过64个字符")
    private String socialCode;
    /** 地址 */
    @ApiModelProperty(value = "地址", required = true)
    @NotBlank(message = "地址不能为空")
    @Size(min = 3, max = 64, message = "地址长度不能超过64个字符")
    private String address;
    /** 企业类型 */
    @ApiModelProperty(value = "企业类型", required = true)
    @NotNull(message = "企业类型不能为空")
    @Max(value = 99 ,message = "企业类型长度不能超过2位数")
    private Integer type;
    /** 法人 */
    @ApiModelProperty(value = "企业类型", required = true)
    @NotBlank(message = "法人不能为空")
    @Size(min = 2, max = 32, message = "法人长度不能超过32个字符")
    private String legalPerson;
    /** 法人身份证号（sm4加密） */
    @ApiModelProperty(value = "法人身份证号", required = true)
    @NotBlank(message = "法人身份证号不能为空")
    @Size(min = 1, max = 100, message = "法人身份证号长度不能超过100个字符")
    private String legalPersonNum;

    /** 注册日期 */
    @ApiModelProperty(value = "注册日期格式yyyy-MM-dd", required = true)
    @NotNull(message = "注册日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regdate;

    /** 营业有效期 */
    @ApiModelProperty(value = "营业有效期格式yyyy-MM-dd", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "营业有效期不能为空")
    private Date expirationDate;

    /** 经营范围 */
    @ApiModelProperty(value = "经营范围", required = true)
    @NotBlank(message = "经营范围不能为空")
    @Size(min = 1, max = 512, message = "经营范围长度不能超过512个字符")
    private String businessScope;

    /** 联系人 */
    @ApiModelProperty(value = "联系人", required = true)
    @NotBlank(message = "联系人不能为空")
    @Size(min = 1, max = 32, message = "联系人长度不能超过32个字符")
    private String contacts;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    @Size(min = 1, max = 30, message = "账号长度不能超过30个字符")
    private String account;
    /** 密码 */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 8,  message = "密码长度不能小于8个字符")
    private String password;
    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Mobile(message = "手机号格式错误")
    private String tel;
    /** 邮箱 */
    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(min = 1, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
    /** 营业执照图片 */
    @ApiModelProperty(value = "营业执照图片", required = true,dataType = "file",example = "file文件类型")
    @NotNull(message = "营业执照图片不能为空")
    private MultipartFile picture;

    /** 手机验证码 */
    @ApiModelProperty(value = "手机验证码", required = true)
    @NotBlank(message = "手机验证码不能为空")
    private String telCode;
    /** 手机验证码-uuid */
    @ApiModelProperty(value = "手机验证码-uuid", required = true)
    @NotBlank(message = "手机验证码-uuid不能为空")
    private String telCodeUuid;

    /** 邮箱验证码 */
    @ApiModelProperty(value = "邮箱验证码", required = true)
    @NotBlank(message = "邮箱验证码不能为空")
    private String emailCode;
    /** 邮箱验证码-uuid */
    @ApiModelProperty(value = "手机验证码-uuid", required = true)
    @NotBlank(message = "邮箱验证码-uuid不能为空")
    private String emailCodeUuid;



}
