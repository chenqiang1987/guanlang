package com.twc.guanlang.service;


import com.alibaba.fastjson.JSONObject;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.maintain.WorkOrder;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.*;

import com.twc.guanlang.param.machine.*;
import com.twc.guanlang.service.udp.SendDemoNew;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.service.ws.WebSocket;
import com.twc.guanlang.vo.MachineWebScoket;
import com.twc.guanlang.vo.MachineWebScoketData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class MachineService {

    /**
     * 由于udp的单向性质，使用map
     * 模拟Http的形式
     * <p>
     * uuid作为请求的标识，如果在10秒内没有收到udp的响应，则认为此次udp消息发送失败，触发再次重试
     * 重试3次，如果依然失败则计入数据库记录失败日志。
     */
    public static Map simulationHttpReqAndResp = new HashMap();
    public static Map<String, Long> machineHeartMap = new HashMap();
    /**
     * 机器人当前工作状态
     */
    public static String WORK_STATUS = null;
    /**
     * 监听充电请求
     */
    public static Map<String, String> rechargeMap = new HashMap();
    @Value("${machine.udp.port}")
    private String port;
    //记录机器人请求充电桩开门
    public static Map<Long, Long> machineRechargeCallOpenMap = new HashMap<>();
    //记录机器人请求充电桩关门
    public static Map<Long, Long> machineRechargeCallCloseMap = new HashMap<>();
    public static Map<Long, Long> heartMap = new HashMap();
    public static Map<Long, String> onLineMap = new HashMap();
    public static Map<Long, MachineWebScoketData> heartData = new HashMap();

    public enum CODE1 {
        QOERY_ROBOT_POSE("00", "查询机器人位置"),
        STANDBY("01", "进入standby"),
        PATROL("02", "进入partol"),
        RECHARGE("03", "进入充电桩"),
        DOOR_IS_OPEN("04", ""),
        QUERY_STATE("05", ""),

        INIT_NUMBER("06", "初始位置编号通知"),

        CTRL_LIFTER_HEIGHT("10", "控制升降高度指令"),
        QUERY_LIFTER_HEIGHT("11", "查询升降高度指令"),
        QUERY_AIRE_CONDITION("20", "查询空气指标"),
        QUERY_POWER("30", ""),
        QUERY_WIRELESS_STATUS("31", ""),
        ALL_DATA("40", "查询机器人当前所有状态数据");

        /**
         * 查询码  16进制
         */
        private String searchCode;
        private String msg;

        CODE1(String s, String i) {
            this.searchCode = s;
            this.msg = i;
        }

        public String getCode() {
            return this.searchCode;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    @Autowired
    private MachineRechargeMapper machineRechargeMapper;
    @Autowired
    private AutoMessionMapper autoMessionMapper;
    @Resource
    private WarningMapper warningMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private UdpDataMapper errorUdpDataMapper;
    @Autowired
    private UdpCommandMachineMapper udpCommandMachineMapper;
    @Autowired
    private UdpCommandRechargeMapper udpCommandRechargeMapper;
    @Autowired
    private RechargeMapper rechargeMapper;
    @Resource
    private MachineMapper machineMapper;
    @Autowired
    private Module2MachineMapper module2MachineMapper;

    public void start(StartParam startParam) throws CustomException {

//        Example example = new Example(Machine.class);
//        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("serialNumber", startParam.getNumber());
//        Machine machine = machineMapper.selectOneByExample(example);
//        if (machine == null)
//            throw new CustomException("机器人不存在");
////        machine.setStatus(Machine.S);
//        machineMapper.updateByPrimaryKey(machine);
//
//        MachineUdpService.map.remove(startParam.getNumber());
    }
    public void stop(StopParam stopParam) throws CustomException {

        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("serialNumber", stopParam.getNumber());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");
//        machine.setStatus(Machine.STATUS.StandingBy);
        machineMapper.updateByPrimaryKey(machine);
    }


    /**
     * 获取机器人列表
     * @return
     */
    public List getMachineList() {
//        Page<Machine> page = PageHelper.startPage(machinePageParam.getPageNum(), machinePageParam.getPageSize(), "update_time");
        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE);
        example.orderBy("isDefault").desc();
        return machineMapper.selectByExample(example);
    }



    /**
     * 通知机器人门状态改变
     */
    public static void noticeMachineDoorStateChange(Machine machine, String doorState) throws CustomException {

        if (!doorState.equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.OPENED.getCode())) return;
        String timestampStr = UdpUtil.machineUdpTimeStamp();
        StringBuffer udpTemplate = UdpUtil.machineUdpSendTemlate();
        String udpStr = udpTemplate.toString();
        //充电指令10个字节 oa00
        udpStr = udpStr.replace("length", "0a00");
        udpStr = udpStr.replace("timeStamp", timestampStr);
        udpStr = udpStr.replace("type", MachineService.CODE1.DOOR_IS_OPEN.getCode());
        udpStr = udpStr.replace("data", "");
        String udpTemp = udpStr;
        //除去check做校验运算
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        udpTemp = udpTemp.replace("check", checkCode).toUpperCase();
        try {
            UdpUtil.sendUdpOrderAndWaitReceive(udpTemp, machine, 24102);
        } catch (Exception e) {

            throw new CustomException("通知机器人充电桩门打开发生异常:" + e.getMessage());
        }
    }

    /**
     * 请求机器人进入recharging状态
     *
     * @param startParam
     * @throws CustomException
     * @throws IOException
     */
    @Transactional
    public void callRecharge(RechargeParam startParam) throws CustomException, IOException, InterruptedException {

//        Example example = new Example(MachineRecharge.class);
//        example.createCriteria().andEqualTo("rechargeId", startParam.getRechrgeId())
//                .andEqualTo("machineId", startParam.getMachineId()).andNotEqualTo("status", MachineRecharge.);

        String machineOnline = MachineService.onLineMap.get(startParam.getMachineId());
        if (null == machineOnline || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }

//        String rechargeOnline = RechargeUdpService.onLineMap.get(startParam.getRechrgeId());
//        if ((null == rechargeOnline || rechargeOnline.equalsIgnoreCase("off"))) {
//            throw new CustomException("充电桩离线,请稍后重试!");
//        }


        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getMachineId());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");

        Example rechargeExample = new Example(Recharge.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getRechrgeId());
        Recharge recharge = rechargeMapper.selectOneByExample(rechargeExample);
        if (recharge == null)
            throw new CustomException("充电桩编号不存在");
        if (startParam.getRechrgeId() > 15l) {
            throw new CustomException("充电桩id不能大于15");
        }

        String timestampStr = UdpUtil.machineUdpTimeStamp();
        StringBuffer udpTemplate = UdpUtil.machineUdpSendTemlate();
        String udpStr = udpTemplate.toString();

        //充电指令11个字节 ob00
        udpStr = udpStr.replace("length", "0b00");
        udpStr = udpStr.replace("timeStamp", timestampStr);
        udpStr = udpStr.replace("type", MachineService.CODE1.RECHARGE.getCode());

        //data为充电桩编号
        Long rechargeId = startParam.getRechrgeId();
        String hexStr = UdpUtil.intToHex(rechargeId.intValue());
        hexStr = hexStr.length() == 1 ? "0" + hexStr : hexStr;
        udpStr = udpStr.replace("data", hexStr);
        String udpTemp = udpStr;
        //除去check做校验运算
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        udpTemp = udpTemp.replace("check", checkCode).toUpperCase();

        log.info("  机器人指令：" + udpTemp);

        UdpUtil.sendMachineUdp(udpTemp, machine.getIp(), machine.getPort());
        UdpCommandMachine udpCommandMachine = new UdpCommandMachine();
        udpCommandMachine.setTimeStamp(Long.parseLong(timestampStr));
        udpCommandMachine.setCreateTime(new Date());
        udpCommandMachine.setUpdateTime(new Date());
        udpCommandMachine.setCommand(udpTemp);
        udpCommandMachineMapper.insert(udpCommandMachine);
        machineRechargeCallOpenMap.put(recharge.getId(), machine.getId().longValue());

        //通知充电桩开门
        StringBuffer rechargeUdpSendBuffer = UdpUtil.rechargeUdpSendTemlate();
        String type = "9" + UdpUtil.intToHex(startParam.getRechrgeId().intValue());
        String rechargeUdpSendStr = rechargeUdpSendBuffer.toString();
        //01-开门
        rechargeUdpSendStr.replace("data", "01");
        String check = UdpUtil.makeChecksum("03" + type + 01);
        String rechargeUdpStr = rechargeUdpSendStr.replace("check", check);
        UdpUtil.sendRechargeUdp(rechargeUdpStr, recharge.getIp(), recharge.getPort());

        UdpCommandRecharge udpCommandRecharge = new UdpCommandRecharge();
        udpCommandRecharge.setTimeStamp(Long.parseLong(timestampStr));
        udpCommandRecharge.setCreateTime(new Date());
        udpCommandRecharge.setUpdateTime(new Date());
        udpCommandRecharge.setCommand(rechargeUdpStr);
        udpCommandRechargeMapper.insert(udpCommandRecharge);
    }


    /**
     * 请求机器人进入standby状态
     *
     * @param stopParam
     * @throws CustomException
     * @throws IOException
     */
    public void callStandby(StandbyParam stopParam) throws CustomException {

        MachineWebScoketData machineWebScoketData = MachineService.heartData.get(stopParam.getId());
        String machineOnline = MachineService.onLineMap.get(stopParam.getId());
        if (null == machineOnline || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }

        Machine machine = machineMapper.selectByPrimaryKey(stopParam.getId());
        if (machine == null)
            throw new CustomException("机器人不存在");
        String time = UdpUtil.machineUdpTimeStamp();
        StringBuffer template = UdpUtil.machineUdpSendTemlate();
        String udpStr = template.toString();
        udpStr = udpStr.replace("length", "0a00");
        udpStr = udpStr.replace("timeStamp", time);
        udpStr = udpStr.replace("type", MachineService.CODE1.STANDBY.getCode());
        udpStr = udpStr.replace("data", "");
        String tempStr = udpStr;
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        tempStr = tempStr.replace("check", checkCode);

        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(tempStr, machine, 24102);
        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        saveCommand(machine, Long.parseLong(time), tempStr);
        if (dataStr.equalsIgnoreCase("01")) {
            throw new CustomException("当前机器人处于保护状态,请稍后重试!");
        } else if (dataStr.equalsIgnoreCase("00")) {
            /**
             * 全局标记机器人进入半自动或者手动
             */
            if (stopParam.getHalfOrHand().equalsIgnoreCase(Machine.WORK_STATUS.HALF_WORKING.getCode())) {
                MachineService.WORK_STATUS = Machine.WORK_STATUS.HALF_WORKING.getCode();
            } else if (stopParam.getHalfOrHand().equalsIgnoreCase(Machine.WORK_STATUS.HAND_WORKING.getCode())) {
                MachineService.WORK_STATUS = Machine.WORK_STATUS.HAND_WORKING.getCode();
            } else if (StringUtil.isEmpty(stopParam.getHalfOrHand())) {
                MachineService.WORK_STATUS = Machine.WORK_STATUS.HOLDING.getCode();
            }

        }
    }


    public void sendInitNumber12(RechargeParam startParam) throws CustomException, IOException {

//        Example example = new Example(MachineRecharge.class);
//        example.createCriteria().andEqualTo("rechargeId", startParam.getRechrgeId())
//                .andEqualTo("machineId", startParam.getMachineId()).andNotEqualTo("status", MachineRecharge.);

        String machineOnline = MachineService.onLineMap.get(startParam.getMachineId());
        if (null == machineOnline || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }

//        String rechargeOnline = RechargeUdpService.onLineMap.get(startParam.getRechrgeId());
//        if ((null == rechargeOnline || rechargeOnline.equalsIgnoreCase("off"))) {
//            throw new CustomException("充电桩离线,请稍后重试!");
//        }


        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getMachineId());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");

