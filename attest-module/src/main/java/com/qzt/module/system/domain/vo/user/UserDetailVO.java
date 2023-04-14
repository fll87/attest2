package com.qzt.module.system.domain.vo.user;

import com.qzt.common.core.domain.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@ApiModel("用户详细信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailVO {

    @ApiModelProperty("用户基本信息")
    private UserInfoVO user;

    @ApiModelProperty(value = "角色组")
    private List<SysRole> roles;

    @ApiModelProperty(value = "用户角色ID")
    private List<Long> roleIds;

    @ApiModelProperty(value = "角色集合")
    private List<String> roleKeys;

    @ApiModelProperty(value = "权限集合")
    private List<String> permissions;

}
