package com.qzt.module.system.domain.vo.code;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("获取验证码返回DATA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeResVO {

    @ApiModelProperty("唯一标识")
    private String uuid;

    @ApiModelProperty("二维码图片")
    private String img;
}
