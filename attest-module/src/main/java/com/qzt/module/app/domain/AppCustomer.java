package com.qzt.module.app.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qzt.common.annotation.Excel;
import com.qzt.common.core.domain.BaseEntity;
import com.qzt.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 企业信息对象 app_customer
 * 
 * @author ruoyi
 * @date 2023-04-06
 */
@Data
@ApiModel("企业信息")
public class AppCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 企业名称 */
    @ApiModelProperty(name = "企业名称", value = "企业名称",required = true)
    @Excel(name = "企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String name;

    /** 企业类型 */
    @ApiModelProperty(name = "企业类型", value = "企业类型", required = true)
    @Excel(name = "企业类型")
    @NotNull(message = "企业类型不能为空")
    private Integer type;

    /** 企业状态（0正常，1冻结） */
    @Excel(name = "企业状态", readConverterExp = "0=正常，1冻结")
    private Integer status;

    /** 企业注册审核状态（0审核中，1已通过，2未通过） */
    @Excel(name = "企业注册审核状态", readConverterExp = "0=审核中，1已通过，2未通过")
    private Integer logonStatus;

    /** 企业认证状态（0未认证，1认证中，2已通过，3未通过） */
    @Excel(name = "企业认证状态", readConverterExp = "0=未认证，1认证中，2已通过，3未通过")
    private Integer attestStatus;

    /** 社会代码 */
    @ApiModelProperty(name = "社会代码", value = "社会代码", required = true)
    @Excel(name = "社会代码")
    @NotBlank(message = "社会代码不能为空")
    private String socialCode;

    /** 地址 */
    @ApiModelProperty(name = "地址", value = "地址", required = true)
    @Excel(name = "地址")
    @NotBlank(message = "地址不能为空")
    private String address;

    /** 法人 */
    @ApiModelProperty(name = "法人", value = "法人", required = true)
    @Excel(name = "法人")
    @NotBlank(message = "法人不能为空")
    private String legalPerson;

    /** 法人身份证号（sm4加密） */
    @ApiModelProperty(name = "法人身份证号", value = "法人身份证号", required = true)
    @Excel(name = "法人身份证号", readConverterExp = "s=m4加密")
    @NotBlank(message = "法人身份证号不能为空")
    private String legalPersonNum;

    /** 注册日期 */
    @ApiModelProperty(name = "注册日期", value = "注册日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "注册日期不能为空")
    @Excel(name = "注册日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date regdate;

    /** 营业有效期 */
    @ApiModelProperty(name = "营业有效期", value = "营业有效期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "营业有效期不能为空")
    @Excel(name = "营业有效期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expirationDate;

    /** 经营范围 */
    @ApiModelProperty(name = "经营范围", value = "经营范围", required = true)
    @Excel(name = "经营范围")
    @NotBlank(message = "经营范围不能为空")
    private String businessScope;

    /** 联系人 */
    @Excel(name = "联系人")
    @ApiModelProperty(name = "联系人", value = "联系人", required = true)
    @NotBlank(message = "联系人不能为空")
    private String contacts;


    /** 图片（营业执照）存储路径 */
    @Excel(name = "图片", readConverterExp = "营业执照")
    private String pictureUrl;
    @ApiModelProperty(name = "营业执照图片", required = true,dataType = "file",example = "file文件类型")
    @NotNull(message = "营业执照图片不能为空")
    private MultipartFile picture;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 审核人 */
    @Excel(name = "审核人")
    private Long auditUserId;

    /** 是否删除0正常；1删除 */
    @Excel(name = "是否删除0正常；1删除")
    private Integer deleted;
    //  '账号'
    private String account;
    //'密码';
    private String password;
    //'手机号'
    @ApiModelProperty(name = "手机号", value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String tel;
    //'邮箱
    @ApiModelProperty(name = "邮箱", value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    private String email;


    /** 手机验证码 */
    @ApiModelProperty(name = "手机验证码")
    private String telCode;
    /** 手机验证码-uuid */
    @ApiModelProperty(name = "手机验证码-uuid")
    private String telCodeUuid;

    /** 邮箱验证码 */
    @ApiModelProperty(name = "邮箱验证码")
    private String emailCode;
    /** 邮箱验证码-uuid */
    @ApiModelProperty(name = "邮箱验证码-uuid")
    private String emailCodeUuid;


    private SysUser user;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("status", getStatus())
            .append("logonStatus", getLogonStatus())
            .append("attestStatus", getAttestStatus())
            .append("socialCode", getSocialCode())
            .append("address", getAddress())
            .append("legalPerson", getLegalPerson())
            .append("legalPersonNum", getLegalPersonNum())
            .append("regdate", getRegdate())
            .append("expirationDate", getExpirationDate())
            .append("businessScope", getBusinessScope())
            .append("contacts", getContacts())
            .append("pictureUrl", getPictureUrl())
            .append("createDate", getCreateDate())
            .append("updateTime", getUpdateTime())
            .append("auditUserId", getAuditUserId())
            .append("deleted", getDeleted())
            .toString();
    }
}
