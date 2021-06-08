package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 启动机器人
 */
@Data
public class StartParam {

    private String number;

    private String ip;

    private int port;

}
