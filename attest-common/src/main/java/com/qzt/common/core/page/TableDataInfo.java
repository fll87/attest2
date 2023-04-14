package com.qzt.common.core.page;

import com.qzt.common.core.domain.R;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author
 */
@ApiModel("Page Result")
public class TableDataInfo<T> extends R<T>
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总记录数")
    private long intTotalRow;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total)
    {
        this.setData((T) list);
        this.intTotalRow = total;
    }

    public long getIntTotalRow()
    {
        return intTotalRow;
    }

    public void setIntTotalRow(long intTotalRow)
    {
        this.intTotalRow = intTotalRow;
    }

}
