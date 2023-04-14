package com.qzt.module.app.domain;

import com.qzt.common.annotation.Excel;
import com.qzt.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 对象 app_asset
 * 
 * @author
 * @date 2023-04-12
 */
@Data
@ApiModel("资产信息")
public class AppAsset extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 企业id */
    @Excel(name = "企业id")
    private Long customerId;

    /** 资产名称 */
    @Excel(name = "资产名称")
    @ApiModelProperty(name = "资产名称", value = "资产名称",required = true)
    @NotBlank(message = "资产名称不能为空")
    private String name;

    /** 类型1主机2web */
    @Excel(name = "类型1主机2web")
    @ApiModelProperty(name = "资产类型", value = "资产类型",required = true)
    @NotNull(message = "资产类型不能为空")
    private Long type;

    /** 资产类别 */
    @Excel(name = "资产类别")
    @ApiModelProperty(name = "资产类别", value = "资产类别",required = true)
    private Long sort;

    /** 网络类型 1内网2外网 */
    @Excel(name = "1内网2外网")
    @ApiModelProperty(name = "网络类型", value = "网络类型",required = true)
    @NotNull(message = "网络类型不能为空")
    private Long netWork;

    /** 资产ip、url（sm4加密） */
    @Excel(name = "ip、url")
    @ApiModelProperty(name = "资产ip/url", value = "资产ip/url",required = true)
    @NotNull(message = "资产ip/url不能为空")
    private String ip;

    /** mac */
    @Excel(name = "mac")
    @ApiModelProperty(name = "mac", value = "mac")
    private String mac;

    /** 重要程度 */
    @Excel(name = "重要程度")
    private Long importance;

    /** 描述 */
    @Excel(name = "描述")
    private String desc;

    /** 是否删除0正常；1删除 */
    @Excel(name = "是否删除0正常；1删除")
    private Long deleted;

    //账号
    @ApiModelProperty(name = "账号", value = "账号")
    private String account;
    //密码
    @ApiModelProperty(name = "密码", value = "密码")
    private String password;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerId", getCustomerId())
            .append("name", getName())
            .append("type", getType())
            .append("sort", getSort())
            .append("netWork", getNetWork())
            .append("ip", getIp())
            .append("mac", getMac())
            .append("importance", getImportance())
            .append("desc", getDesc())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deleted", getDeleted())
            .toString();
    }
}
