package com.twc.guanlang.param.permit;


import com.twc.guanlang.common.annotation.TwcParamCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PermitAddOneParam {

    @ApiModelProperty(value = "上级菜单id")
    @TwcParamCheck(required = true)
    private int parentId;

    @ApiModelProperty(value = "菜单名称")
    @TwcParamCheck(required = true)
    private String name;

    @ApiModelProperty(value = "菜单路径")
    private String path;


}
