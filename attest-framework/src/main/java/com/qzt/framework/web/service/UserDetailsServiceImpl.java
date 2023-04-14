package com.qzt.framework.web.service;

import com.qzt.common.constant.UserConstants;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.module.system.service.ISysCustomerUserService;
import com.qzt.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 用户验证处理
 *
 * @author
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysCustomerUserService customerUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.checkUserEff(username);
        passwordService.validate(user);
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        Long userId = user.getUserId();
        Set<String> permissions = permissionService.getMenuPermission(user);
        if(UserConstants.USER_TYPE_CUSTOMER.equals(user.getUserType())){
            Long customerId = customerUserService.selectCustomerIdByUserId(userId);
            return new LoginUser(userId, user, permissions,customerId);
        }
        return new LoginUser(userId, user, permissions);
    }

}
