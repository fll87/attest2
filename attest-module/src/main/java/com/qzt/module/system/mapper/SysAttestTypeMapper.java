package com.qzt.module.system.mapper;

import java.util.List;
import com.qzt.module.system.domain.SysAttestType;

/**
 * 认证服务类型Mapper接口
 *
 * @author kf
 * @date 2023-04-11
 */
public interface SysAttestTypeMapper
{
    /**
     * 查询认证服务类型
     *
     * @param id 认证服务类型主键
     * @return 认证服务类型
     */
    public SysAttestType selectById(Long id);

    /**
     * 查询认证服务类型列表
     *
     * @param sysAttestType 认证服务类型
     * @return 认证服务类型集合
     */
    public List<SysAttestType> selectList(SysAttestType sysAttestType);

    /**
     * 新增认证服务类型
     *
     * @param sysAttestType 认证服务类型
     * @return 结果
     */
    public int save(SysAttestType sysAttestType);

    /**
     * 修改认证服务类型
     *
     * @param sysAttestType 认证服务类型
     * @return 结果
     */
    public int update(SysAttestType sysAttestType);

    /**
     * 删除认证服务类型
     *
     * @param id 认证服务类型主键
     * @return 结果
     */
    public int deleteById(Long id);

    /**
     * 批量删除认证服务类型
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids);

    /**
     * 按父ID删除认证服务类型
     *
     * @param pids 认证服务类型父ID
     * @return 结果
     */
    void deleteByPids(List<Long> pids);
}
