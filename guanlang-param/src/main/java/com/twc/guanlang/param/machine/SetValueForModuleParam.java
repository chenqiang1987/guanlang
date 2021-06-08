package com.twc.guanlang.param.machine;


import lombok.Data;

/**
 * 感应模块阈值设置
 */
@Data
public class SetValueForModuleParam {


    Integer machineId;

    String CO;

    String PM10;

    String PM25;

    String PM1;

    String TEMPERATURE;

    String HUMIDITY;

    String OXYGEN;

    String SMOKE;

    String METHANCE;

    String HYDROTHION;


}
