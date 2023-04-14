package com.qzt.module.certification.service;

import java.io.IOException;
import java.util.List;
import com.qzt.module.certification.domain.BpmAttest;
import com.qzt.module.certification.domain.vo.BpmAttestAddVO;

/**
 * 认证流程Service接口
 *
 * @author kf
 * @date 2023-04-12
 */
public interface IBpmAttestService
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
     * @param vo 认证申请对象
     * @return 结果
     */
    public int save(BpmAttestAddVO vo);

    /**
     * 修改认证流程
     *
     * @param bpmAttest 认证流程
     * @return 结果
     */
    public int update(BpmAttest bpmAttest);

    /**
     * 批量删除认证流程
     *
     * @param ids 需要删除的认证流程主键集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids);

    /**
     * 删除认证流程信息
     *
     * @param id 认证流程主键
     * @return 结果
     */
    public int deleteById(Long id);
}
