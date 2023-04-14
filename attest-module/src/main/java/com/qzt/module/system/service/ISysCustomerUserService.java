package com.qzt.module.system.service;

import com.qzt.module.system.domain.SysCustomerUser;


public interface ISysCustomerUserService
{

    public Long insertCustomerUser(SysCustomerUser sysCustomerUser);

    public void deleteConfigById(Long configIds);

    Long selectCustomerIdByUserId(Long userId);
}
