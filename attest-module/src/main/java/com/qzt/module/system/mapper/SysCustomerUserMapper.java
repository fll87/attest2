package com.qzt.module.system.mapper;


import com.qzt.module.system.domain.SysCustomerUser;

/**
 * 参数配置 数据层
 *
 * @author
 */
public interface SysCustomerUserMapper
{
    Long insertCustomerUser(SysCustomerUser sysCustomerUser);

    void deleteConfigById(Long configIds);

    Long selectCustomerIdByUserId(Long userId);
}
