package com.qzt.module.system.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户 用户名|手机|邮箱 + 密码登录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PwdLoginVO {
    @ApiModelProperty(value = "登录账号（用户名|手机|邮箱）", required = true,example = "admin")
    @NotEmpty(message = "登录账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true,example = "207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @ApiModelProperty(value = "唯一标识(用于校验验证码)",required = true)
    @NotEmpty(message = "唯一标识不能为空")
    private String uuid;

}