//        Example rechargeExample = new Example(Recharge.class);
//        rechargeExample.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getRechrgeId());
//        Recharge recharge = rechargeMapper.selectOneByExample(rechargeExample);
//        if (recharge == null)
//            throw new CustomException("充电桩编号不存在");
//        if (startParam.getRechrgeId() > 15l) {
//            throw new CustomException("充电桩id不能大于15");
//        }

        String timestampStr = UdpUtil.machineUdpTimeStamp();
        StringBuffer udpTemplate = UdpUtil.machineUdpSendTemlate();
        String udpStr = udpTemplate.toString();

        //充电指令11个字节 ob00
        udpStr = udpStr.replace("length", "0b00");
        udpStr = udpStr.replace("timeStamp", timestampStr);
        udpStr = udpStr.replace("type", CODE1.INIT_NUMBER.getCode());

        //data为充电桩编号
        int number=0;


        Long rechargeId = startParam.getRechrgeId();

//        if(rechargeId.intValue()==1)
//            number=3;
//        if(rechargeId.intValue()==2)
//            number=1;
//        if(rechargeId.intValue()==3)
//            number=2;

        String hexStr = UdpUtil.intToHex(rechargeId.intValue());
        hexStr = hexStr.length() == 1 ? "0" + hexStr : hexStr;
        udpStr = udpStr.replace("data", hexStr);
        String udpTemp = udpStr;
        //除去check做校验运算
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        udpTemp = udpTemp.replace("check", checkCode).toUpperCase();

        log.info("  机器人指令：" + udpTemp);

        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(udpTemp, machine, 24102);


