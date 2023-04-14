package com.qzt.module.system.domain;

import com.qzt.common.core.domain.BaseEntity;
import com.qzt.common.core.text.Convert;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.sign.ToolUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel("认证服务类型")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAttestType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "服务类型名称")
    private String name;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(hidden = true)
    private Long parentId;

    @ApiModelProperty(value = "父ID")
    private String peid;

    @ApiModelProperty(hidden = true)
    private Long deleted;

    private List<SysAttestType> children = new ArrayList<SysAttestType>();

    public String getPeid() {
        this.peid = Objects.isNull(parentId) || parentId == 0 ? "" : ToolUtil.encryptSm4(Convert.toStr(parentId));
        return peid;
    }

    public void setPeid(String peid) {
        this.peid = peid;
        this.setParentId(StringUtils.isEmpty(peid) ? 0 : Convert.toLong(ToolUtil.decryptSm4(peid)));
    }
}
