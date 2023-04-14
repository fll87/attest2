package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.TreeSelect;
import com.qzt.common.core.domain.entity.SysMenu;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.StringUtils;
import com.qzt.module.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @ApiOperation("获取菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public R<List<SysMenu>> list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return R.ok(menus);
    }

    @ApiOperation("根据菜单编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public R<SysMenu> getInfo(@PathVariable Long menuId)
    {
        return R.ok(menuService.selectMenuById(menuId));
    }

    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public R<List<TreeSelect>> treeselect(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return R.ok(menuService.buildMenuTreeSelect(menus));
    }

    @ApiOperation("获取角色菜单ID")
    @ApiResponse(code = R.SUCCESS,message = "data=>{checkedKeys:选中菜单ID,menus:菜单树结构列表}")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public R<List<Long>> roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        Map data = new HashMap<>();
        data.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        data.put("menus", menuService.buildMenuTreeSelect(menus));
        return R.ok(menuService.selectMenuListByRoleId(roleId));
    }

    @ApiOperation("新增菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail(SysErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
        else if (menu.getOuterChain() && !StringUtils.ishttp(menu.getPath()))
        {
            return R.fail(SysErrorCodeConstants.MENU_PATH_NOT_HTTP);
        }
        menu.setCreator(getUsername());
        menuService.insertMenu(menu);
        return R.ok();
    }

    @ApiOperation("修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return R.fail(SysErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
        else if (menu.getOuterChain() && !StringUtils.ishttp(menu.getPath()))
        {
            return R.fail(SysErrorCodeConstants.MENU_PATH_NOT_HTTP);
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return R.fail(SysErrorCodeConstants.MENU_PARENT_ERROR);
        }
        menu.setUpdater(getUsername());
        menuService.updateMenu(menu);
        return R.ok();
    }

    @ApiOperation("删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public R<String> remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return R.fail(SysErrorCodeConstants.MENU_EXISTS_CHILDREN);
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return R.fail(SysErrorCodeConstants.MENU_EXIST_ROLE);
        }
        menuService.deleteMenuById(menuId);
        return R.ok();
    }
}
