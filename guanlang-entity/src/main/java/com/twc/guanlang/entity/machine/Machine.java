package com.twc.guanlang.entity.machine;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 机器人
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_machine")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_machine")
public class Machine {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "id", type = MySqlTypeConstant.BIGINT, length = 11, isKey = true, isAutoIncrement = true)
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
     * 机器人名称
     */
    @Column(name = "name")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "name", type = MySqlTypeConstant.VARCHAR)
    private String name;

    /**
     * 机器人编号
     */
    @Column(name = "serial_number")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "serial_number", type = MySqlTypeConstant.VARCHAR)
    private String serialNumber;

    /**
     * 位置三要素
     */
    @Column(name = "position_x")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "position_x", type = MySqlTypeConstant.VARCHAR)
    private String positionX;

    @Column(name = "position_y")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "position_y", type = MySqlTypeConstant.VARCHAR)
    private String positionY;

    /**
     * 角度
     */
    @Column(name = "angle")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "angle", type = MySqlTypeConstant.VARCHAR)
    private String angle;

    /**
     * 升降机高度 cm
     */
    @Column(name = "height")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "height", type = MySqlTypeConstant.VARCHAR)
    private String height;

    /**
     * 机器人当前状态
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    private String status;

    @Column(name = "type")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "type", type = MySqlTypeConstant.VARCHAR)
    private int type;

    /**
     * 所属公司
     */
    @Column(name = "company")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "company", type = MySqlTypeConstant.VARCHAR)
    private String company;

    /**
     * ip地址
     */
    @Column(name = "ip", unique = true)
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "ip", type = MySqlTypeConstant.VARCHAR, isUnique = true)
    private String ip;

    /**
     * 端口
     */
    @Column(name = "port")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "port", type = MySqlTypeConstant.INT)
    private Integer port;


    /**
     * 默认机器人，全局只允许一台机器人允许作业
     */
    @Column(name = "isDefault")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "isDefault", type = MySqlTypeConstant.INT)
    private Integer isDefault;


    /**
     * 初始化充電樁編號
     */
    @Column(name = "initRechargeId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "initRechargeId", type = MySqlTypeConstant.INT)
    private Integer initRechargeId;





    /**
     * 无线充电状态
     * <p>
     * 对应udp消息里面的code
     */
    public static class WireLessRechargeStatus {

        public static final String UNRECHARGE = "00";
        public static final String RECHARGING = "01";
        public static final String RECHARGECOMPLATE = "02";
    }


    public static class TYPE {
        /**
         * 无线
         */
        public static final int WIRELESS = 1;
        /**
         * 有线
         */
        public static final int WIRE = 0;
    }

    public enum CODE1 {
        /**
         *
         */
        INITIAL_FAILED("FF", "初始化失败!"),
        OUT_OF_FAILURE("FE", "脱困失败"),
        LOWER_POWER("FD", "电量过低"),
        LOCATE_FAILURE("FC", "机器人定位失败!"),
        RECHARGE_FAILURE("FB", "充電失敗!"),

        STANDINGBY("01", "StandingBy状态"),
        PATROLLING("02", "Patrolling状态"),
        RETURNING("03", "Returning状态"),
        RECHARGING("04", "Recharging状态"),
        Initiating("05","Initiating状态"),
        Ready("06","ready状态");



        /**
         * 对应udp消息里面的code
         */
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

    public enum RECHARGE_CODE {
        /**
         *
         */
        UN_RECHARGE("00", "不在无线充电!"),
        WIRELESS_RECHARGING("01", "无线充电中"),
        RECHARGING_COMPLETE("02", "无线充电已满");


        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        RECHARGE_CODE(String s, String i) {
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


    public enum WORK_STATUS {
        /**
         *
         */
        HALF_WORKING("HALF", "半自动任务执行中"),
        HAND_WORKING("HAND", "手动任务执行中"),
        AUTO_WORKING("AUTO", "自动任务执行中"),
        HOLDING("HOLDING", "原地待机中"),
        CHARGIN("CHARGIN", "充电中");

        /**
         * 对应udp消息里面的code
         */
        private String code;
        private String msg;

        WORK_STATUS(String s, String i) {
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

    public static void main(String s[]) {
        System.out.println(CODE1.INITIAL_FAILED.name());
    }
}