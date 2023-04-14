package com.qzt.module.system.service;

import java.io.IOException;
import java.util.List;
import com.qzt.module.system.domain.SysBpmTemplate;

/**
 * 流程模板Service接口
 *
 * @author kf
 * @date 2023-04-11
 */
public interface ISysBpmTemplateService
{
    /**
     * 查询流程模板
     *
     * @param id 流程模板主键
     * @return 流程模板
     */
    public SysBpmTemplate selectById(Long id);

    /**
     * 查询流程模板列表
     *
     * @param sysBpmTemplate 流程模板
     * @return 流程模板集合
     */
    public List<SysBpmTemplate> selectList(SysBpmTemplate sysBpmTemplate);

    /**
     * 新增流程模板
     *
     * @param sysBpmTemplate 流程模板
     * @return 结果
     */
    public int save(SysBpmTemplate sysBpmTemplate) throws IOException;

    /**
     * 修改流程模板
     *
     * @param sysBpmTemplate 流程模板
     * @return 结果
     */
    public int update(SysBpmTemplate sysBpmTemplate) throws IOException;

    /**
     * 删除流程模板信息
     *
     * @param id 流程模板主键
     * @return 结果
     */
    public int deleteById(Long id);
}
