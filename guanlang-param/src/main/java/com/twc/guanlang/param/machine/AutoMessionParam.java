package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 任务添加参数
 *
 * @author chenqiang
 */
@Data
public class AutoMessionParam {

    Long machineId;

    String[] start;

    String targetPositon;

    /**
     * auto/half/hand
     */
    String type;

}
