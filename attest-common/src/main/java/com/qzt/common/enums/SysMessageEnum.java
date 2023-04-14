package com.qzt.common.enums;

import cn.hutool.core.util.ArrayUtil;
import com.qzt.common.core.domain.IntArrayValuable;
import com.qzt.common.sms.SmsSceneEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by csc on 2023/4/13.
 * 消息订阅-消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum SysMessageEnum implements IntArrayValuable {
    //推送企业
    REGISTER_RESULT(1, "msg-register-result", "注册申请结果"),
    CUSTOMER_STATE(2, "msg-customer-state", "冻结/解冻"),
    VALIDITY_PROXIMITY_PROMPT(3,"msg-validity-proximity-prompt","有效性临近提示"),
    ATTEST_QUALIFICATION_SITUATION(4,"msg-attest-qualification-situation","认证资质的有效性情况"),
    ATTEST_RESULT(5,"msg-attest-result","认证申请结果"),
    ASSESS_REPORT_RESULT(6,"msg-assess-report-result","安全评估结果通报"),
    //推送 评审员，管理员
    APPOINTED_TASK(7,"msg-appointed-task","指派任务"),
    //推送管理员
    REGISTER_AUDIT(8,"msg-register-audit","注册审核"),
    //推送评审员
    ASSESS_RESULT_SUBMIT(9,"msg-assess-result-submit","检测评估结果上报"),
    //企业，评审员，管理员 。用户登陆发送，页面不显示勾选项
    VERIFICATION_CODE_LOGIN(10, "msg-verification-code", "验证码-登录"),
    OTHER(99,"other","其他");




    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(SysMessageEnum::getScene).toArray();
    /**
     * 编号
     */
    private final Integer scene;
    /**
     * 编码
     */
    private final String templateCode;
    /**
     * 描述
     */
    private final String description;
    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static SysMessageEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(messageEnum -> messageEnum.getScene().equals(scene),
                values());
    }

}
