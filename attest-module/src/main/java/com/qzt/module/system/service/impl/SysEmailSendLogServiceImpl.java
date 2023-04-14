package com.qzt.module.system.service.impl;

import java.util.List;
import com.qzt.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qzt.module.system.mapper.SysEmailSendLogMapper;
import com.qzt.module.system.domain.SysEmailSendLog;
import com.qzt.module.system.service.ISysEmailSendLogService;

/**
 * 邮箱发送记录Service业务层处理
 *
 * @author kf
 * @date 2023-04-07
 */
@Service
public class SysEmailSendLogServiceImpl implements ISysEmailSendLogService
{
    @Autowired
    private SysEmailSendLogMapper sysEmailSendLogMapper;

    /**
     * 查询邮箱发送记录
     *
     * @param id 邮箱发送记录主键
     * @return 邮箱发送记录
     */
    @Override
    public SysEmailSendLog selectById(Long id)
    {
        return sysEmailSendLogMapper.selectById(id);
    }

    /**
     * 查询邮箱发送记录列表
     *
     * @param sysEmailSendLog 邮箱发送记录
     * @return 邮箱发送记录
     */
    @Override
    public List<SysEmailSendLog> selectList(SysEmailSendLog sysEmailSendLog)
    {
        return sysEmailSendLogMapper.selectList(sysEmailSendLog);
    }

    /**
     * 新增邮箱发送记录
     *
     * @param sysEmailSendLog 邮箱发送记录
     * @return 结果
     */
    @Override
    public int save(SysEmailSendLog sysEmailSendLog)
    {
        sysEmailSendLog.setCreateTime(DateUtils.getNowDate());
        return sysEmailSendLogMapper.save(sysEmailSendLog);
    }

    /**
     * 按邮箱查询最后一条日志记录
     *
     * @param email 邮箱
     * @return 结果
     */
    @Override
    public SysEmailSendLog selectLastByEmail(String email) {
        return sysEmailSendLogMapper.selectLastByEmail(email);
    }

}
