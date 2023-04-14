package com.qzt.module.certification.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qzt.common.annotation.Excel;
import com.qzt.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("认证管理对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmAttest extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long id;

    @Excel(name = "流程id")
    @ApiModelProperty(value = "流程id")
    private String processId;

    @Excel(name = "流程名称")
    @ApiModelProperty(value = "流程名称")
    private String processName;

    @Excel(name = "模板id")
    @ApiModelProperty(value = "模板id")
    private String templateId;

    @Excel(name = "模板key")
    @ApiModelProperty(value = "模板key")
    private String templateKey;

    @Excel(name = "企业id")
    @ApiModelProperty(value = "模板key")
    private Long customerId;

    @Excel(name = "流程状态", readConverterExp = "1=进行中,4=已取消,6=已通过,7=未通过")
    @ApiModelProperty(value = "流程状态（1进行中 4已取消 6已通过 7未通过）")
    private Integer status;

    @Excel(name = "认证服务类别(sys_attest_type主键)")
    @ApiModelProperty(value = "认证服务类别(sys_attest_type主键)")
    private Long attestType;

    /** 流程开始时间 */
    @Excel(name = "流程开始时间")
    @ApiModelProperty(value = "流程开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 流程结束时间 */
    @Excel(name = "流程结束时间")
    @ApiModelProperty(value = "流程结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(hidden = true)
    private Long deleted;


}
