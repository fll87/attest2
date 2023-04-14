package com.qzt.web.controller.certification;

import com.qzt.common.annotation.Log;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.core.text.Convert;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.poi.ExcelUtil;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.certification.domain.BpmAttest;
import com.qzt.module.certification.domain.vo.BpmAttestAddVO;
import com.qzt.module.certification.service.IBpmAttestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "认证流程")
@RestController
@RequestMapping("/bpm/attest")
public class BpmAttestController extends BaseController
{
    @Autowired
    private IBpmAttestService bpmAttestService;

    @ApiOperation(value = "查询认证流程列表")
    @PreAuthorize("@ss.hasPermi('bpm:attest:list')")
    @GetMapping("/list")
    public TableDataInfo<BpmAttest> list(BpmAttest bpmAttest)
    {
        startPage();
        List<BpmAttest> list = bpmAttestService.selectList(bpmAttest);
        return getDataTable(list);
    }

    /**
     * 导出认证流程列表
     */
    @PreAuthorize("@ss.hasPermi('bpm:attest:export')")
    @Log(title = "认证流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmAttest bpmAttest)
    {
        List<BpmAttest> list = bpmAttestService.selectList(bpmAttest);
        ExcelUtil<BpmAttest> util = new ExcelUtil<BpmAttest>(BpmAttest.class);
        util.exportExcel(response, list, "认证流程数据");
    }

    @ApiOperation(value = "获取认证流程详细信息")
    @PreAuthorize("@ss.hasPermi('bpm:attest:query')")
    @GetMapping(value = "/{eid}")
    public R<BpmAttest> getInfo(@PathVariable("eid") String eid)
    {
        Long id = Convert.toLong(ToolUtil.encryptSm4(eid));
        return R.ok(bpmAttestService.selectById(id));
    }

    @ApiOperation(value = "新增认证流程")
    @PreAuthorize("@ss.hasPermi('bpm:attest:add')")
    @Log(title = "认证流程", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody BpmAttestAddVO reqVO)
    {
        bpmAttestService.save(reqVO);
        return R.ok();
    }

    @ApiOperation(value = "修改认证流程")
    @PreAuthorize("@ss.hasPermi('bpm:attest:edit')")
    @Log(title = "认证流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@RequestBody BpmAttest bpmAttest)
    {
        bpmAttest.setUpdater(getUsername());
        bpmAttestService.update(bpmAttest);
        return R.ok();
    }

    @ApiOperation(value = "删除认证流程")
    @PreAuthorize("@ss.hasPermi('bpm:attest:remove')")
    @Log(title = "认证流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{eids}")
    public R<String> remove(@PathVariable String[] eids)
    {
        List<Long> ids = new ArrayList<>();
        for (String eid : eids){
            Long id = Convert.toLong(ToolUtil.decryptSm4(eid));
            ids.add(id);
        }
        bpmAttestService.deleteByIds(ids);
        return R.ok();
    }
}
