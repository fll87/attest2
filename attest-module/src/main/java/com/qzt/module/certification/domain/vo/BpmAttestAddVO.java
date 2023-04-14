package com.qzt.module.certification.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qzt.common.annotation.Excel;
import com.qzt.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel("认证申请对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmAttestAddVO
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "认证服务类型eid",required = true)
    @NotEmpty
    private String attestType;

    @ApiModelProperty(value = "资产eid组",required = true)
    @NotEmpty
    private String[] assets;

    @ApiModelProperty("附件")
    @NotNull
    private MultipartFile[] files;


}
