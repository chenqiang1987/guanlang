package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 机器人进入standby状态参数
 *
 * @author chenqiang
 */
@Data
public class StandbyParam {


    private Long id;

    /**
     * hand/half
     * <p>
     * 为空表示原地待命
     */
    String halfOrHand;

}
