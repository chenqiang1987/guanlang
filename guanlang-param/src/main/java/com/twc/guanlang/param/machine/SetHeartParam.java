package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 巡检周期
 */
@Data
public class SetHeartParam {


    /**
     * 巡检间隔时间 单位 秒钟
     */
    private Integer seconds;

    private String number;

}
