package com.twc.guanlang.service;

import com.alibaba.fastjson.JSONObject;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.time.TwcTimeUtil;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.maintain.WorkOrder;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.*;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.service.ws.WebSocket;
import com.twc.guanlang.vo.MachineWebScoket;
import com.twc.guanlang.vo.MachineWebScoketData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * udp方式接收机器人消息-此类由于改动 功能暂时遗弃
 */
@Slf4j
@Configuration
@EnableScheduling
public class MachineUdpService {

    /**
     * 监听充电请求
     */
//    public static Map<String, String> rechargeMap = new HashMap();
//    @Value("${machine.udp.port}")
//    private String port;
//    @Resource
//    private MonitorDataMapper monitorDataMapper;
//    @Resource
//    private WarningMapper warningMapper;
//    @Resource
//    private ModuleMapper moduleMapper;
//    @Resource
//    private UdpDataMapper errorUdpDataMapper;
//    @Autowired
//    private WorkOrderMapper workOrderMapper;
//    @Autowired
//    private UserMapper userMapper;
//    public static Map<Integer, Long> heartMap = new HashMap();
//    public static Map<Long, String> onLineMap = new HashMap();

    /**
     * 构造函数--启动线程监听机器人上报数据--此方式暂时遗弃，改为UpdUtil中的同步方法失败重发机制
     * 监听接收机器人udp数据
     *
     * @throws SocketException
     */
//    public MachineUdpService() throws SocketException {
//
//        //创建接收端的Socket服务对象，并且指定端口号
//        /**
//         * 这里需要异步启动，否则springboot会被阻塞在 while(true)语句块里
//         */
//        DatagramSocket ds = new DatagramSocket(1000000000);
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    log.info("启动udp监听..........................");
//                    while (true) {
//                        //创建一个数据包，用于接收数据
//                        try {
//                            byte[] bys = new byte[500];
//                            DatagramPacket dp = new DatagramPacket(bys, bys.length);
//                            //接收数据
//                            ds.receive(dp);
//                            //解析数据
//                            //获取ip地址
//                            String ip = dp.getAddress().getHostAddress();
//
//                            log.info("接收到ip:" + ip + "机器人的消息");
//                            Example example = new Example(Machine.class);
//                            example.createCriteria().andEqualTo("ip", ip);
//                            Machine machine = machineMapper.selectOneByExample(example);
//                            Long time = System.currentTimeMillis();
//                            heartMap.put(machine.getId().intValue(), time);
//                            onLineMap.put(machine.getId(), "on");
//                            //获取数据
//                            String data = new String(dp.getData(), 0, dp.getLength());
//                            StringBuffer stringBuffer = new StringBuffer(data);
//                            for (byte b : dp.getData()) {
//                                stringBuffer.append(String.format("%02x", b));
//                            }
//                            String rs = stringBuffer.toString();
//                            log.info(" 收到机器人" + ip + "udp原始消息============:" + rs);
//                            int start = rs.indexOf("bb");
//                            int end = rs.lastIndexOf("66");
//                            if (start == -1 || end == -1) {
//                                return;
//                            }
//                            rs = rs.substring(start, end + 2);
//                            log.info(" 收到机器人" + ip + "udp消息，去除乱码============:" + rs);
//                            MachineUdpData machineUdpData = new MachineUdpData();
//                            machineUdpData.setCreateTime(new Date());
//                            machineUdpData.setUpdateTime(new Date());
//                            machineUdpData.setIp(ip);
//                            machineUdpData.setData(rs);
////                            data = data.toLowerCase();
//                            errorUdpDataMapper.insert(machineUdpData);
//                            String typeCode = getudpDataTypeFromData(rs);
//                            if (typeCode.equals(CODE1.ALL_DATA.getCode())) {
//
//                                resolveMachine0x40Data(rs, machine);
//
//                            } else if (typeCode.equals(CODE1.RECHARGE.getCode())) {
//
//                                /***
//                                 * 0x00 收到指令并执行状态切换
//                                 * 0x01 收到指令,或处于保护状态，或指令数据字段有误，不接受状态切换
//                                 */
//                                String dataMsg = rs.substring(16, 18);
//
//                                if (dataMsg.equals("00")) {
//
//
//                                } else if (dataMsg.equals("01")) {
//
//                                }
//                            }
//
//
////                            webSocket.sendMessageAll(com.alibaba.fastjson.JSONObject.toJSONString(monitorData));
////                            log.info(" 成功插入监控数据==========:" + data);
//                        } catch (Exception e) {
//                            log.info(" udp监控数据异常==========:");
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    log.info(" udp启动异常==========:");
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//    }