//        UdpUtil.sendMachineUdp(udpTemp, machine.getIp(), machine.getPort());
        UdpCommandMachine udpCommandMachine = new UdpCommandMachine();
        udpCommandMachine.setTimeStamp(Long.parseLong(timestampStr));
        udpCommandMachine.setCreateTime(new Date());
        udpCommandMachine.setUpdateTime(new Date());
        udpCommandMachine.setCommand(udpTemp);
        udpCommandMachineMapper.insert(udpCommandMachine);
//        machineRechargeCallOpenMap.put(recharge.getId(), machine.getId().longValue());

        machine.setInitRechargeId(rechargeId.intValue());
        //更新機器人的起始位置
        machineMapper.updateByPrimaryKey(machine);

        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        if (dataStr.equalsIgnoreCase("01")) {
            throw new CustomException("当前机器人处于保护状态,请稍后重试!");
        }

//        //通知充电桩开门
//        StringBuffer rechargeUdpSendBuffer = UdpUtil.rechargeUdpSendTemlate();
//        String type = "9" + UdpUtil.intToHex(startParam.getRechrgeId().intValue());
//        String rechargeUdpSendStr = rechargeUdpSendBuffer.toString();
//        //01-开门
//        rechargeUdpSendStr.replace("data", "01");
//        String check = UdpUtil.makeChecksum("03" + type + 01);
//        String rechargeUdpStr = rechargeUdpSendStr.replace("check", check);
//        UdpUtil.sendRechargeUdp(rechargeUdpStr, recharge.getIp(), recharge.getPort());
//
//        UdpCommandRecharge udpCommandRecharge = new UdpCommandRecharge();
//        udpCommandRecharge.setTimeStamp(Long.parseLong(timestampStr));
//        udpCommandRecharge.setCreateTime(new Date());
//        udpCommandRecharge.setUpdateTime(new Date());
//        udpCommandRecharge.setCommand(rechargeUdpStr);
//        udpCommandRechargeMapper.insert(udpCommandRecharge);
    }


    /**
     * 发送初始充电桩编号  虚拟位置 12  离中间充电桩12米
     *
     * @param stopParam
     * @throws CustomException
     * @throws IOException
     */
    public void sendInitNumber(RechargeParam startParam) throws CustomException, IOException {

//        Example example = new Example(MachineRecharge.class);
//        example.createCriteria().andEqualTo("rechargeId", startParam.getRechrgeId())
//                .andEqualTo("machineId", startParam.getMachineId()).andNotEqualTo("status", MachineRecharge.);

        String machineOnline = MachineService.onLineMap.get(startParam.getMachineId());
        if (null == machineOnline || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }

//        String rechargeOnline = RechargeUdpService.onLineMap.get(startParam.getRechrgeId());
//        if ((null == rechargeOnline || rechargeOnline.equalsIgnoreCase("off"))) {
//            throw new CustomException("充电桩离线,请稍后重试!");
//        }


        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getMachineId());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");

        Example rechargeExample = new Example(Recharge.class);
        rechargeExample.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("id", startParam.getRechrgeId());
        Recharge recharge = rechargeMapper.selectOneByExample(rechargeExample);
        if (recharge == null)
            throw new CustomException("充电桩编号不存在");
        if (startParam.getRechrgeId() > 15l) {
            throw new CustomException("充电桩id不能大于15");
        }

        String timestampStr = UdpUtil.machineUdpTimeStamp();
        StringBuffer udpTemplate = UdpUtil.machineUdpSendTemlate();
        String udpStr = udpTemplate.toString();

        //充电指令11个字节 ob00
        udpStr = udpStr.replace("length", "0b00");
        udpStr = udpStr.replace("timeStamp", timestampStr);
        udpStr = udpStr.replace("type", CODE1.INIT_NUMBER.getCode());

        //data为充电桩编号
        int number=0;


        Long rechargeId = startParam.getRechrgeId();

        if(rechargeId.intValue()==1)
            number=3;
        if(rechargeId.intValue()==2)
            number=1;
        if(rechargeId.intValue()==3)
            number=2;

        String hexStr = UdpUtil.intToHex(number);
        hexStr = hexStr.length() == 1 ? "0" + hexStr : hexStr;
        udpStr = udpStr.replace("data", hexStr);
        String udpTemp = udpStr;
        //除去check做校验运算
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        udpTemp = udpTemp.replace("check", checkCode).toUpperCase();

        log.info("  机器人指令：" + udpTemp);

        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(udpTemp, machine, 24102);


