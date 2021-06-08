package com.twc.guanlang.param.role;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RoleModifyOneParam {

    @ApiModelProperty(value = "角色编号")
    private int id;

//    @ApiModelProperty(value = "角色编码")
//    private String name;

    @ApiModelProperty(value = "角色名称")
    private String nickName;
}
