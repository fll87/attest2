package com.qzt.module.certification.mapper;

import java.util.List;
import com.qzt.module.certification.domain.BpmAttest;

/**
 * 认证流程Mapper接口
 *
 * @author kf
 * @date 2023-04-12
 */
public interface BpmAttestMapper
{
    /**
     * 查询认证流程
     *
     * @param id 认证流程主键
     * @return 认证流程
     */
    public BpmAttest selectById(Long id);

    /**
     * 查询认证流程列表
     *
     * @param bpmAttest 认证流程
     * @return 认证流程集合
     */
    public List<BpmAttest> selectList(BpmAttest bpmAttest);

    /**
     * 新增认证流程
     *
     * @param bpmAttest 认证流程
     * @return 结果
     */
    public int save(BpmAttest bpmAttest);

    /**
     * 修改认证流程
     *
     * @param bpmAttest 认证流程
     * @return 结果
     */
    public int update(BpmAttest bpmAttest);

    /**
     * 删除认证流程
     *
     * @param id 认证流程主键
     * @return 结果
     */
    public int deleteById(Long id);

    /**
     * 批量删除认证流程
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids);
}
