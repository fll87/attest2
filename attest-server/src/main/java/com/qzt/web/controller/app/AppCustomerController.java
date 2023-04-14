package com.qzt.web.controller.app;

import com.qzt.common.annotation.Log;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.app.domain.AppCustomer;
import com.qzt.module.app.service.IAppCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by csc on 2023/4/11.
 */
@Api(tags = "企业管理")
@RestController
@RequestMapping("/app/customer")
public class AppCustomerController extends BaseController{

    @Autowired
    private IAppCustomerService appCustomerService;

    @ApiOperation(value = "企业列表")
    @PreAuthorize("@ss.hasPermi('app:customer:list')")
    @PostMapping("/customerList")
    public TableDataInfo<AppCustomer> customerList(AppCustomer customer)
    {
        startPage();
        List<AppCustomer> list = appCustomerService.selectAppCustomerList(customer);
        return getDataTable(list);
    }

    //注册审核
    @ApiOperation(value = "注册审核")
    @PreAuthorize("@ss.hasPermi('app:customer:audit')")
    @Log(title = "企业管理-注册审核", businessType = BusinessType.UPDATE)
    @PutMapping("/logonVerify")
    public R<String> logonVerify(@RequestParam @ApiParam(value = "密文id", required = true) String eid,
                                 @RequestParam @ApiParam(value = "注册审核状态1:通过；2:不通过", required = true) Integer logonStatus,
                                 @RequestParam @ApiParam(value = "审核不通过原因") String description)
    {
        appCustomerService.updateLogonVerify(eid,logonStatus,description);
        return R.ok();
    }

    //企业详细信息
    @ApiOperation(value = "企业信息")
    @PreAuthorize("@ss.hasPermi('app:customer:info')")
    @GetMapping("/getCustomerById/{eid}")
    public R<AppCustomer> getCustomerById(@PathVariable(value = "eid") String eid )
    {
        Long id = null;
        try {
            id = Long.parseLong(ToolUtil.decryptSm4(eid));
        }catch (Exception e){
            throw new ServiceException(SysErrorCodeConstants.CUSTOMER_DATA_NOT_EXISTS);
        }
        AppCustomer appCustomer = appCustomerService.selectAppCustomerById(id);
        return R.ok(appCustomer);
    }

    //企业编辑
    @ApiOperation(value = "编辑企业信息")
    @PreAuthorize("@ss.hasPermi('app:customer:update')")
    @PutMapping("/editCustomer")
    public R<String> editCustomer(@RequestBody @ModelAttribute @Valid AppCustomer app)
    {
        appCustomerService.updateAppCustomer(app);
        return R.ok();
    }



}
