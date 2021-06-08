package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 巡检周期
 */
@Data
public class SetPeriodParam {


    /**
     * 巡检间隔时间 单位 分钟
     */
    private Integer minitues;

    private String number;

}
