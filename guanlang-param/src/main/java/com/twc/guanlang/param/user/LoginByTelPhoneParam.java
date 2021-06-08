package com.twc.guanlang.param.user;

import com.twc.guanlang.common.annotation.TwcParamCheck;
import lombok.Data;


@Data
public class LoginByTelPhoneParam {

    @TwcParamCheck(required = true, length_min = 11, length_max = 11)
    private String telPhone;

    @TwcParamCheck(required = true, length_min = 6, length_max = 6)
    private String code;

}