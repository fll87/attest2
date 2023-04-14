package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.page.TableDataInfo;
import com.qzt.common.core.text.Convert;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.module.system.domain.SysBpmTemplate;
import com.qzt.module.system.service.ISysBpmTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "流程模板管理")
@RestController
@RequestMapping("/system/bmp")
public class SysBpmTemplateController extends BaseController {
    @Autowired
    private ISysBpmTemplateService sysBpmTemplateService;

    @ApiOperation(value = "流程模板列表")
    @PreAuthorize("@ss.hasPermi('system:bmp:list')")
    @GetMapping("/list")
    public TableDataInfo<SysBpmTemplate> list(SysBpmTemplate sysBpmTemplate) {
        startPage();
        List<SysBpmTemplate> list = sysBpmTemplateService.selectList(sysBpmTemplate);
        return getDataTable(list);
    }

    @ApiOperation(value = "根据ID获取流程模板详细信息")
    @PreAuthorize("@ss.hasPermi('system:bmp:query')")
    @GetMapping(value = "/{eid}")
    public R<SysBpmTemplate> getInfo(@PathVariable("eid") String eid) {
        Long id = Convert.toLong(ToolUtil.encryptSm4(eid));
        return R.ok(sysBpmTemplateService.selectById(id));
    }

    @ApiOperation(value = "新增流程模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "模板附件", required = true, dataType = "file", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "name", value = "模板名称", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('system:bmp:add')")
    @Log(title = "流程模板", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestParam(value = "file", required = true) MultipartFile file,
                          @RequestParam(value = "name", required = true) String name) throws Exception {
        if (file.isEmpty()) {
            throw new ServiceException(SysErrorCodeConstants.FILE_IS_EMPTY);
        }
        String path = FileUploadUtils.uploadByOldName(AttestConfig.getTemplatePath(), file, new String[]{"xml"});
        SysBpmTemplate sysBpmTemplate = SysBpmTemplate.builder().name(name).path(path).build();
        sysBpmTemplate.setCreator(getUsername());
        sysBpmTemplateService.save(sysBpmTemplate);
        return R.ok();
    }

    @ApiOperation(value = "修改流程模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "模板KEY", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "file", value = "模板附件", required = true, dataType = "file", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "name", value = "模板名称", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @PreAuthorize("@ss.hasPermi('system:bmp:edit')")
    @Log(title = "流程模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(
            @RequestParam(value = "key", required = true) String key,
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "name", required = true) String name) throws Exception {
        if (file.isEmpty()) {
            throw new ServiceException(SysErrorCodeConstants.FILE_IS_EMPTY);
        }
        String path = FileUploadUtils.uploadByOldName(AttestConfig.getTemplatePath(), file, new String[]{"xml"});
        SysBpmTemplate sysBpmTemplate = SysBpmTemplate.builder().templateKey(key).name(name).path(path).build();
        sysBpmTemplate.setUpdater(getUsername());
        sysBpmTemplateService.update(sysBpmTemplate);
        return R.ok();
    }

    @ApiOperation(value = "根据模板主键删除流程模板")
    @PreAuthorize("@ss.hasPermi('system:bmp:remove')")
    @Log(title = "流程模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eid}")
    public R<String> remove(@PathVariable String eid) {
        Long id = Convert.toLong(ToolUtil.encryptSm4(eid));
        sysBpmTemplateService.deleteById(id);
        return R.ok();
    }
}
