package com.twc.guanlang.vo;

import lombok.Data;

/**
 * 向前端推送的websocket 机器人数据
 * <p>
 * 包括 坐标 空气质量 状态等信息
 *
 * @author chenqiang
 */
@Data
public class MachineWebScoketData {


    /**
     * 位置角度
     */
    private float x;
    private float y;
    private float theta;

    /**
     * 空气质量
     */
    private short humidity;
    private short oxygen;
    private short co;
    private short smoke;
    private short methance;
    private short hydrothion;
    private short o2;
    private short pm1;
    private short pm25;
    private short pm10;
    private short temperature;

    /**
     * 机器人状态
     */
    private String status;


    /**
     * 机器人充电状态
     */
    private String wirelessChargingState;

    /**
     * 剩余电量
     */
    private Integer energy;

    /**
     * 升降机高度
     */
    private Integer height;

}