    /**
     * 根据udp数据获得类型
     *
     * @param data
     * @return
     */
 /*   public static String getudpDataTypeFromData(String data) {
        String typeCode = data.substring(14, 16);
        return typeCode;
    }*/

    /**
     * 检查检测数据是否越界或者超过阈值
     */
/*    private void checkModuleData(MonitorData monitorData) {
        String number = monitorData.getSerialNumber();
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
                //自动选择维修人员
                //  workOrder.setUserId(autoMatchRepairUserByFaultCode(warningCode).getId());
                workOrder.setStart(new Date());
                workOrderMapper.insert(workOrder);
            }
        }
    }*/


    /**
     * 根据维修类型自动匹配维修人员
     *
     * @param userId
     * @return
     */
//    public SystemUser autoMatchRepairUserByFaultCode(String faultCode) {
//        Example example = new Example(WorkOrder.class);
//        example.createCriteria().andEqualTo("faultCode", faultCode).andEqualTo("status", WorkOrder.CODE1.REPAIRING);
//        List<WorkOrder> repairing = workOrderMapper.selectByExample(example);
//        List<Repair2User> repair2Users = getUserByFaultCode(faultCode);
//        Long userId = null;
//        List<Repair2User> busy = new ArrayList<>();
//        if (repair2Users != null && repair2Users.size() > 0) {
//            for (Repair2User repair2User : repair2Users) {
//                if (repairing == null || repairing.size() == 0) {
//                    return userMapper.selectByPrimaryKey(repair2Users.get(0).getUserId());
//                } else {
//                    for (WorkOrder workOrder : repairing) {
//                        if (repair2User.getUserId() == workOrder.getUserId()) {
//                            busy.add(repair2User);
//                        }
//                    }
//                }
//            }
//            //获得空闲工程师
//            repair2Users.removeAll(busy);
//            //没有空闲工程师 ，则随机挑选一个，否则取第一个空闲工程师
//            if (repair2Users == null) {
//                int size = repair2Users.size();
//                Random random = new Random(size);
//                int randomid = random.nextInt();
//                return userMapper.selectByPrimaryKey(repair2Users.get(randomid));
//            } else {
//                return userMapper.selectByPrimaryKey(repair2Users.get(0));
//            }
//        }
//        return null;
//    }


    /**
     * 根据故障寻找专家
     *
     * @param faultCode
     * @return
     */
//    public List<Repair2User> getUserByFaultCode(String faultCode) {
//        Example example = new Example(Repair2User.class);
//        example.createCriteria().andEqualTo("repairCode", faultCode);
//        List<Repair2User> repair2Users = repair2UserMapper.selectByExample(example);
//        return repair2Users;
//    }

//
//    @Autowired
//    private Repair2UserMapper repair2UserMapper;

//    @Value("${monitor.maxBreak}")
//    private int maxBreak;

    /**
     * 机器人枚举
     */
//    public enum CODE1 {
//        QOERY_ROBOT_POSE("00", "查询机器人位置"),
//        STANDBY("01", ""),
//        PATROL("02", ""),
//        RECHARGE("03", ""),
//        DOOR_IS_OPEN("04", ""),
//        QUERY_STATE("05", ""),
//        CTRL_LIFTER_HEIGHT("10", "控制升降高度指令"),
//        QUERY_LIFTER_HEIGHT("11", "查询升降高度指令"),
//        QUERY_AIRE_CONDITION("20", "查询空气指标"),
//        QUERY_POWER("30", ""),
//        QUERY_WIRELESS_STATUS("31", ""),
//        ALL_DATA("40", "查询机器人当前所有状态数据");
//
//        /**
//         * 查询码  16进制
//         */
//        private String searchCode;
//        private String msg;
//
//        CODE1(String s, String i) {
//            this.searchCode = s;
//            this.msg = i;
//        }
//
//        public String getCode() {
//            return this.searchCode;
//        }
//
//        public String getMsg() {
//            return this.msg;
//        }
//    }


//    public static Map<String, Date> map = new HashMap();
//    @Resource
//    private WebSocket webSocket;

