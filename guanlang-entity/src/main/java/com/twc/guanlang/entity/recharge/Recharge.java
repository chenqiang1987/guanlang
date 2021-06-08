package com.twc.guanlang.entity.recharge;

import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 充电桩
 */
@Data
@Entity
@Table(name = "t_recharge")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_recharge")
public class Recharge extends BaseEntity {

    /**
     * 0关 1开
     */
    @Column(name = "doorState")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "doorState", type = MySqlTypeConstant.INT)
    Integer doorState;
    /**
     * 剩余电量
     */
    @Column(name = "rest")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "rest", type = MySqlTypeConstant.VARCHAR)
    String rest;

    /**
     * 编号
     */
    @Column(name = "number")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "number", type = MySqlTypeConstant.VARCHAR)
    String number;

    /**
     * 运行状态 normal正常运行 not_Normal非正常
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    String status;

    /**
     * 坐标x
     */
    @Column(name = "x")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "x", type = MySqlTypeConstant.DOUBLE)
    float x;

    /**
     * 坐标y
     */
    @Column(name = "y")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "y", type = MySqlTypeConstant.DOUBLE)
    float y;

    @Column(name = "voltage")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "voltage", type = MySqlTypeConstant.DOUBLE)
    Double voltage;

    @Column(name = "electricity")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "electricity", type = MySqlTypeConstant.DOUBLE)
    Double electricity;

    @Column(name = "online")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "online", type = MySqlTypeConstant.INT)
    Integer online;

    /**
     * ip地址
     */
    @Column(name = "ip", unique = true)
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "ip", type = MySqlTypeConstant.VARCHAR, isUnique = true)
    String ip;
    /**
     * 端口
     */
    @Column(name = "port")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "port", type = MySqlTypeConstant.INT)
    Integer port;


    /**
     * 机器人对应充电编号
     */
    @Column(name = "machineChargeNumber")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineChargeNumber", type = MySqlTypeConstant.INT)
    Integer machineChargeNumber;


    /**
     * 位置
     */
    @Column(name = "location")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "location", type = MySqlTypeConstant.INT)
    String location;

    /**
     * 充电桩门状态响应枚举
     */
    public enum RECHARGE_UDP_DOOR_RES_CODE {
        /**
         *
         */
        OPENED("02", "门已经打开!"),
        CLOSED("01", "门已经关闭");

        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        RECHARGE_UDP_DOOR_RES_CODE(String s, String i) {
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

    /**
     *
     */
    public enum STATUS_ENUM {
        /**
         *
         */
        NORMAL("NORMAL", "正常状态"),
        NOT_NORMAL("NOT_NORMAL", "不可用");

        /**
         *
         */
        private String code;
        private String msg;

        STATUS_ENUM(String s, String i) {
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