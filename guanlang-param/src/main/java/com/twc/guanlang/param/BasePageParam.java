package com.twc.guanlang.param;


import com.twc.guanlang.common.annotation.TwcParamCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 分页参数基类
 */
@Data
public class BasePageParam {

    @TwcParamCheck
    @ApiModelProperty(name = "pageNum", value = "页码")
    private int pageNum = 1;

    @TwcParamCheck
    @ApiModelProperty(name = "pageSize", value = "每页数量")
    private int pageSize = 10;

}
