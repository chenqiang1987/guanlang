package com.twc.guanlang.param;

import com.twc.guanlang.common.annotation.TwcParamCheck;
import lombok.Data;


@Data
public class PermitParam {

    /**
     * 上级菜单id
     */
    @TwcParamCheck(required = true)
    private int parentId;

}