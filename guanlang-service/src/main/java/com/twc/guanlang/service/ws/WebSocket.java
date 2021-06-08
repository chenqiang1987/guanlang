package com.twc.guanlang.service.ws;

/**
 * 聊天websocket
 */

import com.alibaba.fastjson.JSONObject;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.maintain.WorkOrder;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.*;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.PositionService;
import com.twc.guanlang.service.RechargeService;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.vo.MachineWebScoket;
import com.twc.guanlang.vo.MachineWebScoketData;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/webSocket/{username}")

public class WebSocket {

    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private String username;

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {
        this.username = username;
        WebSocket.sessionMap.put(username, session);
        addOnlineCount();
        clients.put(username, this);
    }


    @OnClose

    public void onClose() throws IOException {
        clients.remove(username);
        WebSocket.sessionMap.remove(username);
        Session session = sessionMap.get(username);
        if (session != null){
            session.close();
            System.out.println(username+"-------is closed---"  +  System.currentTimeMillis());
        }
        subOnlineCount();
    }

    MachineWebScoketData  machineWebScoketData;




    @OnMessage

    public void onMessage(String message,Session session) throws IOException {


        if (message.equalsIgnoreCase("heart")) {
//            sendMessageAll("heart");
//            MachineWebScoket machineWebScoket = new MachineWebScoket();
//            machineWebScoket.setType("data");
//            MachineWebScoketData machineWebScoketData=this.machineWebScoketData;
//            double num = Math.random() * 20000;
//            machineWebScoketData.setX((int)num);
//             num = Math.random() * 30000;
//            machineWebScoketData.setY((int)num);
//
//            machineWebScoketData.setStatus("offline");
//
//            num = Math.random() * 30000;
//            machineWebScoketData.setSmoke((short)num);
//            num = Math.random() * 30000;
//            machineWebScoketData.setHumidity((short)num);
//            num = Math.random() * 30000;
//            machineWebScoketData.setPm10((short)num);
//            num = Math.random() * 30000;
//            machineWebScoketData.setTemperature((short)num);
//            machineWebScoket.setMachineWebScoketData(machineWebScoketData);
                //session.getAsyncRemote().sendText(message);

               if(jsonWsString!=null  && jsonWsString.length()>0)

                   session.getAsyncRemote().sendText(jsonWsString);

        }

    }


    @OnError

    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    public void sendMessageTo(String message, String To) throws IOException {
    }



    public void sendMessageAll(String message) throws IOException {



//        MachineWebScoket machineWebScoket = new MachineWebScoket();
//        machineWebScoket.setType("data");
//        MachineWebScoketData machineWebScoketData=new MachineWebScoketData();
//
//        machineWebScoketData.setX(100);
//        machineWebScoketData.setY(200);
//
//
//        machineWebScoket.setMachineWebScoketData(machineWebScoketData);

        for (String key : clients.keySet()) {
            Session session = WebSocket.sessionMap.get(key);
            if (session != null   && session.isOpen())
                System.out.println(session.getId()+"----"+session.isOpen()+ System.currentTimeMillis());
                //session.getAsyncRemote().sendText(message);
//            session.getAsyncRemote().sendText(JSONObject.toJSONString(machineWebScoket));

            session.getAsyncRemote().sendText(jsonWsString);
        }

    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }




//    @Scheduled(fixedRate = 10 * 1000)
    public void machineUdpDataSchdule_Test() throws IOException {

        MachineWebScoket machineWebScoket = new MachineWebScoket();
        machineWebScoket.setType("data");
        MachineWebScoketData machineWebScoketData=new MachineWebScoketData();

        machineWebScoketData.setX(100);
        machineWebScoketData.setY(200);
        machineWebScoket.setMachineWebScoketData(machineWebScoketData);
//        sendMessageAll(JSONObject.toJSONString(machineWebScoket));
    }


