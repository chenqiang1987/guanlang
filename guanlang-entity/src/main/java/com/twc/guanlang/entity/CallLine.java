package com.twc.guanlang.entity;


import lombok.Data;


/**
 * 聊天线路
 */
@Data
public class CallLine {


    private Caller from;

    private Caller to;


    private int connectStatus;

    public class STATUS {


        /**
         * 0 等待被叫连接
         * 1 被叫确认连接,等待主叫确认连接
         * 2 主叫确认连接,连接成功
         * -1 连接失效
         */
        public static final int WAIT_TO = 0;

        public static final int WAIT_FROM = 1;

        public static final int SUCCESS = 2;

        public static final int LINE_DISABLE = -1;


    }

}
