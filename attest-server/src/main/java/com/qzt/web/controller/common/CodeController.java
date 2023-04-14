package com.qzt.web.controller.common;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import com.qzt.common.annotation.Anonymous;
import com.qzt.common.config.AttestConfig;
import com.qzt.common.constant.CacheConstants;
import com.qzt.common.constant.Constants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.R;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.core.redis.RedisCache;
import com.qzt.common.core.text.StrFormatter;
import com.qzt.common.email.EmailSceneEnum;
import com.qzt.common.email.MailManager;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.sms.SMSManager;
import com.qzt.common.sms.SmsSceneEnum;
import com.qzt.common.utils.MessageUtils;
import com.qzt.common.utils.ip.IpUtils;
import com.qzt.common.utils.sign.Base64;
import com.qzt.common.utils.uuid.IdUtils;
import com.qzt.module.system.domain.SysEmailSendLog;
import com.qzt.module.system.domain.SysSmsSendLog;
import com.qzt.module.system.domain.vo.code.CodeResVO;
import com.qzt.module.system.domain.vo.user.SmsCodeVO;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysEmailSendLogService;
import com.qzt.module.system.service.ISysSmsSendLogService;
import com.qzt.module.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.util.RandomUtil.randomInt;

@Api(tags = "验证码")
@RestController
@Validated
@RequestMapping("code")
public class CodeController {
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysSmsSendLogService smsSendLogService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysEmailSendLogService emailSendLogService;
    @Autowired
    private MailManager mailManager;

    /**
     * 获取验证码
     */
    @PostMapping("/captcha")
    @ApiOperation(value = "获取图形验证码")
    @Anonymous
    public R<CodeResVO> getCode() {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (!captchaEnabled) {
            return R.fail(SysErrorCodeConstants.CAPTCHA_IS_ENABLED);
        }
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;
        // 生成验证码
        String captchaType = AttestConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return R.fail(MessageUtils.exception(SysErrorCodeConstants.CAPTCHA_GEN_ERROR, e.getMessage()));
        }

