package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 记录机器人进充电桩
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "machine_recharge")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "machine_recharge")
public class MachineRecharge extends BaseEntity {

    @Column(name = "machineId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineId", type = MySqlTypeConstant.BIGINT)
    private Long machineId;

    @Column(name = "rechargeId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "rechargeId", type = MySqlTypeConstant.BIGINT)
    private Long rechargeId;

    @Column(name = "machineIp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "machineIp", type = MySqlTypeConstant.VARCHAR)
    private String machineIp;

    @Column(name = "rechargeIp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "rechargeIp", type = MySqlTypeConstant.VARCHAR)
    private String rechargeIp;

    /**
     * 完成条件：机器人进站后再成功出站
     */
    @Column(name = "status")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "status", type = MySqlTypeConstant.VARCHAR)
    private String status;

    /**
     * 失敗原因
     */
    @Column(name = "failed_reason")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "failed_reason", type = MySqlTypeConstant.VARCHAR)
    private String failed_reason;

    public enum STATUS_ENUM {
        /**
         *
         */
//        RETURNING("RETURNING", "返回充电桩途中!"),
        WAIT_DOOR_PEN("WAIT_DOOR_PEN", "等待门开"),
        RECHARG_STARTED("RECHARG_STARTED", "充电已经开始"),
        RECHARGE_COMPLETED("RECHARGE_COMPLETED", "充電結束"),
        REBOOT_DIS("REBOOT_DIS", "重启中断"),
        PROTECT("PROTECT", "保护状态"),
        RECHARGE_FAILED_TIMEOUT("RECHARGE_FAILED", "充电状态下大于6分钟未恢复充电");

        /**
         * 对应udp消息里面的code
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