//        UdpUtil.sendMachineUdp(udpTemp, machine.getIp(), machine.getPort());
        UdpCommandMachine udpCommandMachine = new UdpCommandMachine();
        udpCommandMachine.setTimeStamp(Long.parseLong(timestampStr));
        udpCommandMachine.setCreateTime(new Date());
        udpCommandMachine.setUpdateTime(new Date());
        udpCommandMachine.setCommand(udpTemp);
        udpCommandMachineMapper.insert(udpCommandMachine);
        machineRechargeCallOpenMap.put(recharge.getId(), machine.getId().longValue());

        machine.setInitRechargeId(rechargeId.intValue());
        //更新機器人的起始位置
        machineMapper.updateByPrimaryKey(machine);

        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        if (dataStr.equalsIgnoreCase("01")) {
            throw new CustomException("当前机器人处于保护状态,请稍后重试!");
        }

//        //通知充电桩开门
//        StringBuffer rechargeUdpSendBuffer = UdpUtil.rechargeUdpSendTemlate();
//        String type = "9" + UdpUtil.intToHex(startParam.getRechrgeId().intValue());
//        String rechargeUdpSendStr = rechargeUdpSendBuffer.toString();
//        //01-开门
//        rechargeUdpSendStr.replace("data", "01");
//        String check = UdpUtil.makeChecksum("03" + type + 01);
//        String rechargeUdpStr = rechargeUdpSendStr.replace("check", check);
//        UdpUtil.sendRechargeUdp(rechargeUdpStr, recharge.getIp(), recharge.getPort());
//
//        UdpCommandRecharge udpCommandRecharge = new UdpCommandRecharge();
//        udpCommandRecharge.setTimeStamp(Long.parseLong(timestampStr));
//        udpCommandRecharge.setCreateTime(new Date());
//        udpCommandRecharge.setUpdateTime(new Date());
//        udpCommandRecharge.setCommand(rechargeUdpStr);
//        udpCommandRechargeMapper.insert(udpCommandRecharge);
    }

    /**
     * 保存用户发起的指令
     *
     * @param machine
     * @param time
     * @param order
     */
    public void saveCommand(Machine machine, Long time, String order) {
        UdpCommandMachine udpCommandMachine = new UdpCommandMachine();
        udpCommandMachine.setTimeStamp(time);
        udpCommandMachine.setCreateTime(new Date());
        udpCommandMachine.setUpdateTime(new Date());
        udpCommandMachine.setIp(machine.getIp());
        udpCommandMachine.setPort(machine.getPort());
        udpCommandMachine.setCommand(order);
        udpCommandMachineMapper.insert(udpCommandMachine);
    }








    /**
     * 请求机器人进入自动巡检
     *
     * @param stopParam
     * @throws CustomException
     * @throws IOException
     */
    public void callPatrol(StandbyParam stopParam) throws CustomException, IOException, InterruptedException {
        String machineOnline = MachineService.onLineMap.get(stopParam.getId());


        /**
         * patrol前确保所有的充电桩门打开
         *
         * 如果有充电桩门无法打开  则patrol无法执行
         */
        if (machineOnline == null || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }
        Machine machine = machineMapper.selectByPrimaryKey(stopParam.getId());
        if (machine == null)
            throw new CustomException("机器人不存在");

        MachineWebScoketData machineWebScoketData = MachineService.heartData.get(stopParam.getId());
        String stateName = machineWebScoketData.getStatus();

        /**
         * 机器人处于初始状态  先设置初始编号
         */
        if(stateName.equalsIgnoreCase(Machine.CODE1.INITIAL_FAILED.getMsg())){

            throw new CustomException("机器人处于初始状态  先设置初始编号！");
        }
        List<Recharge>  list=rechargeMapper.selectAll();
       // RechargeService.openAll(list);
       // Thread.sleep(30*1000);
        Map<Long, String> onLineMap =RechargeUdpService.onLineMap;
       for(Recharge recharge:list){
           if(onLineMap.get(recharge.getId()).equalsIgnoreCase("off")){

           }else {
               String rechargeDoorState=RechargeUdpService.rechargeDoorState.get(recharge.getId().intValue());
               if(rechargeDoorState.equalsIgnoreCase("01")){
                   throw new CustomException(recharge.getLocation()+"-充电桩门无法打开，自动任务执行失败!");
               }
           }
       }


        if(stateName.equalsIgnoreCase(Machine.CODE1.PATROLLING.getMsg()) ){
            throw new CustomException("機器人正在自動執行任務!");
        }
//        if(stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg()) ){
//            throw new CustomException("機器人正在執行充電任務!");
//        }

        String time = UdpUtil.machineUdpTimeStamp();
        StringBuffer template = UdpUtil.machineUdpSendTemlate();
        String udpStr = template.toString();
        udpStr = udpStr.replace("length", "0a00");
        udpStr = udpStr.replace("timeStamp", time);
        udpStr = udpStr.replace("type", MachineService.CODE1.PATROL.getCode());
        udpStr = udpStr.replace("data", "");
        String tempStr = udpStr;
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        tempStr = tempStr.replace("check", checkCode);

        saveCommand(machine, Long.parseLong(time), tempStr);


        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(tempStr, machine, 24102);
        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        if (dataStr.equalsIgnoreCase("01")) {
            throw new CustomException("当前机器人处于保护状态,请稍后重试!");
        } else if (dataStr.equalsIgnoreCase("00")) {

            /**
             * 全局标记机器人进入自动模式
             */
            MachineService.WORK_STATUS = Machine.WORK_STATUS.AUTO_WORKING.getCode();
        }



    }

    /**
     * 调整机器人升降机高度
     *
     * @param stopParam
     * @throws CustomException
     * @throws IOException
     */
    public void callHeight(HeightParam stopParam) throws CustomException, IOException {
        String machineOnline = MachineService.onLineMap.get(stopParam.getId());
        if (null == machineOnline || machineOnline.equalsIgnoreCase("off")) {
            throw new CustomException("机器人离线,请稍后重试!");
        }
        Machine machine = machineMapper.selectByPrimaryKey(stopParam.getId());
        if (machine == null)
            throw new CustomException("机器人不存在");

        String time = UdpUtil.machineUdpTimeStamp();
        StringBuffer template = UdpUtil.machineUdpSendTemlate();
        String udpStr = template.toString();
        udpStr = udpStr.replace("length", "0b00");
        udpStr = udpStr.replace("timeStamp", time);
        udpStr = udpStr.replace("type", MachineService.CODE1.CTRL_LIFTER_HEIGHT.getCode());
        udpStr = udpStr.replace("data", UdpUtil.intToHex(stopParam.getHeight()));
        String tempStr = udpStr;
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        tempStr = tempStr.replace("check", checkCode);
        saveCommand(machine, Long.parseLong(time), tempStr);

        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(tempStr, machine, 24102);
        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        if (dataStr.equalsIgnoreCase("01")) {
            throw new CustomException("当前机器人处于保护状态,请稍后重试!");
        } else if (dataStr.equalsIgnoreCase("00")) {

        }


    }

    public void setSection(SectionParam sectionParam) throws CustomException {

        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("serialNumber", sectionParam.getNumber());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");
        machineMapper.updateByPrimaryKey(machine);
    }

    /**
     * 保存故障信息
     * @param startParam
     * @throws CustomException
     */
    public void saveReportFalut(ReportFaultParam startParam) throws CustomException {

        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("serialNumber", startParam.getNumber());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");
//        machine.setStatus(Machine.STATUS.StandingBy);
        machineMapper.updateByPrimaryKey(machine);
//        MachineUdpService.map.remove(startParam.getNumber());
    }

    /**
     * 给机器人设置模块
     *
     * @param machineId
     * @param modules
     */
    @Transactional
    public void updateModules(Integer machineId, String modules) {

        String[] mods = modules.split(",");
        List<String> idList = Arrays.asList(mods);
        Example example = new Example(Module2Machine.class);
        example.createCriteria().andEqualTo("machineId", machineId);
        module2MachineMapper.deleteByExample(example);
        for (String mod : idList) {
            Module2Machine module2Machine = new Module2Machine();
            module2Machine.setMachineId(machineId.longValue());
            module2Machine.setModuleId(Integer.parseInt(mod));
            module2MachineMapper.insert(module2Machine);
        }
    }

    /**
     * 新增机器人
     *
     * @param number
     * @param name
     * @throws CustomException
     */
    @Transactional
    public void add(String number, String name) throws CustomException {
        Machine machine = new Machine();
        machine.setIsDefault(0);
        Module.CODE1[] arr = Module.CODE1.values();
        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("serialNumber", number);
        List list = machineMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            throw new CustomException("该机器人编号已经存在!");
        }
        machine.setName(name);
        machine.setCreateTime(new Date());
        machine.setEnable(BaseEntity.ENABLE.ENABLE);
        machine.setUpdateTime(new Date());
        machine.setSerialNumber(number);
        machineMapper.insert(machine);
        Example example1 = new Example(Module.class);
        List<Module> modules = moduleMapper.selectAll();
        List list1 = new ArrayList();
        for (Module module : modules) {
            Module2Machine module2Machine = new Module2Machine();
            module2Machine.setMachineId(machine.getId().longValue());
            module2Machine.setModuleCode(module.getType());
            module2Machine.setModuleId(module.getId());
            module2Machine.setMin(module.getMinValue());
            module2Machine.setMax(module.getMaxValue());
            module2Machine.setIsShow(1);
            module2MachineMapper.insert(module2Machine);
        }
    }

    /**
     * 设置默认机器人
     *
     * @param id
     * @throws CustomException
     */
    @Transactional
    public void setDefault(Long id) throws CustomException {

        Machine machine = machineMapper.selectByPrimaryKey(id);
        machine.setIsDefault(1);
        machineMapper.updateByPrimaryKey(machine);

        Example example = new Example(Machine.class);
        example.createCriteria().andNotEqualTo("id", id);
        List<Machine> list = machineMapper.selectByExample(example);
        for (Machine machine1 : list) {
            machine1.setIsDefault(0);
            machineMapper.updateByPrimaryKey(machine1);
        }
    }

    /**
     * 新增自动任务
     * @param machineId
     * @param start
     * @param end
     * @param desc
     * @throws CustomException
     */
    @Transactional
    public void addAutoMession(Integer machineId, String start, String end, String desc) throws CustomException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AutoMession autoMession = new AutoMession();
        try {
            autoMession.setStart(start);
            autoMession.setEnd(simpleDateFormat.parse(end));
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
            Date nowDate = new Date();
            if (startDate.before(nowDate) || endDate.before(nowDate)) {
                throw new CustomException("开始和结束时间不能小于当前时间");
            }
            if (endDate.before(startDate)) {
                throw new CustomException("结束时间不能小于开始时间");
            }
        } catch (ParseException e) {
            throw new CustomException("日期格式转换异常");
        }
        autoMession.setMachineId(machineId.longValue());
        autoMession.setContent(desc);
        autoMession.setCreateTime(new Date());
        autoMession.setUpdateTime(new Date());
        autoMessionMapper.insert(autoMession);
    }

    /**
     * 获取自动任务列表
     * @param machineId
     * @return
     */
    @Transactional
    public List<AutoMession> getAutoMessions(Integer machineId) {
        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("machineId", machineId);
        return autoMessionMapper.selectByExample(example);

    }

    /**
     * 删除自动任务
     * @param messionId
     */
    @Transactional
    public void delAutoMession(Integer messionId) {
        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("id", messionId);
        autoMessionMapper.deleteByExample(example);
    }

    @Value("${machineHostUrl}")
    private String machineVedioUrl;

    @Value("${machineVedioUrl}")
    private String machineHostUrl;

