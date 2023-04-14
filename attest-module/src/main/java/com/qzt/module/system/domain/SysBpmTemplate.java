package com.qzt.module.system.domain;

import com.qzt.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("流程模板对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysBpmTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("模板key")
    private String templateKey;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty(hidden = true)
    private String path;


}
