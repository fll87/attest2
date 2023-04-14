package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.entity.SysRole;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.core.text.Convert;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.bean.BeanUtils;
import com.qzt.common.utils.poi.ExcelUtil;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.system.domain.vo.user.*;
import com.qzt.module.system.service.ISysRoleService;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "获取用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo<UserListReqVO> list(UserListReqVO user) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyBeanProp(sysUser, user);
        startPage();
        List<SysUser> list = userService.selectUserList(sysUser);
        List<UserListRspVO> data = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)) {
            list.forEach(item -> {
                UserListRspVO vo = new UserListRspVO();
                BeanUtils.copyBeanProp(vo, item);
                vo.setEid(ToolUtil.encryptSm4(Convert.toStr(item.getUserId())));
                data.add(vo);
            });
        }
        return getDataTable(data);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public R<UserImportRespVO> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        return R.ok(userService.importUser(userList, updateSupport));
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @ApiOperation("根据用户编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{eid}"})
    public R<UserDetailVO> getInfo(@PathVariable(value = "eid", required = false) String eid) {
        Long userId = Convert.toLong(ToolUtil.decryptSm4(eid));
        userService.checkUserDataScope(userId);
        Map<String, Object> map = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        UserDetailVO data = UserDetailVO.builder().roles(SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList())).build();
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            UserInfoVO user = new UserInfoVO();
            BeanUtils.copyBeanProp(user, sysUser);
            data.setUser(user);
            data.setRoleIds(sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return R.ok(data);
    }

    @ApiOperation("新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Validated @RequestBody UserAddVO vo) {
        SysUser user = new SysUser();
        BeanUtils.copyBeanProp(user, vo);
        user.setCreator(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(vo.getPassword()));
        userService.insertUser(user);
        return R.ok();
    }

    @ApiOperation("修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@Validated @RequestBody UserEditVO vo) {
        SysUser user = new SysUser();
        BeanUtils.copyBeanProp(user, vo);
        user.setUserId(Convert.toLong(ToolUtil.decryptSm4(vo.getEid())));
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdater(getUsername());
        userService.updateUser(user);
        return R.ok();
    }

    @ApiOperation("删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eids}")
    public R<String> remove(@PathVariable String[] eids) {
        Long curUserId = getUserId();
        List<Long> userIds = new ArrayList<>();
        for (String eid : eids){
            Long id = Convert.toLong(ToolUtil.decryptSm4(eid));
            if(curUserId.equals(id)){
                return R.fail(SysErrorCodeConstants.USER_DELETE_SELF);
            }
            userIds.add(id);
        }
        userService.deleteUserByIds(userIds);
        return R.ok();
    }

    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eid", value = "用户ID"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public R<String> resetPwd(@RequestParam  @Valid @NotEmpty String eid,
                               @RequestParam @Valid @NotEmpty String password) {
        SysUser user = new SysUser();
        user.setUserId(Convert.toLong(ToolUtil.decryptSm4(eid)));
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdater(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(password));
        userService.resetPwd(user);
        return R.ok();
    }

    @ApiOperation("用户状态修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eid", value = "用户ID"),
            @ApiImplicitParam(name = "status", value = "状态",allowableValues="0,1")
    })
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<String> changeStatus(@RequestParam  @Valid @NotEmpty String eid,
                                   @RequestParam @Valid @NotEmpty Integer status) {
        SysUser user = new SysUser();
        user.setUserId(Convert.toLong(ToolUtil.decryptSm4(eid)));
        user.setStatus(status);
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdater(getUsername());
        userService.updateUserStatus(user);
        return R.ok();
    }

    @ApiOperation("根据用户编号获取授权角色")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{eid}")
    public R<UserDetailVO> authRole(@PathVariable("eid") String eid) {
        Long userId = Convert.toLong(ToolUtil.decryptSm4(eid));
        SysUser sysUser = userService.selectUserById(userId);
        UserInfoVO user = new UserInfoVO();
        BeanUtils.copyBeanProp(user, sysUser);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        UserDetailVO data = UserDetailVO.builder().user(user)
                .roles(SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList())).build();
        return R.ok(data);
    }

    @ApiOperation("用户授权角色")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public R<String> insertAuthRole(String eid, Long[] roleIds) {
        Long userId = Convert.toLong(ToolUtil.decryptSm4(eid));
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return R.ok();
    }
}
