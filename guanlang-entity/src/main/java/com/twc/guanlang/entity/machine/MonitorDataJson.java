package com.twc.guanlang.entity.machine;


import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import net.sf.json.JSONObject;

import javax.persistence.Column;

import java.util.Date;


/**
 * 机器人报文json
 * @author chenqiang
 */
@Data
public class MonitorDataJson {


    /**
     * 机器人编号
     */
    @Column(name = "serialNumber")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "serialNumber", type = MySqlTypeConstant.VARCHAR)
    private String serialNumber;

    /**
     * 温度传感器
     */

    @Column(name = "temperature")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "temperature", type = MySqlTypeConstant.VARCHAR)
    public String temperature;

    /**
     * 湿度传感器
     */

    @Column(name = "humidity")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "humidity", type = MySqlTypeConstant.VARCHAR)
    public String humidity;

    /**
     * 氧气浓度传感器
     */

    @Column(name = "oxygen")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "oxygen", type = MySqlTypeConstant.VARCHAR)
    public String oxygen;


    /**
     * 一氧化碳传感器
     */

    @Column(name = "co")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "co", type = MySqlTypeConstant.VARCHAR)
    public String co;

    /**
     * 烟雾传感器
     */

    @Column(name = "smoke")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "smoke", type = MySqlTypeConstant.VARCHAR)
    public String smoke;

    /**
     * 甲烷传感器
     */

    @Column(name = "methance")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "methance", type = MySqlTypeConstant.VARCHAR)
    public String methance;


    /**
     * 空气质量传感器
     */

    @Column(name = "pm1")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm1", type = MySqlTypeConstant.VARCHAR)
    public String  pm1;

    @Column(name = "pm25")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm25", type = MySqlTypeConstant.VARCHAR)
    public String  pm25;


    @Column(name = "pm10")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "pm10", type = MySqlTypeConstant.VARCHAR)
    public String pm10;

    /**
     * 硫化氢传感器
     */

    @Column(name = "hydrothion")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "hydrothion", type = MySqlTypeConstant.VARCHAR)
    public String hydrothion;


    @Column(name = "timeStamp")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "timeStamp", type = MySqlTypeConstant.DOUBLE)
    public long timeStamp;

    /**
     * 消息编号
     */
    @Column(name = "msgId")
    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "msgId", type = MySqlTypeConstant.VARCHAR)
    public String msgId;

    /**
     * 二氧化碳
     */
//    @Column(name = "co2")
//    @com.gitee.sunchenbin.mybatis.actable.annotation.Column(name = "co2", type = MySqlTypeConstant.VARCHAR)
//    public String co2;



    public String x;

    public String y;

    public static void main(String s[]) {


    }

    public static String test() {

        MonitorDataJson monitorMsgJson = new MonitorDataJson();
        monitorMsgJson.setHydrothion("1.11");
        //monitorMsgJson.setAirquality("80.00");
        monitorMsgJson.setHydrothion("0.11");
        monitorMsgJson.setOxygen("0.12");
        monitorMsgJson.setCo("1.20");
        monitorMsgJson.setPm1("1");
        monitorMsgJson.setPm25("3");
        monitorMsgJson.setPm10("1.6");
        monitorMsgJson.setSmoke("0.88");
        monitorMsgJson.setTemperature("26.00");
        monitorMsgJson.setMethance("0.18");
        monitorMsgJson.setTimeStamp(new Date().getTime());
        monitorMsgJson.setMsgId("12fsf-r4gfdg-22ferghtrh-wefdffg");


        return JSONObject.fromObject(monitorMsgJson).toString();
    }
}
