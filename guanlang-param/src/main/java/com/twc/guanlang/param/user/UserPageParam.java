package com.twc.guanlang.param.user;

import com.twc.guanlang.param.BasePageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 机器人分页列表查询
 */
@Data

@ApiModel("userPageParam")
public class UserPageParam extends BasePageParam {


    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    @ApiModelProperty(name = "roleId", value = "角色id")
    private String roleId;

    @ApiModelProperty(name = "status", value = "用户状态")
    private String status;

    @ApiModelProperty(name = "telPhone", value = "手机号码")
    private String telPhone;

    @ApiModelProperty(name = "nickName", value = "用户姓名")
    private String nickName;

//    @ParamCheck(required = true, length_min = 6, length_max = 15)
//    private String confirmPassword;
//
//    @ParamCheck(required = true, length_min = 11, length_max = 11)
//    private String telPhone;
//
//    /**
//     * 用户新增时候的角色  [1,2,3]
//     */
//    @ParamCheck(required = true)
//    private int[] roleIds;


}