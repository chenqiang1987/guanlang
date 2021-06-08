package com.twc.guanlang.entity.recharge;

import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 充电桩门状态广播消息
 */
@Data
@Entity
@Table(name = "t_recharge_broadcast")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_recharge_broadcast")
public class RechargeMsg extends BaseEntity {


    /**
     * 门状态  open开close关
     */
    @Column(name = "doorState")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "doorState", type = MySqlTypeConstant.VARCHAR)
    String doorState;



    /**
     * 时间戳
     */
    @Column(name = "timeS")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "timeS", type = MySqlTypeConstant.BIGINT)
    String timeS;



}
