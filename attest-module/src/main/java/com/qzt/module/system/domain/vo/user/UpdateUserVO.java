package com.qzt.module.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel("修改个人信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserVO {

    @ApiModelProperty(value = "昵称")
    @NotEmpty
    private String nickname;

    @ApiModelProperty(value = "邮箱")
    @NotEmpty
    private String email;

    @ApiModelProperty(value = "手机号码")
    @NotEmpty
    private String mobile;

    @ApiModelProperty(value = "用户性别(1男 2女)")
    @NotNull
    private Integer sex;

    @ApiModelProperty(value = "邮箱验证码")
    private String emailCode;

    @ApiModelProperty(value = "邮箱验证uuid")
    private String emailUuid;

    @ApiModelProperty(value = "手机验证码")
    private String mobileCode;

    @ApiModelProperty(value = "手机验证uuid")
    private String mobileUuid;

}
