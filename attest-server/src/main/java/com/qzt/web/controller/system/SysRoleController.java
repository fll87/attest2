package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.entity.SysRole;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.poi.ExcelUtil;
import com.qzt.framework.web.service.SysPermissionService;
import com.qzt.framework.web.service.TokenService;
import com.qzt.module.system.domain.SysUserRole;
import com.qzt.module.system.service.ISysRoleService;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;

    @ApiOperation("获取角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo<SysRole> list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    @ApiOperation("根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public R<SysRole> getInfo(@PathVariable Long roleId)
    {
        roleService.checkRoleDataScope(roleId);
        return R.ok(roleService.selectRoleById(roleId));
    }

    @ApiOperation("新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody SysRole role)
    {
        if (!roleService.checkRoleNameUnique(role))
        {
            return R.fail(MessageUtils.exception(SysErrorCodeConstants.ROLE_NAME_DUPLICATE,role.getRoleName()));
        }
        else if (!roleService.checkRoleKeyUnique(role))
        {
            return R.fail(SysErrorCodeConstants.ROLE_KEY_DUPLICATE);
        }
        role.setCreator(getUsername());
        roleService.insertRole(role);
        return R.ok();

    }

    @ApiOperation("修改保存角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role))
        {
            return R.fail(MessageUtils.exception(SysErrorCodeConstants.ROLE_NAME_DUPLICATE,role.getRoleName()));
        }
        else if (!roleService.checkRoleKeyUnique(role))
        {
            return R.fail(SysErrorCodeConstants.ROLE_KEY_DUPLICATE);
        }
        role.setUpdater(getUsername());

        if (roleService.updateRole(role) > 0)
        {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
            {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUsername(loginUser.getUser().getUsername()));
                tokenService.setLoginUser(loginUser);
            }
        }
        return R.ok();
    }

    @ApiOperation("角色状态修改")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdater(getUsername());
        roleService.updateRoleStatus(role);
        return R.ok();
    }

    @ApiOperation("删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public R<String> remove(@PathVariable Long[] roleIds)
    {
        roleService.deleteRoleByIds(roleIds);
        return R.ok();
    }

    @ApiOperation("获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public R<List<SysRole>> optionselect()
    {
        return R.ok(roleService.selectRoleAll());
    }

    @ApiOperation("查询已分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo<SysUser> allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    @ApiOperation("查询未分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo<SysUser> unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    @ApiOperation("取消授权用户")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public R<String> cancelAuthUser(@RequestBody SysUserRole userRole)
    {
        roleService.deleteAuthUser(userRole);
        return R.ok();
    }

    @ApiOperation("批量取消授权用户")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public R<String> cancelAuthUserAll(Long roleId, Long[] userIds)
    {
        roleService.deleteAuthUsers(roleId, userIds);
        return R.ok();
    }

    @ApiOperation("批量选择用户授权")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public R<String> selectAuthUserAll(Long roleId, Long[] userIds)
    {
        roleService.checkRoleDataScope(roleId);
        roleService.insertAuthUsers(roleId, userIds);
        return R.ok();
    }

}
