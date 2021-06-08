package com.twc.guanlang.entity.unique;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserUnique extends BaseUnique {


    @ApiModelProperty(name = "userName", value = "用户名")
    private String telPhone;

    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;
}
