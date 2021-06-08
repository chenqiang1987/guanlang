package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 机器人和感应器
 */
@Data
@Entity
@Table(name = "t_module2Machine")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_module2Machine")

public class Module2Machine  extends BaseEntity {



    @Column(name = "machineId")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
    Long machineId;

    @Column(name = "moduleId")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleId", type = MySqlTypeConstant.INT)
    Integer moduleId;


    @Column(name = "moduleCode")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleCode", type = MySqlTypeConstant.VARCHAR)
    String moduleCode;



    @Column(name = "max")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "max", type = MySqlTypeConstant.DOUBLE)
    Double max;


    @Column(name = "min")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "min", type = MySqlTypeConstant.DOUBLE)
    Double min;

    /**
     * 是否显示
     *  0/1
     */
    @Column(name = "isShow")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "isShow", type = MySqlTypeConstant.INT)
    Integer isShow;

}
