package com.qzt.module.system.service;

import java.util.List;
import com.qzt.module.system.domain.SysEmailSendLog;

/**
 * 邮箱发送记录Service接口
 *
 * @author kf
 * @date 2023-04-07
 */
public interface ISysEmailSendLogService
{
    /**
     * 查询邮箱发送记录
     *
     * @param id 邮箱发送记录主键
     * @return 邮箱发送记录
     */
    public SysEmailSendLog selectById(Long id);

    /**
     * 查询邮箱发送记录列表
     *
     * @param sysEmailSendLog 邮箱发送记录
     * @return 邮箱发送记录集合
     */
    public List<SysEmailSendLog> selectList(SysEmailSendLog sysEmailSendLog);

    /**
     * 新增邮箱发送记录
     *
     * @param sysEmailSendLog 邮箱发送记录
     * @return 结果
     */
    public int save(SysEmailSendLog sysEmailSendLog);

    /**
     * 按邮箱查询最后一条日志记录
     *
     * @param email 邮箱
     * @return 结果
     */
    SysEmailSendLog selectLastByEmail(String email);
}
