package com.qzt.module.system.domain.vo.user;

import com.qzt.common.validation.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("发送邮箱验证码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCodeVO {

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    @Email
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    /**
     * 发送场景
     */
    @ApiModelProperty(value = "验证码场景(1注册 2修改邮箱)", access = "1,2", required = true, example = "1")
    @NotNull(message = "发送场景不能为空")
    @InEnum(intValues = {1, 2})
    private Integer scene;


}
