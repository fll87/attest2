package com.qzt.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.reflect.ReflectUtils;
import com.qzt.common.utils.sign.ToolUtil;
import io.swagger.annotations.ApiModelProperty;

/**
 * Entity基类
 *
 * @author
 */
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String searchValue;

    /** 创建者 */
    @ApiModelProperty(hidden = true)
    private String creator;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(hidden = true)
    private String updater;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty(value = "备注",example = "")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(hidden = true)
    private Map<String, Object> params;
    //id加密值
    @ApiModelProperty(value = "主键",example = "")
    private String eid;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdater()
    {
        return updater;
    }

    public void setUpdater(String updater)
    {
        this.updater = updater;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public String getEid() {
        if(StringUtils.isBlank(eid)){
            try {
                Long id =  ReflectUtils.getFieldValue(this, "id");
                if (id != null) {
                    //sm4 加密后 同时把id设置为空
                    this.eid = ToolUtil.encryptSm4(id.toString());
                    ReflectUtils.setFieldValue(this,"id", null);
                }
            }catch (Exception e){
                System.out.println("===加密失败===");
            }
        }
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
        if(StringUtils.isNotBlank(eid)){
            try {
                Long decrypt = Long.valueOf(ToolUtil.decryptSm4(eid));
                ReflectUtils.setFieldValue(this,"id", decrypt);
            }catch (Exception e){
                System.out.println("==eid解密失败==");
            }
        }
        this.eid = eid;
    }
}
