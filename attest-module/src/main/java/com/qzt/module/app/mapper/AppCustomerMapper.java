package com.qzt.module.app.mapper;


import com.qzt.module.app.domain.AppCustomer;

import java.util.List;

/**
 * 企业信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-04-06
 */
public interface AppCustomerMapper 
{
    /**
     * 查询企业信息
     * 
     * @param id 企业信息主键
     * @return 企业信息
     */
     AppCustomer selectAppCustomerById(Long id);

    /**
     * 查询企业信息列表
     * 
     * @param appCustomer 企业信息
     * @return 企业信息集合
     */
     List<AppCustomer> selectAppCustomerList(AppCustomer appCustomer);

    /**
     * 新增企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
     int insertAppCustomer(AppCustomer appCustomer);

    /**
     * 修改企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
     int updateAppCustomer(AppCustomer appCustomer);

    /**
     * 删除企业信息
     * 
     * @param id 企业信息主键
     * @return 结果
     */
     int deleteAppCustomerById(Long id);

    /**
     * 批量删除企业信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteAppCustomerByIds(String[] ids);
    //查询企业是否存在
    List<AppCustomer> selectCustomerExists(AppCustomer app);
    //获取用户绑定的企业信息
    AppCustomer getBindingInfo(Long userId);

}
