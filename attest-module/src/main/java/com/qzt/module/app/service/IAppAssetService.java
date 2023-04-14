package com.qzt.module.app.service;


import com.qzt.module.app.domain.AppAsset;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2023-04-12
 */
public interface IAppAssetService 
{
    /**
     * 查询
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AppAsset selectAppAssetById(Long id);

    /**
     * 查询
     * 
     * @param appAsset
     * @return 集合
     */
    public List<AppAsset> selectAppAssetList(AppAsset appAsset);

    /**
     * 新增
     * 
     * @param appAsset
     * @return 结果
     */
    public int insertAppAsset(AppAsset appAsset);

    /**
     * 修改
     * 
     * @param appAsset
     * @return 结果
     */
    public int updateAppAsset(AppAsset appAsset);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的主键集合
     * @return 结果
     */
    public int deleteAppAssetByIds(List<Long> ids);

    /**
     * 删除
     * 
     * @param id 主键
     * @return 结果
     */
    public int deleteAppAssetById(Long id);
}