        CodeResVO data = CodeResVO.builder().uuid(uuid).img(Base64.encode(os.toByteArray())).build();
        return R.ok(data);
    }

    /**
     * 发送手机验证码
     */
    @PostMapping("/sms")
    @ApiOperation(value = "发送手机验证码",notes = "返回uuid用于验证手机验证码")
    @Anonymous
    public R<String> sendSmsCode(@RequestBody @Valid SmsCodeVO reqVO) {
        boolean smsEnabled = configService.selectSmsEnabled();
        if (!smsEnabled) {
            return R.fail(SysErrorCodeConstants.SMS_IS_ENABLED);
        }
        SmsSceneEnum sceneEnum = SmsSceneEnum.getCodeByScene(reqVO.getScene());
        if (Objects.isNull(sceneEnum)) {
            return R.fail(MessageUtils.exception(SysErrorCodeConstants.SMS_SCENE_NOT_FOUND, reqVO.getScene()));
        }
        // 校验是否可以发送验证码
        SysSmsSendLog lastSmsLog = validSendSms(reqVO.getMobile(),sceneEnum);
        boolean hasLog = lastSmsLog != null && DateUtil.isSameDay(lastSmsLog.getCreateTime(), new Date());
        // 生成验证码
        String code = String.valueOf(randomInt(1000, 9999));
        String template = configService.selectSmsTemplateByCode(sceneEnum.getTemplateCode());
        if (StrUtil.isEmpty(template)) {
            throw new ServiceException(SysErrorCodeConstants.SMS_SEND_TEMPLATE_IS_EMPTY);
        }
        String content = StrFormatter.format(template,code);

        // 发送短信
        SMSManager.sendSMS(reqVO.getMobile(),content);

        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.SMS_CODE_KEY + uuid;
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        //记录短信发送日志
        SysSmsSendLog smsRecord = SysSmsSendLog.builder().mobile(reqVO.getMobile()).content(content).scene(reqVO.getScene())
                .todayIndex( hasLog ? lastSmsLog.getTodayIndex() + 1 : 1)
                .createIp(IpUtils.getIpAddr()).build();
        smsSendLogService.save(smsRecord);

        return R.ok(uuid);
    }

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email")
    @ApiOperation(value = "发送邮箱验证码",notes = "返回uuid用于验证邮箱验证码")
    @ApiParam(value = "邮箱", name = "email",required = true)
    @Anonymous
    public R<String> sendEmailCode(@RequestParam @Valid @Email @NotEmpty(message = "邮箱不能为空") String email) {
        boolean emailEnabled = configService.selectEmailEnabled();
        if (!emailEnabled) {
            return R.fail(SysErrorCodeConstants.EMAIL_IS_ENABLED);
        }
        // 校验是否可以发送验证码
        SysEmailSendLog lastEmailLog = validSendEmail(email);
        boolean hasLog = lastEmailLog != null && DateUtil.isSameDay(lastEmailLog.getCreateTime(), new Date());
        // 生成验证码
        String code = String.valueOf(randomInt(1000, 9999));
        StringBuilder content = new StringBuilder();
        content.append("<p>您好！</p><br/>");
        content.append("<div style='padding-left:40px;'>验证码：");
        content.append(code);
        content.append("，有效期为五分钟，请尽快验证。</div><br/>");
        content.append("<div style='padding-left:40px;'>以上内容由安全认证模块自动发送，请勿回复！！<br/></div>");

        // 发送邮件
        mailManager.sendComplexMail("验证码",content.toString(),new String[]{email});

        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.EMAIL_CODE_KEY + uuid;
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        //记录邮箱发送日志
        SysEmailSendLog emailRecord = SysEmailSendLog.builder().email(email).content(content.toString())
                .scene(EmailSceneEnum.MSG_CODE.getScene())
                .todayIndex( hasLog ? lastEmailLog.getTodayIndex() + 1 : 1)
                .createIp(IpUtils.getIpAddr()).build();
        emailSendLogService.save(emailRecord);

        return R.ok(uuid);
    }

    private SysSmsSendLog validSendSms(String mobile, SmsSceneEnum sceneEnum) {
        //验证手机号
        SysUser sysUser = sysUserService.selectUserExits(mobile);
        if(SmsSceneEnum.VERIFICATION_CODE_REGISTER.equals(sceneEnum) || SmsSceneEnum.VERIFICATION_CODE_UPDATE_MOBILE.equals(sceneEnum)){
            if(Objects.nonNull(sysUser)){
                throw new ServiceException(SysErrorCodeConstants.MOBILE_IS_USED);
            }
        }else{
            if(Objects.isNull(sysUser)){
                throw new ServiceException(SysErrorCodeConstants.MOBILE_NOT_EXISTS);
            }
        }

        SysSmsSendLog lastSmsLog = smsSendLogService.selectLastByMobile(mobile);
        if (lastSmsLog != null) {
            // 发送过于频繁
            if (DateUtil.between(lastSmsLog.getCreateTime(), DateUtil.date(), DateUnit.MINUTE) < Constants.CAPTCHA_EXPIRATION) {
                throw new ServiceException(SysErrorCodeConstants.SMS_CODE_SEND_TOO_FAST);
            }
            // 超过当天发送的上限
            if (DateUtil.isSameDay(lastSmsLog.getCreateTime(), new Date()) && lastSmsLog.getTodayIndex() >= 20) {
                throw new ServiceException(SysErrorCodeConstants.SMS_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
            // TODO 每个 IP 每天可发送数量
            // TODO 每个 IP 每小时可发送数量
        }
        return lastSmsLog;
    }

    private SysEmailSendLog validSendEmail(String email){
        //验证邮箱
        SysUser sysUser = sysUserService.selectUserExits(email);
        if(Objects.nonNull(sysUser)){
            throw new ServiceException(SysErrorCodeConstants.EMAIL_IS_USED);
        }

        SysEmailSendLog lastLog = emailSendLogService.selectLastByEmail(email);
        if (lastLog != null) {
            // 发送过于频繁
            if (DateUtil.between(lastLog.getCreateTime(), DateUtil.date(), DateUnit.MINUTE) < Constants.CAPTCHA_EXPIRATION) {
                throw new ServiceException(SysErrorCodeConstants.EMAIL_CODE_SEND_TOO_FAST);
            }
            // 超过当天发送的上限
            if (DateUtil.isSameDay(lastLog.getCreateTime(), new Date()) && lastLog.getTodayIndex() >= 20) {
                throw new ServiceException(SysErrorCodeConstants.EMAIL_CODE_EXCEED_SEND_MAXIMUM_QUANTITY_PER_DAY);
            }
        }
        return lastLog;
    }
}
