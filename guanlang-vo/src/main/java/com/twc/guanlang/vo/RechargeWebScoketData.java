package com.twc.guanlang.vo;

import lombok.Data;

/**
 * 充电桩websocketData
 *
 * @author chenqiang
 */
@Data
public class RechargeWebScoketData {


    /**
     * 位置角度
     */
    private float x;
    private float y;

    /**
     * 电量
     */
    private String power;

    /**
     * 电压
     */
    private String vol;

    /**
     * 电流
     */
    private String electricity;

    /**
     * 在线离线
     */
    private Integer isOnline;


    /**
     * 开关门状态
     */
    private String openStatus;


}
