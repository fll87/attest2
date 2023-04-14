package com.qzt.module.system.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qzt.module.system.domain.SysAttestType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel("认证服务类型树结构")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAttestTypeTreeVO {
    /**
     * 节点ID
     */
    @ApiModelProperty("节点ID")
    private Long id;

    /**
     * 节点名称
     */
    @ApiModelProperty("节点名称")
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty("子节点")
    private List<SysAttestTypeTreeVO> childen;

    public SysAttestTypeTreeVO(SysAttestType attestType) {
        this.id = attestType.getId();
        this.label = attestType.getName();
        this.childen = attestType.getChildren().stream().map(SysAttestTypeTreeVO::new).collect(Collectors.toList());
    }
}
