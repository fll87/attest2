package com.qzt.module.system.service;

import java.util.List;

import com.qzt.common.core.domain.TreeSelect;
import com.qzt.module.system.domain.SysAttestType;
import com.qzt.module.system.domain.vo.user.SysAttestTypeTreeVO;

/**
 * 认证服务类型Service接口
 *
 * @author kf
 * @date 2023-04-11
 */
public interface ISysAttestTypeService
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
     * 批量删除认证服务类型
     *
     * @param ids 需要删除的认证服务类型主键集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids);

    /**
     * 删除认证服务类型信息
     *
     * @param id 认证服务类型主键
     * @return 结果
     */
    public int deleteById(Long id);

    /**
     * 构建树结构数据
     * @param list
     * @return
     */
    List<SysAttestTypeTreeVO> buildTree(List<SysAttestType> list);
}
