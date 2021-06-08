package com.twc.guanlang.param.permit;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PermitSelectOneParam {

    @ApiModelProperty(value = "菜单id")
    private String permitId;


}
