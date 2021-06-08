package com.twc.guanlang.param.user;

import com.twc.guanlang.param.BaseParam;
import com.twc.guanlang.common.annotation.TwcParamCheck;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel
public class LoginByUserNameParam implements BaseParam {

    @TwcParamCheck(required = true, length_min = 5, length_max = 20)
    private String userName;

    @TwcParamCheck(required = true, length_min = 6, length_max = 15)
    private String password;

}