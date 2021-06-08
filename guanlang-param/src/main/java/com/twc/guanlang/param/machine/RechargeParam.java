package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 返回充电机器人
 */

@Data
public class RechargeParam {

    /**
     * 机器人id
     */
    private Long machineId;


    /**
     * 充电桩id
     */
    private Long rechrgeId;


    private String ip;

    private int port;

}
