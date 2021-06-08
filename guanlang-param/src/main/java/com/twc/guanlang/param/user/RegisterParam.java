package com.twc.guanlang.param.user;

import com.twc.guanlang.common.annotation.TwcParamCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RegisterParam {

    @ApiModelProperty(value = "用户id,为空表示用户新增,不为空表示用户修改")
    private String id;

    @TwcParamCheck(required = true, length_min = 5, length_max = 20)
    private String userName;

    @TwcParamCheck(required = true, length_min = 6, length_max = 15)
    private String password;

    @TwcParamCheck(required = true, length_min = 6, length_max = 15)
    @ApiModelProperty(value = "注册密码二次确认")
    private String confirmPassword;

    @TwcParamCheck(required = true, length_min = 11, length_max = 11)
    @ApiModelProperty(value = "手机号码")
    private String telPhone;


    @ApiModelProperty(value = "用户姓名")
    private String nickName;

    @ApiModelProperty(value = "角色id")
    @TwcParamCheck(required = true)
    private int[] roleIds;


}