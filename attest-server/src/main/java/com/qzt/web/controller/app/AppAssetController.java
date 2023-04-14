package com.qzt.web.controller.app;


import com.qzt.common.annotation.Log;
import com.qzt.common.constant.AppErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.core.text.Convert;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.app.domain.AppAsset;
import com.qzt.module.app.service.IAppAssetService;
import com.qzt.module.app.domain.AppCustomer;
import com.qzt.module.app.service.IAppCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * @date 2023-04-12
 */
@Api(tags = "资产管理")
@RestController
@RequestMapping("/app/asset")
public class AppAssetController extends BaseController
{

    @Autowired
    private IAppAssetService appAssetService;
    @Autowired
    private IAppCustomerService customerService;


    /**
     * 查询列表
     */
    @ApiOperation(value = "资产列表")
    @PreAuthorize("@ss.hasPermi('system:asset:list')")
    @PostMapping("/list")
    public TableDataInfo<AppAsset> list(AppAsset appAsset)
    {
        LoginUser loginUser = getLoginUser();
        //获取当前登录企业用户的资产信息
        AppCustomer customer = customerService.getBindingInfo(loginUser.getUserId());
        appAsset.setCustomerId(customer.getId());
        startPage();
        List<AppAsset> list = appAssetService.selectAppAssetList(appAsset);
        for(AppAsset asset : list){
            //sm4解密
            asset.setIp(ToolUtil.decryptSm4(asset.getIp()));
        }
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:asset:export')")
    @Log(title = "导出资产", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(AppAsset appAsset)
    {
        List<AppAsset> list = appAssetService.selectAppAssetList(appAsset);
        ExcelUtil<AppAsset> util = new ExcelUtil<AppAsset>(AppAsset.class);
        return util.exportExcel(list, "资产数据");
    }*/


    /**
     * 新增保存【请填写功能名称】
     */
    @ApiOperation(value = "新增资产")
    @PreAuthorize("@ss.hasPermi('system:asset:add')")
    @Log(title = "新增资产", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> addSave(@RequestBody @Valid AppAsset appAsset)
    {
        appAssetService.insertAppAsset(appAsset);
        return R.ok();
    }

    /**
     * 获取资产信息
     */
    @ApiOperation(value = "资产详细信息")
    @PreAuthorize("@ss.hasPermi('system:asset:edit')")
    @GetMapping("/edit/{eid}")
    public R<AppAsset> edit(@PathVariable("eid") String eid)
    {
        Long id = null;
        try {
            id = Long.parseLong(ToolUtil.decryptSm4(eid));
        }catch (Exception e){
            throw new ServiceException(AppErrorCodeConstants.ASSET_DATA_NOT_EXISTS);
        }
        AppAsset appAsset = appAssetService.selectAppAssetById(id);
        //sm4解密
        appAsset.setIp(ToolUtil.decryptSm4(appAsset.getIp()));
        return R.ok(appAsset);
    }

    /**
     * 修改资产
     */
    @ApiOperation(value = "修改资产")
    @PreAuthorize("@ss.hasPermi('system:asset:edit')")
    @Log(title = "修改资产", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<String> editSave(@RequestBody @Valid AppAsset appAsset)
    {
        appAssetService.updateAppAsset(appAsset);
        return R.ok();
    }

    /**
     * 删除资产
     */
    @ApiOperation(value = "删除资产")
    @PreAuthorize("@ss.hasPermi('system:asset:remove')")
    @Log(title = "删除资产", businessType = BusinessType.DELETE)
    @DeleteMapping( "/remove/{eids}")
    public R<String> remove(@PathVariable String[] eids)
    {
        List<Long> ids = new ArrayList<>();
        for (String eid : eids){
            Long id = Convert.toLong(ToolUtil.decryptSm4(eid));
            ids.add(id);
        }
        appAssetService.deleteAppAssetByIds(ids);
        return R.ok();
    }
}
