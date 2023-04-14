package com.qzt.module.system.domain;

import com.qzt.common.core.domain.BaseEntity;
import com.qzt.common.email.EmailSceneEnum;
import lombok.*;

/**
 * 邮寄发送日志
 * idx_email 索引：基于 {@link #email} 字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysEmailSendLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 验证码
     */
    private String content;
    /**
     * 发送场景
     * 枚举 {@link EmailSceneEnum}
     */
    private Integer scene;
    /**
     * 创建 IP
     */
    private String createIp;
    /**
     * 今日发送的第几条
     */
    private Integer todayIndex;

}
