package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 机器人感应模块
 */
@Data
@Entity
@Table(name = "t_module")

//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_module")
public class Module {

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
     * 传感器类型
     */
    @Column(name = "type")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "type", type = MySqlTypeConstant.VARCHAR, length = 111)
    private String type;


    @Column(name = "name")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR, length = 111)
    private String name;


    /**
     * 供应商名称
     */
    @Column(name = "provider")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "provider", type = MySqlTypeConstant.VARCHAR)
    private String provider;

    /**
     * 感应上限
     */
    @Column(name = "max_value")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "max_value", type = MySqlTypeConstant.DOUBLE)
    private Double maxValue;

    /**
     * 感应上限
     */
    @Column(name = "min_value")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "min_value", type = MySqlTypeConstant.DOUBLE)
    private Double minValue;

    public enum CODE1 {

        /**
         *
         */
        TEMPERATURE("TEMPERATURE", "温度传感器"),
        HUMIDITY("HUMIDITY", "湿度传感器"),
        OXYGEN("OXYGEN", "氧气浓度传感器"),
        CO("CO", "一氧化碳"),
        SMOKE("SMOKE", "烟雾传感器"),
        METHANCE("METHANCE", "甲烷浓度传感器"),
        HYDROTHION("HYDROTHION", "硫化氢浓度传感器"),
        PM1("PM1", "PM1传感器"),
        PM25("PM25", "PM25传感器"),
        PM10("PM10", "PM10传感器");

        private String code;
        private String msg;

        CODE1(String s, String i) {
            this.code = s;
            this.msg = i;
        }

        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }
    }
}