package com.qzt.web.controller.system;

import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.framework.web.service.SysRegisterService;
import com.qzt.module.system.domain.vo.user.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 注册验证
 *
 * @author
 */
@Api(tags = "企业注册")
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    //企业注册
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody @ModelAttribute @Valid RegisterVO reqVO)
    {
        registerService.register(reqVO);
        return R.ok();
    }
}