    /**
     * 定时接收机器人巡检数据和状态
     */
    @Scheduled(fixedRate = 10 * 1000)
    public void machineUdpDataSchdule() {

        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        List<Machine> machines = machineMapper.selectByExample(example);
        if (machines != null && machines.size() > 0) {
            for (Machine machine : machines) {
                String time = UdpUtil.machineUdpTimeStamp();
                StringBuffer template = UdpUtil.machineUdpSendTemlate();
                String udpStr = template.toString();
                udpStr = udpStr.replace("length", "0a00");
                udpStr = udpStr.replace("timeStamp", time);
                udpStr = udpStr.replace("type", MachineService.CODE1.ALL_DATA.getCode());
                udpStr = udpStr.replace("data", "");
                String tempStr = udpStr;
                udpStr = udpStr.replace("check", "");
                String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
                tempStr = tempStr.replace("check", checkCode);

//                try {
//                    UdpUtil.sendMachineUdp(tempStr, machine.getIp(), machine.getPort());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                String rs = null;
                try {
                    rs = UdpUtil.sendUdpOrderAndWaitReceive(tempStr, machine, 24102);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                if (StringUtil.isEmpty(rs)) return;
                resolveMachine0x40Data(rs, machine);
                UdpCommandMachine udpCommandMachine = new UdpCommandMachine();
                udpCommandMachine.setTimeStamp(Long.parseLong(time));
                udpCommandMachine.setCreateTime(new Date());
                udpCommandMachine.setUpdateTime(new Date());
                udpCommandMachine.setCommand(tempStr);
                udpCommandMachine.setIp(machine.getIp());
                udpCommandMachine.setPort(machine.getPort());
                udpCommandMachineMapper.insert(udpCommandMachine);
            }
        }
    }

    @Autowired
    UdpCommandMachineMapper udpCommandMachineMapper;
    @Resource
    private AutoMessionMapper autoMessionMapper;
    @Autowired
    private MachineService machineService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private MachineMapper machineMapper;

    @Autowired
    private RechargeMapper rechargeMapper;
//    @Resource
//    private WebSocket webSocket;
    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private WarningMapper warningMapper;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private Repair2UserMapper repair2UserMapper;
    @Autowired
    private UserMapper userMapper;

    //


    @Autowired
    private MonitorDataMapper monitorDataMapper;

    @Autowired
    private MachineRechargeMapper machineRechargeMapper;


    /**
     * 解析机器人上报数据
     * @param rs
     * @param machine
     */
    public void resolveMachine0x40Data(String rs, Machine machine) {
        //String rs = " bb a0 00     10 00 00 00    40     CD CC 8C 3F   CD CC 8C 3F   CD CC 8C 3F     10      30    00 01 02 03 04 00 01 02 03 04 00 01 02 03 04 00 01 02 03 ff    10   10   A0  66";
        rs = rs.replace(" ", "");
        String lengthStr = rs.substring(2, 6);
        String timeStampStr = rs.substring(6, 14);
        String typeCodeStr = rs.substring(14, 16);
        String positionStr = rs.substring(16, 40);
        String stateStr = rs.substring(40, 40 + 2);
        String heigthStr = rs.substring(42, 42 + 2);
        String airStr = rs.substring(44, 44 + 40);
        String dumpEnergyStr = rs.substring(84, 84 + 2);
        String butteryTempStr = rs.substring(86, 94);
        String wirelessChargingStateStr = rs.substring(94, 94 + 2);
        String checkStr = rs.substring(96, 96 + 2);
        String withOutCheck = rs.substring(0, 88 + 8) + rs.substring(90 + 8, rs.length());
        String checkCode = UdpUtil.getCheckCodeByhexByte(withOutCheck.toUpperCase());

//        log.info(checkStr + "   ：  校验码比对   ：" + checkCode);
//        log.info(" 机器人状态切换为   ：" + stateStr);
//        log.info(" 机器人充电状态切换为   ：" + wirelessChargingStateStr);

        float x = UdpUtil.hexStr2foloatBylittle(positionStr.substring(0, 8));
        float y = UdpUtil.hexStr2foloatBylittle(positionStr.substring(8, 16));
        float theta = UdpUtil.hexStr2foloatBylittle(positionStr.substring(16, 24));
        float bettryTemp = UdpUtil.hexStr2foloatBylittle(butteryTempStr);
        float dumpEnergy=0.0f;
        if(dumpEnergyStr.equalsIgnoreCase("00")){
             dumpEnergy=0.0f;
        }else{

             dumpEnergy=UdpUtil.hexStr2ShortBylittle(dumpEnergyStr+"00");
        }


        MachineWebScoketData machineWebScoketData = new MachineWebScoketData();
//        machineWebScoketData.setX(x);
//        machineWebScoketData.setY(y);


        machineWebScoketData.setX(23680f-y);
        machineWebScoketData.setY(9584f-x);

//        machineWebScoketData.setX(23680f-545.4984f);
//        machineWebScoketData.setY(9584f-3328.4067f);

        machineWebScoketData.setTheta(theta);


        short pm1 = UdpUtil.hexStr2ShortBylittle(airStr.substring(0, 4));
        short pm25 = UdpUtil.hexStr2ShortBylittle(airStr.substring(4, 8));
        short pm10 = UdpUtil.hexStr2ShortBylittle(airStr.substring(8, 12));
        short smoke = UdpUtil.hexStr2ShortBylittle(airStr.substring(12, 16));
        short ch4 = UdpUtil.hexStr2ShortBylittle(airStr.substring(16, 20));
        short co = UdpUtil.hexStr2ShortBylittle(airStr.substring(20, 24));
        short h2s = UdpUtil.hexStr2ShortBylittle(airStr.substring(24, 28));
        short o2 = UdpUtil.hexStr2ShortBylittle(airStr.substring(28, 32));
        short temprature = UdpUtil.hexStr2ShortBylittle(airStr.substring(32, 36));
        short hum = UdpUtil.hexStr2ShortBylittle(airStr.substring(36, 40));

        machineWebScoketData.setPm1(pm1);
        machineWebScoketData.setPm25(pm25);
        machineWebScoketData.setPm10(pm10);
        machineWebScoketData.setSmoke(smoke);
        machineWebScoketData.setCo(co);
        machineWebScoketData.setO2(o2);
        machineWebScoketData.setHumidity(hum);
        machineWebScoketData.setHydrothion(h2s);
        machineWebScoketData.setMethance(ch4);
        machineWebScoketData.setEnergy(UdpUtil.hex2decimal(dumpEnergyStr));
        machineWebScoketData.setHeight(UdpUtil.hex2decimal(heigthStr));
        machineWebScoketData.setOxygen(o2);

        machineWebScoketData.setTemperature(temprature);
        String stateName = "";
        Machine.CODE1[] code1s = Machine.CODE1.values();
        for (Machine.CODE1 code1 : code1s) {
            if (stateStr.equalsIgnoreCase(code1.getCode()))
                stateName = code1.getMsg();
        }
        machineWebScoketData.setStatus(stateName);

        String setWirelessChargingStateName = "";
        Machine.RECHARGE_CODE[] res_code = Machine.RECHARGE_CODE.values();
        for (Machine.RECHARGE_CODE code1 : res_code) {
            if (wirelessChargingStateStr.equalsIgnoreCase(code1.getCode()))
                setWirelessChargingStateName = code1.getMsg();
        }
        machineWebScoketData.setWirelessChargingState(setWirelessChargingStateName);

        this.machineWebScoketData=machineWebScoketData;
        MonitorData monitorData = new MonitorData();
        monitorData.setButteryTemp(bettryTemp + "");
        monitorData.setX(x + "");
        monitorData.setY(y + "");
        monitorData.setTheta(theta + "");
        monitorData.setCo(co + "");
        monitorData.setOxygen(o2 + "");
        monitorData.setHumidity(hum + "");
        monitorData.setHydrothion(h2s + "");
        monitorData.setMethance(ch4 + "");
        monitorData.setPm1(pm1 + "");
        monitorData.setPm25(pm25 + "");
        monitorData.setPm10(pm10 + "");
        monitorData.setTemperature(temprature + "");
        monitorData.setSmoke(smoke + "");
        monitorData.setCreateTime(new Date());
        monitorData.setEnable(BaseEntity.ENABLE.ENABLE);
        monitorData.setMachineId(machine.getId());
        monitorData.setDumpEnergy(dumpEnergy+"");



        MachineService.heartData.put(machine.getId(), machineWebScoketData);
        MachineService.onLineMap.put(machine.getId(), "on");
        MachineService.heartMap.put(machine.getId(), System.currentTimeMillis());


        System.out.println("机器人状态==============:"+stateStr);
        if (stateStr.equalsIgnoreCase(com.twc.guanlang.entity.machine.Machine.CODE1.RECHARGING.getCode())) {
            MachineService.WORK_STATUS = Machine.CODE1.RECHARGING.getMsg();
        } else if (stateStr.equalsIgnoreCase(Machine.CODE1.PATROLLING.getCode())) {
            MachineService.WORK_STATUS = Machine.CODE1.PATROLLING.getMsg();

            /**
             * 发现机器人自动巡检后 把未完成的充电任务全部归类到  完成
             */
            Example example=new Example(MachineRecharge.class);
            example.createCriteria().orEqualTo("status",MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode()).orEqualTo("status",MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
            List<MachineRecharge> list=machineRechargeMapper.selectByExample(example);
            if(list!=null  && list.size() >0){

                for(MachineRecharge machineRecharge:list){

                    machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RECHARGE_COMPLETED.getCode());
                    machineRecharge.setUpdateTime(new Date());
                    machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                }

            }


        } else if (stateStr.equalsIgnoreCase(Machine.CODE1.STANDINGBY.getCode())) {
            MachineService.WORK_STATUS =Machine.CODE1.STANDINGBY.getMsg();
        }else if (stateStr.equalsIgnoreCase(Machine.CODE1.RETURNING.getCode())) {
            MachineService.WORK_STATUS =Machine.CODE1.RETURNING.getMsg();
        }else if (stateStr.equalsIgnoreCase(Machine.CODE1.RECHARGE_FAILURE.getCode())) {
            MachineService.WORK_STATUS =Machine.CODE1.RECHARGE_FAILURE.getMsg();
        }else if (stateStr.equalsIgnoreCase(Machine.CODE1.INITIAL_FAILED.getCode())) {
            MachineService.WORK_STATUS =Machine.CODE1.INITIAL_FAILED.getMsg();
        }else if (stateStr.equalsIgnoreCase(Machine.CODE1.LOCATE_FAILURE.getCode())) {
            MachineService.WORK_STATUS =Machine.CODE1.LOCATE_FAILURE.getMsg();
        }else if (stateStr.equalsIgnoreCase(Machine.CODE1.LOWER_POWER.getCode())) {
            MachineService.WORK_STATUS = Machine.CODE1.LOWER_POWER.getMsg();
        } else if (stateStr.equalsIgnoreCase(Machine.CODE1.Ready.getCode())) {
            MachineService.WORK_STATUS = Machine.CODE1.Ready.getMsg();

            /**
             * 发现机器人初始化以后 把未完成的充电任务全部归类到  重启中断 中
             */
            Example example=new Example(MachineRecharge.class);
            example.createCriteria().orEqualTo("status",MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode()).orEqualTo("status",MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
            List<MachineRecharge> list=machineRechargeMapper.selectByExample(example);
            if(list!=null  && list.size() >0){

                for(MachineRecharge machineRecharge:list){

                    machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.REBOOT_DIS.getCode());
                    machineRecharge.setUpdateTime(new Date());
                    machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                }

            }
        }else if(stateStr.equalsIgnoreCase(Machine.CODE1.Initiating.getCode())) {
                MachineService.WORK_STATUS =Machine.CODE1.Initiating.getMsg();

                /**
                 * 发现机器人初始化以后 把未完成的充电任务全部归类到  重启中断 中
                 */
                Example example=new Example(MachineRecharge.class);
                example.createCriteria().orEqualTo("status",MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode()).orEqualTo("status",MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
                List<MachineRecharge> list=machineRechargeMapper.selectByExample(example);
                if(list!=null  && list.size() >0){

                    for(MachineRecharge machineRecharge:list){

                        machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.REBOOT_DIS.getCode());
                        machineRecharge.setUpdateTime(new Date());
                        machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                    }

                }

            }

        monitorData.setStatus(MachineService.WORK_STATUS);
        checkModuleData(monitorData);
        monitorDataMapper.insert(monitorData);

        /**
         *  请求充电桩关门：
         *  1 机器人成功改变状态为recharging并且正在充电
         *  2 机器人进入patrolling状态
         *
         */
        if ((stateStr.equalsIgnoreCase(com.twc.guanlang.entity.machine.Machine.CODE1.RECHARGING.getCode())
                && !wirelessChargingStateStr.equalsIgnoreCase(Machine.WireLessRechargeStatus.UNRECHARGE)) ||
                stateStr.equalsIgnoreCase(Machine.CODE1.PATROLLING.getCode())
        ) {
            Long rechargeId = MachineService.machineRechargeCallOpenMap.get(machine.getId());
            if (rechargeId != null) {
                CloseDoorParam closeDoorParam = new CloseDoorParam();
                closeDoorParam.setId(rechargeId);
                // rechargeService.closeDoor(closeDoorParam);
                //如果机器人成功进入patrolling状态则清除机器人和充电桩的关联关系
                if (stateStr.equalsIgnoreCase(Machine.CODE1.PATROLLING.getCode())) {
                    MachineService.machineRechargeCallOpenMap.remove(machine.getId());
                }
            }
        }
        try {
            MachineWebScoket machineWebScoket = new MachineWebScoket();
            machineWebScoket.setType("data");
            machineWebScoket.setMachineWebScoketData(machineWebScoketData);
//            webSocket.sendMessageAll(JSONObject.toJSONString(machineWebScoket));

            jsonWsString=JSONObject.toJSONString(machineWebScoket);
            sendMessageAll(JSONObject.toJSONString(machineWebScoket));


        } catch (IOException e) {
//            log.error(" websocket数据发送失败： " + e.getMessage());
        }
    }


    String jsonWsString="";
//    static MachineWebScoketData machineWebScoketData;



    /**
     * 检测机器人上报数据  -- 越界告警，告警后生成工单分配维修人员
     * @param monitorData
     */
    private void checkModuleData(MonitorData monitorData) {
        String number = monitorData.getSerialNumber();
        /**
         * 获取感应器模块集合
         */
        Module.CODE1[] code1s = Module.CODE1.values();
        for (Module.CODE1 code1 : code1s) {
            String moduleCode = code1.getCode();
            String moduleName = code1.getMsg();
            Example example = new Example(Module.class);
            example.createCriteria().andEqualTo("type", moduleCode);
            Module module = moduleMapper.selectOneByExample(example);
            Double max = module.getMaxValue();
            Double min = module.getMinValue();
//            Double waringValue = Double.parseDouble(module.getWarningValue());
            Warning warning = new Warning();
            String warningCode = null;
            String warningMsg = null;
            String temp = null;
            Double dataVal = null;
            boolean flag = false;
            switch (moduleCode) {
                case "PM1":
                    temp = monitorData.getPm1();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {
                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "PM1数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(0d) < 0) {
                            warningCode = Warning.CODE1.DATA_ERROR.getCode();
                            warningMsg = "PM1数据不能小于0";
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.PM1_OWER.getCode();
                            warningMsg = Warning.CODE1.PM1_OWER.getMsg();
                            flag = true;
                        }
                    }
                    break;
                case "PM25":
                    temp = monitorData.getPm25();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {

                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "PM2.5数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(0d) < 0) {
                            warningCode = Warning.CODE1.DATA_ERROR.getCode();
                            warningMsg = "PM2.5数据不能小于0,实测:" + dataVal;
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.PM25_OWER.getCode();
                            warningMsg = "PM2.5数据大于最大值:" + max + ",实测:" + dataVal;
                            flag = true;
                        }
                    }
                    break;
                case "PM10":
                    temp = monitorData.getPm10();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {

                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "PM10数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(0d) < 0) {
                            warningCode = Warning.CODE1.DATA_ERROR.getCode();
                            warningMsg = "PM10数据不能小于0,实测:" + dataVal;
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.PM10_OWER.getCode();
                            warningMsg = "PM10数据大于最大值:" + max + ",实测:" + dataVal;
                            flag = true;
                        }
                    }
                    break;
                case "TEMPERATURE":
                    temp = monitorData.getTemperature();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {
                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "温度数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(min) < 0) {
                            warningCode = Warning.CODE1.TEMPERATURE_DATA_LOWER.getCode();
                            warningMsg = "温度数据低于最小值:" + min + "摄氏度,实测:" + dataVal;
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.HOT.getCode();
                            warningMsg = "温度高于最大值:" + max + "摄氏度,实测:" + dataVal;
                            flag = true;
                        }
                    }
                    break;
                case "HUMIDITY":
                    temp = monitorData.getHumidity();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {
                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "湿度数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(0d) < 0) {
                            warningCode = Warning.CODE1.DATA_ERROR.getCode();
                            warningMsg = "湿度数据小于0,实测:" + dataVal;
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.WET.getCode();
                            warningMsg = "湿度高于最大值:" + max + ",实测:" + dataVal;
                            flag = true;
                        }
                    }
                    break;
                case "HYDROTHION":
                    temp = monitorData.getHydrothion();
                    dataVal = Double.parseDouble(temp);
                    if (StringUtil.isEmpty(temp)) {
                        warningCode = Warning.CODE1.NULL_DATA_ITEM.getCode();
                        warningMsg = "硫化氢数据丢失!";
                        flag = true;
                    } else {
                        if (dataVal.compareTo(0d) < 0) {
                            warningCode = Warning.CODE1.DATA_ERROR.getCode();
                            warningMsg = "PM2.5数据不能小于0,实测:" + dataVal;
                            flag = true;
                        } else if (dataVal.compareTo(max) > 0) {
                            warningCode = Warning.CODE1.PM25_OWER.getCode();
                            warningMsg = "PM2.5数据大于最大值:" + max + ",实测:" + dataVal;
                            flag = true;
                        }
                    }
                    break;
                case "CO":
                    temp = monitorData.getCo();
                    dataVal = Double.parseDouble(temp);
                    break;
                case "OXYGEN":
                    temp = monitorData.getOxygen();
                    dataVal = Double.parseDouble(temp);
                    break;
                case "METHANCE":
                    temp = monitorData.getMethance();
                    dataVal = Double.parseDouble(temp);
                    break;
                case "SMOKE":
                    temp = monitorData.getSmoke();
                    dataVal = Double.parseDouble(temp);
                    break;
            }
            if (flag) {
                warning.setType(warningCode);
                warning.setTypeName(warningMsg);
                warning.setDataVal(temp);
                warning.setConent(number + "#" + moduleName + "#" + warningMsg);
                warning.setCreateTime(new Date());
                warning.setUpdateTime(new Date());
                warning.setEnable(BaseEntity.ENABLE.ENABLE);
                warning.setSerialNumber(number);
                warning.setModuleName(moduleName);
                warning.setModuleMaxVal(module.getMaxValue());
                warning.setModuleMinVal(module.getMinValue());
//                warning.setWarningVal(module.getWarningValue());
                warning.setModuleType(module.getType());
                warning.setDataTimeStamp(monitorData.getTimeStamp());
                warning.setDataMsgId(monitorData.getMsgId());
                warning.setMachineId(monitorData.getMachineId());
                warning.setX(monitorData.getX());
                warning.setY(monitorData.getY());
                warningMapper.insert(warning);
                WorkOrder workOrder = new WorkOrder();
                workOrder.setFalutId(warning.getId());
                workOrder.setCreateTime(new Date());
                workOrder.setUpdateTime(new Date());
                workOrder.setContent(warning.getConent());
                //机器人的userid=0
                workOrder.setExecuterId(0l);
                workOrder.setEnable("1");
                workOrder.setStatus(WorkOrder.CODE1.WAIT_REPAIR.getCode());
                workOrder.setFaultCode(warningCode);
                workOrder.setFaultName(warningMsg);
                workOrder.setStatusName(WorkOrder.CODE1.WAIT_REPAIR.getMsg());

                SystemUser repaireUser = autoMatchRepairUserByFaultCode(warningCode);
                if (repaireUser != null)
                    workOrder.setUserId(repaireUser.getId());
                workOrder.setStart(new Date());
                workOrderMapper.insert(workOrder);
            }
        }
    }

    /**
     * 自动选择维修人员
     * @param faultCode
     * @return
     */
    public SystemUser autoMatchRepairUserByFaultCode(String faultCode) {
        Example example = new Example(WorkOrder.class);
        example.createCriteria().andEqualTo("faultCode", faultCode).andEqualTo("status", WorkOrder.CODE1.REPAIRING);
        List<WorkOrder> repairing = workOrderMapper.selectByExample(example);
        List<Repair2User> repair2Users = getUserByFaultCode(faultCode);
        Long userId = null;
        List<Repair2User> busy = new ArrayList<>();
        if (repair2Users != null && repair2Users.size() > 0) {
            for (Repair2User repair2User : repair2Users) {
                if (repairing == null || repairing.size() == 0) {
                    return userMapper.selectByPrimaryKey(repair2Users.get(0).getUserId());
                } else {
                    for (WorkOrder workOrder : repairing) {
                        if (repair2User.getUserId() == workOrder.getUserId()) {
                            busy.add(repair2User);
                        }
                    }
                }
            }
            //获得空闲工程师
            repair2Users.removeAll(busy);
            //没有空闲工程师 ，则随机挑选一个，否则取第一个空闲工程师
            if (repair2Users == null) {
                int size = repair2Users.size();
                Random random = new Random(size);
                int randomid = random.nextInt();
                return userMapper.selectByPrimaryKey(repair2Users.get(randomid));
            } else {
                return userMapper.selectByPrimaryKey(repair2Users.get(0));
            }
        }
        return null;
    }

    /**
     * 根据故障寻找专家
     *
     * @param faultCode
     * @return
     */
    public List<Repair2User> getUserByFaultCode(String faultCode) {
        Example example = new Example(Repair2User.class);
        example.createCriteria().andEqualTo("repairCode", faultCode);
        List<Repair2User> repair2Users = repair2UserMapper.selectByExample(example);
        return repair2Users;
    }
}