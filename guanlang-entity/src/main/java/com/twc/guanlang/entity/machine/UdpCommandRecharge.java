package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * udp 指令发送记录
 *
 * @author chenqiang
 */
@Data
@Entity
@Table(name = "t_udpcomman_recharge")
//@com.gitee.sunchenbin.mybatis.actable.annotation.Table(name = "t_udpcomman_recharge")
public class UdpCommandRecharge extends BaseEntity {


    @Column(name = "timeStamp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "timeStamp", type = MySqlTypeConstant.BIGINT)
    private Long timeStamp;


    @Column(name = "command")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "command", type = MySqlTypeConstant.VARCHAR)
    private String command;


    @Column(name = "ip")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "ip", type = MySqlTypeConstant.VARCHAR)
    private String ip;


    @Column(name = "port")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "port", type = MySqlTypeConstant.INT)
    private Integer port;

    /**
     * 收到对方请求作为此次通信成功
     */
    @Column(name = "isSuccess")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "isSuccess", type = MySqlTypeConstant.INT)
    private Integer isSuccess;


}
