package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 巡检周期
 */
@Data
public class ReportFaultParam {


    /**
     * 故障信息
     */
    private String msg;


    /**
     * 信息唯一标识
     */
    private String msgId;


    /**
     * 机器人唯一编号
     */
    private String number;


    /**
     * 故障时间戳
     */
    private Long timeStamp;


}
