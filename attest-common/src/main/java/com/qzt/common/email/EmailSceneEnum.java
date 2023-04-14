package com.qzt.common.email;

import cn.hutool.core.util.ArrayUtil;
import com.qzt.common.core.domain.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户短信发送场景的枚举
 *
 * @author
 */
@Getter
@AllArgsConstructor
public enum EmailSceneEnum implements IntArrayValuable {

    MSG_CODE(1, "", "验证码"),
    MSG_SYS(2, "", "平台消息");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(EmailSceneEnum::getScene).toArray();

    /**
     * 验证场景的编号
     */
    private final Integer scene;
    /**
     * 模版编码
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

    public static EmailSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

}
