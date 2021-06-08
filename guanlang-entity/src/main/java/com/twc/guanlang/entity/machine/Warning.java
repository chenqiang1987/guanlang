package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 告警信息
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_warning")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_warning")
public class Warning {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.INT, length = 11, isKey = true, isAutoIncrement = true)
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


    @Column(name = "moduleName")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleName", type = MySqlTypeConstant.VARCHAR)
    private String moduleName;
    @Column(name = "moduleMaxVal")

    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleMaxVal", type = MySqlTypeConstant.DOUBLE)
    private Double moduleMaxVal;

    @Column(name = "moduleMinVal")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleMinVal", type = MySqlTypeConstant.DOUBLE)
    private Double moduleMinVal;

    @Column(name = "warningVal")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "warningVal", type = MySqlTypeConstant.DOUBLE)
    private Double warningVal;

    @Column(name = "moduleType")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "moduleType", type = MySqlTypeConstant.VARCHAR)
    private String moduleType;

    @Column(name = "serialNumber")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "serialNumber", type = MySqlTypeConstant.VARCHAR)
    private String serialNumber;

    /**
     * 报文时间戳
     */
    @Column(name = "dataTimeStamp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "dataTimeStamp", type = MySqlTypeConstant.BIGINT)
    private long dataTimeStamp;

    /**
     * 报文id
     */
    @Column(name = "dataMsgId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "dataMsgId", type = MySqlTypeConstant.VARCHAR)
    private String dataMsgId;

    /**
     * 告警类型
     */
    @Column(name = "type")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "type", type = MySqlTypeConstant.VARCHAR)
    private String type;

    /**
     * 告警类型名称
     */
    @Column(name = "typeName")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "typeName", type = MySqlTypeConstant.VARCHAR)
    private String typeName;

    /**
     * 告警内容
     */
    @Column(name = "content")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "content", type = MySqlTypeConstant.VARCHAR)
    private String conent;

    /**
     * 实际检测值
     */
    @Column(name = "dataVal")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "dataVal", type = MySqlTypeConstant.VARCHAR)
    private String dataVal;

    /**
     * 故障位置
     */
    @Column(name = "x")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "x", type = MySqlTypeConstant.VARCHAR)
    private String x;

    @Column(name = "y")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "y", type = MySqlTypeConstant.VARCHAR)
    private String y;


    @Column(name = "machineId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.INT)
    private Long machineId;

    public enum CODE1 {



        RECHARGE_FAILED_TIMEOUT("RECHARGE_FAILED_TIMEOUT", "大于6分钟未恢复充电状态(超时)!"),
        /**
         *
         */
        DATA_ERROR("DATA_ERROR", "数据错误!"),
        //平台类故障
        LOSE_DATA("LOSE_DATA", "检测数据丢失"),
        NULL_DATA_ITEM("NULL_DATA", "检测项丢失"),
        TEMPERATURE_DATA_LOWER("TEMPERATURE_DATA_LOWER", "温度低于下限!"),
        //专家类故障
        LINE_BREAK("LINE_BREAK", "管道损坏"),
        FIRE("FIRE", "火灾险情"),
        HOT("HOT", "温度高于上限!"),
        WET("WET", "湿度高于上限!"),
        OXGEN_LOWER("OXGEN_LOWER", "氧气浓度过低!"),
        OXGEN_OWER("OXGEN_OWER", "氧气浓度过高!"),
        CO_OWER("CO_OWER", "一氧化碳浓度过高!"),
        CH4_OWER("CH4_OWER", "甲烷浓度过高!"),
        PM1_OWER("PM1_OWER", "pm1.0过高!"),
        PM25_OWER("PM25_OWER", "pm2.5过高!"),
        PM10_OWER("PM10_OWER", "pm10过高!");

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