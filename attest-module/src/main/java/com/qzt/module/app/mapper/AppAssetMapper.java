package com.qzt.module.app.mapper;


import com.qzt.module.app.domain.AppAsset;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-12
 */
public interface AppAssetMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AppAsset selectAppAssetById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param appAsset 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AppAsset> selectAppAssetList(AppAsset appAsset);

    /**
     * 新增【请填写功能名称】
     *
     * @param appAsset 【请填写功能名称】
     * @return 结果
     */
    public int insertAppAsset(AppAsset appAsset);

    /**
     * 修改【请填写功能名称】
     *
     * @param appAsset 【请填写功能名称】
     * @return 结果
     */
    public int updateAppAsset(AppAsset appAsset);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAppAssetById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppAssetByIds(Long[] ids);

    /**
     * 根据ID批量查询资产信息
     * @param assetIds
     * @return
     */
    List<AppAsset> selectAppAssetByIds(List<Long> assetIds);
}
