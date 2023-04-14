package com.qzt.module.system.domain;

import com.qzt.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * Created by csc on 2023/4/10.
 */
@Data
public class SysCustomerUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long customerId;

    private Long userId;
}
