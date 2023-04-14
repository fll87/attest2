package com.qzt.module.app.service.impl;

import java.util.List;

import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.DateUtils;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.common.validation.ValidationUtils;
import com.qzt.module.app.domain.AppAsset;
import com.qzt.module.app.mapper.AppAssetMapper;
import com.qzt.module.app.service.IAppAssetService;

import com.qzt.module.app.domain.AppCustomer;
import com.qzt.module.app.service.IAppCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-04-12
 */
@Service
public class AppAssetServiceImpl implements IAppAssetService
{
    @Autowired
    private AppAssetMapper appAssetMapper;
    @Autowired
    private IAppCustomerService customerService;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public AppAsset selectAppAssetById(Long id)
    {
        return appAssetMapper.selectAppAssetById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param appAsset 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AppAsset> selectAppAssetList(AppAsset appAsset)
    {
        return appAssetMapper.selectAppAssetList(appAsset);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param appAsset 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAppAsset(AppAsset appAsset)
    {
        checkData(appAsset);
        //获得登录人的企业id
        AppCustomer customer = customerService.getBindingInfo(SecurityUtils.getUserId());
        //当前操作人的企业id
        appAsset.setCustomerId(customer.getId());
        appAsset.setCreateTime(DateUtils.getNowDate());
        return appAssetMapper.insertAppAsset(appAsset);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param appAsset 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAppAsset(AppAsset appAsset)
    {
        checkData(appAsset);
        appAsset.setUpdateTime(DateUtils.getNowDate());
        return appAssetMapper.updateAppAsset(appAsset);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppAssetByIds(List<Long> ids)
    {
        Long[] arr = new Long[ids.size()];
        for(int i = 0,j = ids.size(); i < j ; i++){
            arr[i] = ids.get(i);
        }
        return appAssetMapper.deleteAppAssetByIds(arr);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppAssetById(Long id)
    {
        return appAssetMapper.deleteAppAssetById(id);
    }

    //5.	当资产类型是主机时可选资产类别，非必填
    private void checkData(AppAsset appAsset){
        //todo 假设 主机 标识是 1
        if(appAsset.getType().intValue() != 1 && appAsset.getSort() == null){
            throw new ServiceException(AppErrorCodeConstants.ASSET_SORT_IS_NULL);
        }
        //2.	Ip/url合法性检查
        if(!ValidationUtils.isURL(appAsset.getIp()) && !ValidationUtils.isIP(appAsset.getIp()) ){
            throw new ServiceException(AppErrorCodeConstants.IP_OR_URL_CHECK);
        }
        // ip/url sm4 加密
        appAsset.setIp(ToolUtil.encryptSm4(appAsset.getIp()));

    }

}
