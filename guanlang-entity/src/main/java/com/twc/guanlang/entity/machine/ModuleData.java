package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 感应模块读数结果
 */
@Data
@Entity
@Table(name = "t_module_data")

//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_module_data")
public class ModuleData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.INT, length = 11, isKey = true, isAutoIncrement = true)
    private Integer id;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "create_time", type = MySqlTypeConstant.DATETIME)
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "update_time", type = MySqlTypeConstant.DATETIME)

    private Date updateTime;

    /**
     * 是否可用
     */
    @Column(name = "enable")


    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "enable", type = MySqlTypeConstant.VARCHAR)
    private String enable;


    /**
     * 感应值
     */
    @Column(name = "value")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "value", type = MySqlTypeConstant.VARCHAR, length = 111)

    private String value;


    /**
     * 模块id
     */
    @Column(name = "module_id")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "module_id", type = MySqlTypeConstant.INT)

    private int moduleId;

}
