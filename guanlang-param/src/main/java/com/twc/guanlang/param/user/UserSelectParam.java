package com.twc.guanlang.param.user;

import com.twc.guanlang.common.annotation.TwcParamCheck;
import lombok.Data;


@Data
public class UserSelectParam {

    @TwcParamCheck(length_min = 5, length_max = 20)
    private String userName;


    @TwcParamCheck(length_min = 11, length_max = 11)
    private String telPhone;

//    /**
//     * 用户新增时候的角色  [1,2,3]
//     */
//    @ParamCheck(required = true)
//    private int[] roleIds;


}