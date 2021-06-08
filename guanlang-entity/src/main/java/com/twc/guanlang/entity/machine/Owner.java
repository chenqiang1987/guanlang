package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 机器人所属单位
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_ower")

//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_ower")
public class Owner  {

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
     * 单位名称
     */
    @Column(name = "name")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR, length = 111)
    private String name;

    /**
     * 电话
     */
    @Column(name = "tel_phone")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "tel_phone", type = MySqlTypeConstant.VARCHAR, length = 111)
    private String telPhone;
}
