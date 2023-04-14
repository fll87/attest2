package com.qzt.web.controller.system;

import com.qzt.common.annotation.Log;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.controller.BaseController;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.domain.model.LoginUser;
import com.qzt.common.enums.BusinessType;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.bean.BeanUtils;
import com.qzt.common.utils.file.FileUploadUtils;
import com.qzt.common.utils.file.MimeTypeUtils;
import com.qzt.framework.web.service.SysLoginService;
import com.qzt.framework.web.service.TokenService;
import com.qzt.module.system.domain.vo.user.UpdateUserVO;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * 用户个人中心 业务处理
 *
 * @author
 */
@Api(tags = "用户个人中心")
@RestController
@RequestMapping("user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService loginService;

    /**
     * 修改用户
     */
    @Log(title = "个人中心-修改个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改个人信息")
    public R<String> updateProfile(@RequestBody UpdateUserVO reqVO) {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        //修改手机
        if (!sysUser.getMobile().equals(reqVO.getMobile())) {
            //手机验证码校验
            loginService.validateCode(sysUser.getUsername(), reqVO.getMobileCode(), reqVO.getMobileUuid(), CacheConstants.SMS_CODE_KEY, false);
            //手机号重复校验
            if (Objects.nonNull(userService.selectUserExits(reqVO.getMobile()))) {
                throw new ServiceException(SysErrorCodeConstants.MOBILE_IS_USED);
            }
        }
        //修改邮箱
        if (!sysUser.getEmail().equals(reqVO.getEmail())) {
            //邮箱验证码校验
            loginService.validateCode(sysUser.getUsername(), reqVO.getEmailCode(), reqVO.getEmailUuid(), CacheConstants.EMAIL_CODE_KEY, false);
            //邮箱重复校验
            if (Objects.nonNull(userService.selectUserExits(reqVO.getEmail()))) {
                throw new ServiceException(SysErrorCodeConstants.EMAIL_IS_USED);
            }
        }
        //更新入库
        SysUser user = new SysUser();
        BeanUtils.copyBeanProp(user, reqVO);
        user.setUserId(loginUser.getUserId());
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            sysUser.setNickname(reqVO.getNickname());
            sysUser.setMobile(reqVO.getMobile());
            sysUser.setEmail(reqVO.getEmail());
            sysUser.setSex(reqVO.getSex());
            tokenService.setLoginUser(loginUser);
        }
        return R.ok();
    }

    /**
     * 修改密码
     */
    @Log(title = "个人中心-修改密码", businessType = BusinessType.UPDATE)
    @PostMapping("/update-pwd")
    @ApiOperation(value = "修改密码")
    public R<String> updatePwd(@RequestParam @Valid @NotEmpty String oldPassword,
                                @RequestParam @Valid @NotEmpty String newPassword) {
        LoginUser loginUser = getLoginUser();
        if (userService.resetUserPwd(loginUser.getUserId(), oldPassword, newPassword) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
        }
        return R.ok();
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    @ApiOperation(value = "头像上传")
    public R<String> avatar(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(AttestConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return R.ok(avatar);
            }
        }
        return R.fail(SysErrorCodeConstants.FILE_UPLOAD_FAIL);
    }
}
