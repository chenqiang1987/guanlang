package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 机器人报文
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_monitor_data")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_monitor_data")
public class MonitorData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.BIGINT, length = 20, isKey = true, isAutoIncrement = true)
    private Long id;

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
     * 机器人编号
     */
    @Column(name = "serialNumber")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "serialNumber", type = MySqlTypeConstant.VARCHAR)
    private String serialNumber;

    @Column(name = "machineId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
    private Long machineId;

    /**
     * 温度传感器
     */

    @Column(name = "temperature")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "temperature", type = MySqlTypeConstant.VARCHAR)
    public String temperature;

    /**
     * 湿度传感器
     */
    @Column(name = "humidity")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "humidity", type = MySqlTypeConstant.VARCHAR)
    public String humidity;

    /**
     * 氧气浓度传感器
     */
    @Column(name = "oxygen")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "oxygen", type = MySqlTypeConstant.VARCHAR)
    public String oxygen;

    /**
     * 一氧化碳传感器
     */
    @Column(name = "co")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "co", type = MySqlTypeConstant.VARCHAR)
    public String co;

    /**
     * 烟雾传感器
     */
    @Column(name = "smoke")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "smoke", type = MySqlTypeConstant.VARCHAR)
    public String smoke;

    /**
     * 甲烷传感器
     */
    @Column(name = "methance")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "methance", type = MySqlTypeConstant.VARCHAR)
    public String methance;

    /**
     * 空气质量传感器
     */
//    @Column(name = "airquality")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "airquality", type = MySqlTypeConstant.VARCHAR)
//    public String airquality;

    @Column(name = "pm1")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm1", type = MySqlTypeConstant.VARCHAR)
    public String pm1;

    @Column(name = "pm25")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm25", type = MySqlTypeConstant.VARCHAR)
    public String pm25;

    @Column(name = "pm10")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm10", type = MySqlTypeConstant.VARCHAR)
    public String pm10;

    /**
     * 硫化氢传感器
     */
    @Column(name = "hydrothion")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "hydrothion", type = MySqlTypeConstant.VARCHAR)
    public String hydrothion;

    @Column(name = "timeStamp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "timeStamp", type = MySqlTypeConstant.BIGINT)
    public long timeStamp;

    /**
     * 消息编号
     */
    @Column(name = "msgId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "msgId", type = MySqlTypeConstant.VARCHAR)
    public String msgId;

    /**
     * 位置 x
     */
    @Column(name = "x")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "x", type = MySqlTypeConstant.VARCHAR)
    public String x;

    /**
     * 位置y
     */
    @Column(name = "y")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "y", type = MySqlTypeConstant.VARCHAR)
    public String y;

    @Column(name = "theta")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "theta", type = MySqlTypeConstant.VARCHAR)
    public String theta;

    /**
     * 电池温度
     */
    @Column(name = "butteryTemp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "butteryTemp", type = MySqlTypeConstant.VARCHAR)
    private String butteryTemp;


    /**
     * 机器人工作状态
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    private String status;


    /**
     * 剩余电量
     */
    @Column(name = "dumpEnergy")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "dumpEnergy", type = MySqlTypeConstant.VARCHAR)
    private String dumpEnergy;

//    /**
//     * 告警信息
//     */
//    @Column(name = "waringMsg")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "waringMsg", type = MySqlTypeConstant.VARCHAR)
//    public String waringMsg;
}