    /**
     * 心跳检测
     */
    //@Scheduled(cron = "0/20 * * * * *")
//    @Transactional
//    public void heartMonitor() throws IOException {
//        List<MonitorDataMapper.Data> datas = monitorDataMapper.selectLatestDataTimeStamp();
//        log.info(" 心跳检测  当前在线机器人：" + datas.size());
//        //项目启动首次触发定时任务，此时可以初始化各个机器人最新的报文时间
//        if (map.size() == 0) {
//            for (MonitorDataMapper.Data data : datas) {
//                Long timeStamp = data.getMaxTime();
//                Date newDate = timeStamp == null ? null : new Date(data.getMaxTime());
//                String number = data.getSerialNumber();
//                map.put(number, newDate);
//            }
//        } else {
//            if (datas != null && datas.size() > 0) {
//                for (MonitorDataMapper.Data data : datas) {
//                    Long timeStamp = data.getMaxTime();
//                    Date newDate = timeStamp == null ? null : new Date(data.getMaxTime());
//                    String number = data.getSerialNumber();
//                    //被停止的机器人 重新开启的情况，或者是刚加入的机器人，下次再判断心跳
//                    Set set = map.keySet();
//                    if (!set.contains(number)) {
//                        map.put(number, newDate);
//                        continue;
//                    }
//                    Date oldTime = map.get(number);
//                    Warning warning = new Warning();
//                    warning.setType(Warning.CODE1.LOSE_DATA.getCode());
//                    warning.setSerialNumber(number);
//                    warning.setCreateTime(new Date());
//                    warning.setUpdateTime(new Date());
//                    warning.setEnable(BaseEntity.ENABLE.ENABLE);
//                    if (oldTime == null && newDate == null || newDate == null || (oldTime != null && newDate != null && oldTime.equals(newDate))
//                            || (oldTime != null && newDate != null && oldTime.after(newDate))
//                    ) {
//                        log.info("warning#未接收到编号为" + number + "机器人检测数据");
//                        if (newDate != null) {
//                            webSocket.sendMessageAll("warning#未接收到编号为" + number + "机器人检测数,上次最新数据时间:" + TwcTimeUtil.reverseTime2String(newDate));
//                            warning.setConent(number + "#" + Warning.CODE1.LOSE_DATA.getMsg() + "#历史最新数据时间:" + TwcTimeUtil.reverseTime2String(newDate));
//                        } else {
//                            webSocket.sendMessageAll("warning#未接收到编号为" + number + "机器人检测数据,历史数据为空");
//                            warning.setConent(number + "#" + Warning.CODE1.LOSE_DATA.getMsg() + "#历史数据为空");
//                        }
//                        warningMapper.insert(warning);
//                    } else if ((oldTime == null && newDate != null) || newDate.after(oldTime)) {
//                        map.put(number, newDate);
//                    }
//                }
//            }
//        }
//    }

    /**
     * 获得随机数
     *
     * @return
     */
//    public static double getRandom() {
//        Random random = new Random();
//        DecimalFormat df = new DecimalFormat("0.00");
//        Double d = random.nextDouble();
//        return Double.parseDouble(df.format(d));
//    }


