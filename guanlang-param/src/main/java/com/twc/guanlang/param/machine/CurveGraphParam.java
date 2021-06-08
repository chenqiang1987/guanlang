package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 模块曲线图查询参数
 */
@Data
public class CurveGraphParam {

    private Integer machineId;

    private Integer moduleId;

    private String start;

    private String end;
}
