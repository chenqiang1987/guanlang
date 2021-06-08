package com.twc.guanlang.entity.maintain;


import lombok.Data;


/**
 * 故障类型  代号  名称
 */
@Data
//@Entity
//@Table(name = "t_repair_type")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_repair_type")
public class RepairType {


    //
//    @Column(name = "code")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "code", type = MySqlTypeConstant.VARCHAR)
    private String code;

    //
//    @Column(name = "name")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR)
    private String name;


}