    public static void main(String[] s) {

//        StringBuffer stringBuffer = new StringBuffer();
//        String ttr = "00000000babssdbbfwe13466";
//        int index = -1;
//        for (int i = 0; i < ttr.length(); i++) {
//
//            if (ttr.charAt(i) != '0') {
//
//                ttr = ttr.substring(i, ttr.length());
//                break;
//            }
//        }
//
//        System.out.println(ttr.length());
//
//        System.out.println(ttr.indexOf("bb") + "-----------" + ttr.lastIndexOf("66"));
//
//        System.out.println(ttr.substring(ttr.length() - 4, ttr.length() - 2));

//        开头：BB
//        长度：length/A0 00
//        时间戳:00 00 00 00
//        数据类型:00
//        数据部分：
//        postion/10 20 30 40 10 20 30 40 10 20 30 40                     1-12字节，表示机器人位置
//        state/10                                                        第13字节，表示机器人状态
//        heigth/30                                                       第14字节，表示升降高度
//        air/00 01 02 03 04 00 01 02 03 04 00 01 02 03 04 00 01 02 03    第15—34字节，表示空气传感器数据
//        dumpEnergy/10                                                   第35、36字节，表示电量和无线充电状态
//        wirelessChargingState/10
//        校验位:A0
//        结束位:66
        String rs = " bb a0 00     10 00 00 00    40     CD CC 8C 3F   CD CC 8C 3F   CD CC 8C 3F     10      30    00 01 02 03 04 00 01 02 03 04 00 01 02 03 04 00 01 02 03 ff    10   10   A0  66";
        rs = rs.replace(" ", "");
        String lengthStr = rs.substring(2, 6);
        String timeStampStr = rs.substring(6, 14);
        String typeCodeStr = rs.substring(14, 16);
        String positionStr = rs.substring(16, 40);
        String stateStr = rs.substring(40, 40 + 2);
        String heigthStr = rs.substring(42, 42 + 2);
        String airStr = rs.substring(44, 44 + 40);
        String dumpEnergyStr = rs.substring(84, 84 + 2);
        String wirelessChargingStateStr = rs.substring(86, 86 + 2);
        String checkStr = rs.substring(88, 88 + 2);
        String withOutCheck = rs.substring(0, 88) + rs.substring(90, rs.length());

        float x = UdpUtil.hexStr2foloatBylittle(positionStr.substring(0, 8));
        float y = UdpUtil.hexStr2foloatBylittle(positionStr.substring(8, 16));
        float theta = UdpUtil.hexStr2foloatBylittle(positionStr.substring(16, 24));

        System.out.println(x + "--" + "y---theta" + theta);

        System.out.print("air:" + airStr + "\noriginalCode:" + checkStr + "\nwithOutCheck:" + withOutCheck + "\ncheckCode:" + UdpUtil.getCheckCodeByhexByte(withOutCheck));
    }

