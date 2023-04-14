package com.qzt.module.app.service;


import com.qzt.module.app.domain.AppCustomer;

import java.util.List;

/**
 * 企业信息Service接口
 * 
 * @author ruoyi
 * @date 2023-04-06
 */
public interface IAppCustomerService 
{
    /**
     * 查询企业信息
     * 
     * @param id 企业信息主键
     * @return 企业信息
     */
    public AppCustomer selectAppCustomerById(Long id);

    /**
     * 查询企业信息列表
     * 
     * @param appCustomer 企业信息
     * @return 企业信息集合
     */
    public List<AppCustomer> selectAppCustomerList(AppCustomer appCustomer);

    /**
     * 新增企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
    public int insertAppCustomer(AppCustomer appCustomer);

    /**
     * 修改企业信息
     * 
     * @param appCustomer 企业信息
     * @return 结果
     */
    public int updateAppCustomer(AppCustomer appCustomer);

    /**
     * 批量删除企业信息
     * 
     * @param ids 需要删除的企业信息主键集合
     * @return 结果
     */
    public int deleteAppCustomerByIds(String ids);

    /**
     * 删除企业信息信息
     * 
     * @param id 企业信息主键
     * @return 结果
     */
    public int deleteAppCustomerById(Long id);
    //查询注册企业填写数据是否存在
    public List<AppCustomer> selectCustomerExists(AppCustomer app);

    //审核企业
    public void updateLogonVerify(String eid,Integer logonStatus,String description);

    //获取用户绑定的企业信息
    public AppCustomer getBindingInfo(Long userId);

}
