package com.twc.guanlang.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ResetPasswordParam {


    @ApiModelProperty(value = "用户id")
    private String userId;

}
