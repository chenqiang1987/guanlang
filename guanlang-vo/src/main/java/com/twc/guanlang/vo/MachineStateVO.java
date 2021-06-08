package com.twc.guanlang.vo;

import lombok.Data;

import java.util.List;

@Data
public class MachineStateVO extends BaseVO {


    /**
     * 剩余电量
     */
    String restElc;


    /**
     * 硬件 状态
     */
    String hardWareState;

    /**
     *故障
     */
    List<String> faults;

    /**
     * 任务状态
     */
    String messionState;


    /**
     * 维修状态
     */
    String repairState;

    /**
     * 专家协助状态
     */
    String expertState;

}
