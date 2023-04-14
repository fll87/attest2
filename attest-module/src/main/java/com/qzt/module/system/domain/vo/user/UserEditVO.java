package com.qzt.module.system.domain.vo.user;

import com.qzt.common.validation.InEnum;
import com.qzt.common.validation.Mobile;
import com.qzt.common.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel("新增用户")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditVO{

    @NotEmpty(message = "用户ID")
    @ApiModelProperty(value = "用户ID", required = true)
    private String eid;

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 2, max = 30, message = "用户昵称长度不能超过30个字符")
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(min = 2, max = 50, message = "邮箱长度不能超过50个字符")
    @NotEmpty
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @Mobile
    @NotEmpty
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;

    @ApiModelProperty(value = "用户性别(1男 2女)", access = "1,2", required = true, example = "1")
    @InEnum(intValues = {1, 2})
    private Integer sex;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）", access = "0,1", required = true, example = "0")
    @InEnum(intValues = {0, 1})
    private Integer status;

    @ApiModelProperty(value = "角色组")
    private Long[] roleIds;
}
