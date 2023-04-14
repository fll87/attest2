package com.qzt.module.system.service.impl;


import com.qzt.module.system.domain.SysCustomerUser;
import com.qzt.module.system.mapper.SysCustomerUserMapper;
import com.qzt.module.system.service.ISysCustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 参数配置 服务层实现
 *
 * @author
 */
@Service
public class SysCustomerUserServiceImpl implements ISysCustomerUserService
{

    @Autowired
    private SysCustomerUserMapper mapper;

    @Override
    public Long insertCustomerUser(SysCustomerUser sysCustomerUser) {
        return mapper.insertCustomerUser(sysCustomerUser);
    }

    @Override
    public void deleteConfigById(Long configIds) {
        mapper.deleteConfigById(configIds);
    }

    @Override
    public Long selectCustomerIdByUserId(Long userId) {
        return mapper.selectCustomerIdByUserId(userId);
    }
}
