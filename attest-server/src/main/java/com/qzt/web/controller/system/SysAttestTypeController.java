package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.core.text.Convert;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.system.domain.SysAttestType;
import com.qzt.module.system.domain.vo.user.SysAttestTypeTreeVO;
import com.qzt.module.system.service.ISysAttestTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "认证服务类型管理")
@RestController
@RequestMapping("/system/type")
public class SysAttestTypeController extends BaseController {
    @Autowired
    private ISysAttestTypeService sysAttestTypeService;

    @ApiOperation(value = "查询认证服务类型列表")
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo<SysAttestType> list(SysAttestType sysAttestType) {
        startPage();
        List<SysAttestType> list = sysAttestTypeService.selectList(sysAttestType);
        return getDataTable(list);
    }

    @ApiOperation(value = "查询认证服务类型列表树结构")
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/tree")
    public R<List<SysAttestTypeTreeVO>> treeList(SysAttestType sysAttestType) {
        List<SysAttestType> list = sysAttestTypeService.selectList(sysAttestType);
        return R.ok(sysAttestTypeService.buildTree(list));
    }

    @ApiOperation(value = "获取认证服务类型详细信息")
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{eid}")
    public R<SysAttestType> getInfo(@PathVariable("eid") String eid) {
        return R.ok(sysAttestTypeService.selectById(Convert.toLong(ToolUtil.decryptSm4(eid))));
    }

    @ApiOperation(value = "新增认证服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "服务类型名称", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderNum", value = "显示顺序", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "peid", value = "父ID（没有为空）", dataType = "String"),
    })
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "认证服务类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(SysAttestType sysAttestType) {
        sysAttestType.setCreator(getUsername());
        sysAttestTypeService.save(sysAttestType);
        return R.ok();
    }

    @ApiOperation(value = "修改认证服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eid", value = "主键ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "name", value = "服务类型名称", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderNum", value = "显示顺序", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "peid", value = "父ID（没有为空）", dataType = "String"),
    })
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "认证服务类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(SysAttestType sysAttestType) {
        sysAttestType.setUpdater(getUsername());
        sysAttestTypeService.update(sysAttestType);
        return R.ok();
    }

    @ApiOperation(value = "删除认证服务类型")
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "认证服务类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eids}")
    public R<String> remove(@PathVariable String[] eids) {
        List<Long> ids = new ArrayList<>();
        for (String eid : eids){
            Long id = Convert.toLong(ToolUtil.decryptSm4(eid));
            ids.add(id);
        }
        sysAttestTypeService.deleteByIds(ids);
        return R.ok();
    }
}
