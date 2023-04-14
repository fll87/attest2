package com.qzt.module.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qzt.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("用户列表-请求参数对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListReqVO {

    @ApiModelProperty(value = "用户类型（0管理端 1企业端）")
    private Integer userType;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "帐号状态(0正常 1停用)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