    /**
     * 解析机器人上报数据
     *
     * 更新机器人位置
     * 更新机器人升降机高度
     * 获取空气质量数据
     * 更新机器人状态
     * 更新机器人剩余电量
     * 更新机器人无线充电状态
     *
     * @param rs
     */
//    public void resolveMachine0x40Data(String rs, Machine machine) {
//        //String rs = " bb a0 00     10 00 00 00    40     CD CC 8C 3F   CD CC 8C 3F   CD CC 8C 3F     10      30    00 01 02 03 04 00 01 02 03 04 00 01 02 03 04 00 01 02 03 ff    10   10   A0  66";
//        rs = rs.replace(" ", "");
//        String lengthStr = rs.substring(2, 6);
//        String timeStampStr = rs.substring(6, 14);
//        String typeCodeStr = rs.substring(14, 16);
//        String positionStr = rs.substring(16, 40);
//        String stateStr = rs.substring(40, 40 + 2);
//        String heigthStr = rs.substring(42, 42 + 2);
//        String airStr = rs.substring(44, 44 + 40);
//        String dumpEnergyStr = rs.substring(84, 84 + 2);
//        String butteryTempStr = rs.substring(86, 94);
//        String wirelessChargingStateStr = rs.substring(94, 94 + 2);
//        String checkStr = rs.substring(96, 96 + 2);
//        String withOutCheck = rs.substring(0, 88 + 8) + rs.substring(90 + 8, rs.length());
//        String checkCode = UdpUtil.getCheckCodeByhexByte(withOutCheck.toUpperCase());
//
////        log.info(checkStr + "   ：  校验码比对   ：" + checkCode);
////        log.info(" 机器人状态切换为   ：" + stateStr);
////        log.info(" 机器人充电状态切换为   ：" + wirelessChargingStateStr);
//
//        float x = UdpUtil.hexStr2foloatBylittle(positionStr.substring(0, 8));
//        float y = UdpUtil.hexStr2foloatBylittle(positionStr.substring(8, 16));
//        float theta = UdpUtil.hexStr2foloatBylittle(positionStr.substring(16, 24));
//        float bettryTemp = UdpUtil.hexStr2ShortBylittle(butteryTempStr);
//
//        MachineWebScoketData machineWebScoketData = new MachineWebScoketData();
//        machineWebScoketData.setX(x);
//        machineWebScoketData.setY(y);
//        machineWebScoketData.setTheta(theta);
//
//        short pm1 = UdpUtil.hexStr2ShortBylittle(airStr.substring(0, 4));
//        short pm25 = UdpUtil.hexStr2ShortBylittle(airStr.substring(4, 8));
//        short pm10 = UdpUtil.hexStr2ShortBylittle(airStr.substring(8, 12));
//        short smoke = UdpUtil.hexStr2ShortBylittle(airStr.substring(12, 16));
//        short ch4 = UdpUtil.hexStr2ShortBylittle(airStr.substring(16, 20));
//        short co = UdpUtil.hexStr2ShortBylittle(airStr.substring(20, 24));
//        short h2s = UdpUtil.hexStr2ShortBylittle(airStr.substring(24, 28));
//        short o2 = UdpUtil.hexStr2ShortBylittle(airStr.substring(28, 32));
//        short temprature = UdpUtil.hexStr2ShortBylittle(airStr.substring(32, 36));
//        short hum = UdpUtil.hexStr2ShortBylittle(airStr.substring(36, 40));
//
//        machineWebScoketData.setPm1(pm1);
//        machineWebScoketData.setPm25(pm25);
//        machineWebScoketData.setPm10(pm10);
//        machineWebScoketData.setSmoke(smoke);
//        machineWebScoketData.setCo(co);
//        machineWebScoketData.setO2(o2);
//        machineWebScoketData.setHumidity(hum);
//        machineWebScoketData.setHydrothion(h2s);
//        machineWebScoketData.setMethance(ch4);
//        machineWebScoketData.setEnergy(UdpUtil.hex2decimal(dumpEnergyStr));
//        machineWebScoketData.setHeight(UdpUtil.hex2decimal(heigthStr));
//        machineWebScoketData.setStatus(stateStr);
//
//        MonitorData monitorData = new MonitorData();
//        monitorData.setButteryTemp(bettryTemp + "");
//        monitorData.setX(x + "");
//        monitorData.setY(y + "");
//        monitorData.setTheta(theta + "");
//        monitorData.setCo(co + "");
//        monitorData.setOxygen(o2 + "");
//        monitorData.setHumidity(hum + "");
//        monitorData.setHydrothion(h2s + "");
//        monitorData.setMethance(ch4 + "");
//        monitorData.setPm1(pm1 + "");
//        monitorData.setPm25(pm25 + "");
//        monitorData.setPm10(pm10 + "");
//        monitorData.setTemperature(temprature + "");
//        monitorData.setSmoke(smoke + "");
//        monitorData.setCreateTime(new Date());
//        monitorData.setEnable(BaseEntity.ENABLE.ENABLE);
//        monitorData.setMachineId(machine.getId());
//        checkModuleData(monitorData);
//        monitorDataMapper.insert(monitorData);
//        /**
//         *  请求充电桩关门：
//         *  1 机器人成功改变状态为recharging并且正在充电
//         *  2 机器人进入patrolling状态
//         *
//         */
//        if ((stateStr.equalsIgnoreCase(com.twc.guanlang.entity.machine.Machine.CODE1.RECHARGING.getCode())
//                && !wirelessChargingStateStr.equalsIgnoreCase(Machine.WireLessRechargeStatus.UNRECHARGE)) ||
//                stateStr.equalsIgnoreCase(Machine.CODE1.PATROLLING.getCode())
//        ) {
//            Long rechargeId = MachineService.machineRechargeCallOpenMap.get(machine.getId());
//            CloseDoorParam closeDoorParam = new CloseDoorParam();
//            closeDoorParam.setId(rechargeId);
//            try {
//                rechargeService.closeDoor(closeDoorParam);
//                //如果机器人成功进入patrolling状态则清除机器人和充电桩的关联关系
//                if (stateStr.equalsIgnoreCase(Machine.CODE1.PATROLLING.getCode())) {
//                    MachineService.machineRechargeCallOpenMap.remove(machine.getId());
//                }
//            } catch (CustomException e) {
//                log.error(" 申请关门失败:" + e.getMsg());
//            }
//        }
//        try {
//            MachineWebScoket machineWebScoket = new MachineWebScoket();
//            machineWebScoket.setType("data");
//            machineWebScoket.setMachineWebScoketData(machineWebScoketData);
//            webSocket.sendMessageAll(JSONObject.toJSONString(machineWebScoket));
//        } catch (IOException e) {
//            log.error(" websocket数据发送失败： " + e.getMessage());
//        }
//    }
//    @Autowired
//    private RechargeService rechargeService;
//
//    @Autowired
//    private MachineMapper machineMapper;
}

