package com.twc.guanlang.vo;

import lombok.Data;

/**
 * webscoket消息
 *
 * @author chenqiang
 */
@Data
public class MachineWebScoket {


    /**
     * data / warning
     */
    String type;

    MachineWebScoketData machineWebScoketData;

    MachineWebScoketWarning machineWebScoketWarning;


}
