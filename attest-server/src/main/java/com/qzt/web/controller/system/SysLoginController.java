package com.qzt.web.controller.system;

import com.qzt.common.annotation.Anonymous;
import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.entity.SysMenu;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.bean.BeanUtils;
import com.qzt.framework.web.service.SysLoginService;
import com.qzt.framework.web.service.SysPermissionService;
import com.qzt.module.system.domain.vo.user.*;
import com.qzt.module.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Api(tags = "登录")
@RestController
@Validated
@Slf4j
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @PostMapping("/pwd-login")
    @ApiOperation(value = "使用用户名/手机/邮箱 + 密码登录", notes = "登录成功返回【token】，需授权的接口将Authorization=【token】放在header中请求")
    public R<String> pwdLogin(@RequestBody @Valid PwdLoginVO reqVO) {
        LoginVO vo = new LoginVO();
        BeanUtils.copyBeanProp(vo, reqVO);
        vo.setType(UserConstants.USER_LOGIN_PWD);
        return R.ok(loginService.login(vo));
    }


    @PostMapping("/sms-login")
    @ApiOperation(value = "使用手机 + 验证码登录", notes = "登录成功返回【token】，需授权的接口将Authorization=【token】放在header中请求")
    public R<String> smsLogin(@RequestBody @Valid SmsLoginVO reqVO) {
        LoginVO vo = new LoginVO();
        BeanUtils.copyBeanProp(vo, reqVO);
        vo.setUsername(reqVO.getMobile());
        vo.setType(UserConstants.USER_LOGIN_SMS);
        return R.ok(loginService.login(vo));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录")
    public void logout() {
    }

    @PostMapping("/forgot-pwd")
    @ApiOperation(value = "忘记密码")
    @Anonymous
    public R<String> forgotPwd(@RequestBody @Valid ForgotPwdVO reqVO) {
        loginService.forgotPwd(reqVO);
        return R.ok();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @ApiOperation(value = "获取用户信息")
    public R<UserDetailVO> getInfo() {
        SysUser curUser = SecurityUtils.getLoginUser().getUser();
        UserInfoVO user = new UserInfoVO();
        BeanUtils.copyBeanProp(user, curUser);
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(curUser);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(curUser);
        UserDetailVO data = UserDetailVO.builder().user(user)
                .roleKeys(new ArrayList<>(roles)).permissions(new ArrayList<>(permissions)).build();
        return R.ok(data);
    }

    /**
     * 获取用户菜单
     *
     * @return 用户菜单信息
     */
    @GetMapping("getMenus")
    @ApiOperation(value = "获取用户菜单")
    public R<List<SysMenu>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menus);
    }
}