//    public static void main(String[] args) throws Exception {
////        final int captureWidth = 1280;
////        final int captureHeight = 720;
////        final FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("rtsp://admin:admin123@192.168.1.63:554/mainstream");
////        grabber.setImageWidth(captureWidth);
////        grabber.setImageHeight(captureHeight);
////        // rtsp格式一般添加TCP配置，否则丢帧会比较严重
////        // Brick在测试过程发现，该参数改成udp可以解决部分电脑出现的下列报警，但是丢帧比较严重
////        // av_interleaved_write_frame() error -22 while writing interleaved video packet.
////        grabber.setOption("rtsp_transport", "tcp");
////        grabber.start();
////        // 最后一个参数是AudioChannels，建议通过grabber获取
////        final FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("rtmp://192.168.1.130:554/machineHost", captureWidth, captureHeight, 1);
////        recorder.setInterleaved(true);
////        // 降低编码延时
////        recorder.setVideoOption("tune", "zerolatency");
////        // 提升编码速度
////        recorder.setVideoOption("preset", "ultrafast");
////        // 视频质量参数(详见 https://trac.ffmpeg.org/wiki/Encode/H.264)
////        recorder.setVideoOption("crf", "28");
////        // 分辨率
////        recorder.setVideoBitrate(2000000);
////        // 视频编码格式
////        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
////        // 视频格式
////        recorder.setFormat("flv");
////        // 视频帧率
////        recorder.setFrameRate(15);
////        recorder.setGopSize(60);
////        recorder.setAudioOption("crf", "0");
////        recorder.setAudioQuality(0);
////        recorder.setAudioBitrate(192000);
////        recorder.setSampleRate(44100);
////        // 建议从grabber获取AudioChannels
////        recorder.setAudioChannels(1);
////        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
////        recorder.start();
////
////        // 解决音视频同步导致的延时问题
////        Field field = recorder.getClass().getDeclaredField("oc");
////        field.setAccessible(true);
////        AVFormatContext oc = (AVFormatContext) field.get(recorder);
////        oc.max_interleave_delta(100);
////
////        // 用来测试的frame窗口
////        final CanvasFrame cFrame = new CanvasFrame("frame");
////        Frame capturedFrame = null;
////
////        // 有些时候，程序执行回报下列错误，添加一行代码解决此问题
////        // av_interleaved_write_frame() error -22 while writing interleaved video packet.
////        grabber.flush();
////
////        while ((capturedFrame = grabber.grab()) != null) {
////            if (cFrame.isVisible()) {
////                cFrame.showImage(capturedFrame);
////            }
////            System.out.println(grabber.getFrameNumber() + "--" + capturedFrame.timestamp);
////            recorder.setTimestamp(capturedFrame.timestamp);
////            recorder.record(capturedFrame);
////        }
////        cFrame.dispose();
////        recorder.close();
////        grabber.close();
//
//        rtmp();
//    }

    //rtsp://192.168.1.63:555/type=0&id=1
    //
    //rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
    String rstpUrl="";
    String rtmpUrl="";
    /**
     * rtsp转换成rtmp  rtmp可以在网页中播放
     *
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void rtmp() throws FrameGrabber.Exception, FrameRecorder.Exception, NoSuchFieldException, IllegalAccessException {

        System.out.println("start...");
        int width = 1280;
        int height = 720;
        try (FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov");
             // 最后一个参数是 AudioChannels, 建议通过 grabber 获取
             FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("rtmp://192.168.1.130:554/machineHot/1", width, height,
                     grabber.getAudioChannels())) {
            // rtsp 格式一般添加 TCP 配置，否则丢帧会比较严重
            // Brick 在测试过程发现, 该参数改成 udp 可以解决部分电脑出现的下列报警, 但是丢帧比较严重
            // av_interleaved_write_frame() error -22 while writing interleaved video packet.
            grabber.setOption("rtsp_transport", "tcp");
            grabber.setImageWidth(width);
            grabber.setImageHeight(height);
            System.out.println("grabber start");
            grabber.start();
            recorder.setInterleaved(true);
            // 降低编码延时
            recorder.setVideoOption("tune", "zerolatency");
            // 提升编码速度
            recorder.setVideoOption("preset", "ultrafast");
            // 视频质量参数(详见 https://trac.ffmpeg.org/wiki/Encode/H.264)
            recorder.setVideoOption("crf", "28");
            // 分辨率
            recorder.setVideoBitrate(1000000);
            // 视频编码格式
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            // 视频格式
            recorder.setFormat("flv");
            // 视频帧率
            recorder.setFrameRate(25);
            recorder.setGopSize(60);
            recorder.setAudioOption("crf", "0");
            recorder.setAudioQuality(0);
            recorder.setAudioBitrate(192000);
            recorder.setSampleRate(44100);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setPixelFormat(0);
            System.out.println("recorder start");
            recorder.start();
            System.out.println("all start!!");

        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
        }
        System.out.println("end...");

    }


    ////////////////////////////////////////////////////



    static boolean exit  = false;
    public static void main(String[] args) throws Exception {
//        System.out.println("start...");
//        String rtmpPath = "rtmp://192.168.1.130:554/machineHot";
////        String rtspPath = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
//        String rtspPath = "rtsp://192.168.1.231:555/type=0&id=1";
//
//
//        int audioRecord =0; // 0 = 不录制，1=录制
//        boolean saveVideo = false;
//        push(rtmpPath,rtspPath,audioRecord,saveVideo);
//
//        System.out.println("end...");


        Machine machine=new Machine();
        machine.setId(1l);
        machine.setIp("192.168.9.119");
        machine.setPort(24101);
        noticeMachineDoorStateChange(machine,"02");
    }

    public static void push(String rtmpPath,String rtspPath,int audioRecord,boolean saveVideo ) throws Exception  {
        // 使用rtsp的时候需要使用 FFmpegFrameGrabber，不能再用 FrameGrabber
        int width = 640,height = 480;
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspPath);
        grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式，不然会丢包很严重

        grabber.setImageWidth(width);
        grabber.setImageHeight(height);
        System.out.println("grabber start");
        grabber.start();
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(rtmpPath,width,height, audioRecord);
        recorder.setInterleaved(true);
        //recorder.setVideoOption("crf","28");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
        recorder.setFormat("flv"); // rtmp的类型
        recorder.setFrameRate(25);
        recorder.setImageWidth(width);recorder.setImageHeight(height);
        recorder.setPixelFormat(0); // yuv420p
        System.out.println("recorder start");
        recorder.start();
        //
        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
        System.out.println("all start!!");
        int count = 0;
        while(!exit){
            count++;
            Frame frame = grabber.grabImage();
            if(frame == null){
                continue;
            }
            if(count % 100 == 0){
                System.out.println("count="+count);
            }
            recorder.record(frame);
        }

        grabber.stop();
        grabber.release();
        recorder.stop();
        recorder.release();
    }

}

