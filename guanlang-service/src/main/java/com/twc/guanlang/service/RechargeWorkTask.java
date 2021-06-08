package com.twc.guanlang.service;


import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.machine.Machine;
import com.twc.guanlang.entity.machine.MachineRecharge;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.MachineMapper;
import com.twc.guanlang.mapper.entity.MachineRechargeMapper;
import com.twc.guanlang.mapper.entity.RechargeMapper;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.param.machine.OpenDoorParam;
import com.twc.guanlang.param.machine.RechargeParam;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.vo.MachineWebScoketData;
import com.twc.guanlang.vo.RechargeWebScoketData;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 充电工作流
 *
 * @author chenqiang
 */
@Service
public class RechargeWorkTask {

    @Autowired
    private MachineRechargeMapper machineRechargeMapper;
    @Autowired
    private MachineMapper machineMapper;
    @Value("${machine.udp.receive.port}")
    private Integer machineUdpReceivePort;
    @Value("${machine.udp.send.port}")
    private Integer machineUdpSendPort;
    @Autowired
    private RechargeService rechargeService;
    /**
     * 开门超时
     */
    @Value("${recharge.openDoor.timeOut}")
    private String rechargeOpenDoorTimeOut;
    /**
     * 关门超时
     */
    @Value("${recharge.closeDoor.timeOut}")
    private String rechargeCloseDoorTimeOut;

    public void startRechargeWorkTask(RechargeParam rechargeParam) throws CustomException, InterruptedException {

        boolean isComplete = false;
        Long newId = null;
        Long start = System.currentTimeMillis();
        Long machineId = rechargeParam.getMachineId();
        Long rechargeId = rechargeParam.getRechrgeId();
        if (MachineService.onLineMap.get(machineId) == null || MachineService.onLineMap.get(machineId).equalsIgnoreCase("off")) {
            throw new CustomException("机器人不在线,本次充电申请被系统拒绝!");
        }
        if (RechargeUdpService.onLineMap.get(rechargeId) == null || RechargeUdpService.onLineMap.get(rechargeId).equalsIgnoreCase("off")) {
            throw new CustomException("充电桩不在线,本次充电申请被系统拒绝!");
        }
        MachineWebScoketData machineWebScoketData = MachineService.heartData.get(machineId);
        String stateName = machineWebScoketData.getStatus();
        String wireLessName = machineWebScoketData.getWirelessChargingState();
//        if (wireLessName.equalsIgnoreCase(Machine.RECHARGE_CODE.RECHARGING_COMPLETE.getMsg()) && stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())) {
//            throw new CustomException("机器人电量已满,本次充电申请被系统拒绝!");
//        }
//        if (wireLessName.equalsIgnoreCase(Machine.RECHARGE_CODE.RECHARGING_COMPLETE.getMsg()) && stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())) {
//            throw new CustomException("机器人目前正在充电中,本次充电申请被系统拒绝!");
//        }


//
//        if (stateName.equalsIgnoreCase(Machine.CODE1.RETURNING.getMsg()) || stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())) {



            Example example = new Example(MachineRecharge.class);
            example.createCriteria().orEqualTo("status", MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode()).orEqualTo("status", MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode()).andEqualTo("machineId",machineId);
            example.orderBy("createTime").desc();
            RowBounds rowBounds = new RowBounds(0, 1);
            List<MachineRecharge> machineRechargeList = machineRechargeMapper.selectByExampleAndRowBounds(example, rowBounds);
            MachineRecharge exists = null;
            if (machineRechargeList != null && machineRechargeList.size() >0) {
                exists = machineRechargeList.get(0);
                Long existsRechargeId=exists.getRechargeId();
                Recharge existsRecharge=rechargeMapper.selectByPrimaryKey(existsRechargeId);

                if(stateName.equalsIgnoreCase(Machine.CODE1.RETURNING.getMsg()) ){
                    throw new CustomException("機器人正在前往"+existsRecharge.getLocation()+"執行充電任務,请等待本次充电流程反馈再发起充电流程!");
                }
                if(stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg()) ){
                    throw new CustomException("機器人正在"+existsRecharge.getLocation()+"充電,请等待本次充电流程反馈再发起充电流程!");
                }
            }else{
                //機器人處於初始化狀態後者初始化后的recharging狀態不接受充電指令
//                if(stateName.equalsIgnoreCase(Machine.CODE1.Initiating.getMsg())  ||stateName.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())  ){
//////                    throw new CustomException("當前機器人處於初始化狀態,請等待機器人進入正常工作狀態后發起充電指令!");
//////                }
            }




            // 继续完成上次充电任务
//            if (exists.getStatus().equalsIgnoreCase(MachineRecharge.STATUS_ENUM.RETURNING.getCode())) {
//                while (true) {
//                    machineWebScoketData = MachineService.heartData.get(machineId);
//                    if (machineWebScoketData.getStatus().equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())
//                            && machineWebScoketData.getWirelessChargingState().equalsIgnoreCase(Machine.RECHARGE_CODE.UN_RECHARGE.getMsg())) {
//                        Example example1 = new Example(Machine.class);
//                        Machine existsMachine = machineMapper.selectByPrimaryKey(machineId);
//                        recharging2wireLessRecharging(exists, exists.getRechargeId(), existsMachine, machineWebScoketData, false);
//                    }
//                }
//            } else {
//                Example example1 = new Example(Machine.class);
//                Machine existsMachine = machineMapper.selectByPrimaryKey(machineId);
//                recharging2wireLessRecharging(exists, exists.getRechargeId(), existsMachine, machineWebScoketData, false);
//            }

//
//            throw new CustomException("机器人已接收上次充电指令,请等待上次充电流程反馈再发起充电流程!");
//
//        }

//
//        if (stateName.equalsIgnoreCase(Machine.CODE1.INITIAL_FAILED.getMsg()) || stateName.equalsIgnoreCase(Machine.CODE1.OUT_OF_FAILURE.getMsg()) || stateName.equalsIgnoreCase(Machine.CODE1.LOCATE_FAILURE.getMsg())) {
//
//        }

        /**
         * 发送充电指令到机器人
         */
        String timestampStr = UdpUtil.machineUdpTimeStamp();
        StringBuffer udpTemplate = UdpUtil.machineUdpSendTemlate();
        String udpStr = udpTemplate.toString();
        //充电指令11个字节 ob00
        udpStr = udpStr.replace("length", "0b00");
        udpStr = udpStr.replace("timeStamp", timestampStr);
        udpStr = udpStr.replace("type", MachineService.CODE1.RECHARGE.getCode());

        //data为充电桩编号
        int number=0;
        if(rechargeId.intValue()==1)
            number =3;
        if(rechargeId.intValue()==2)
            number =1;
        if(rechargeId.intValue()==3)
            number =2;
        String hexStr = UdpUtil.intToHex(number);
        hexStr = hexStr.length() == 1 ? "0" + hexStr : hexStr;
        udpStr = udpStr.replace("data", hexStr);
        String udpTemp = udpStr;
        //除去check做校验运算
        udpStr = udpStr.replace("check", "");
        String checkCode = UdpUtil.getCheckCodeByhexByte(udpStr.toUpperCase());
        udpTemp = udpTemp.replace("check", checkCode).toUpperCase();
        Machine machine = machineMapper.selectByPrimaryKey(machineId);
        String udpReceiveStr = UdpUtil.sendUdpOrderAndWaitReceive(udpTemp, machine, machineUdpReceivePort);
        udpReceiveStr = udpReceiveStr.replace(" ", "");
        String lengthStr = udpReceiveStr.substring(2, 6);
        String timeStampStr = udpReceiveStr.substring(6, 14);
        String typeCodeStr = udpReceiveStr.substring(14, 16);
        String dataStr = udpReceiveStr.substring(16, 18);
        if (!dataStr.equalsIgnoreCase("00")) {


            MachineRecharge machineRecharge = new MachineRecharge();
            machineRecharge.setMachineId(machine.getId());
            machineRecharge.setRechargeId(rechargeId);
            machineRecharge.setCreateTime(new Date());
            machineRecharge.setUpdateTime(new Date());
            machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.PROTECT.getCode());
            machineRechargeMapper.insert(machineRecharge);
            throw new CustomException("机器人处于保护状态,请稍后重试!");
        }else{


            MachineRecharge machineRecharge = new MachineRecharge();
            machineRecharge.setMachineId(machine.getId());
            machineRecharge.setRechargeId(rechargeId);
            machineRecharge.setCreateTime(new Date());
            machineRecharge.setUpdateTime(new Date());
            machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode());
            machineRechargeMapper.insert(machineRecharge);
            //通知開門

            OpenDoorParam openDoorParam = new OpenDoorParam();
            openDoorParam.setId(rechargeId);
            //防止与充电桩通信失败,间隔1秒，总共发送3次开门指令
            rechargeService.openDoor(openDoorParam);
//            Thread.sleep(1000);
//            rechargeService.openDoor(openDoorParam);
//            Thread.sleep(1000);
//            rechargeService.openDoor(openDoorParam);
        }


//        else {
//
//            /**
//             * 监听机器人状态变更:
//             *      recharging--->通知充电桩开门
//             *
//             */
//            MachineRecharge machineRecharge = null;
//            boolean hasNew = false;
//            while (true) {
//                if (isComplete) break;
//                machineWebScoketData = MachineService.heartData.get(machineId);
//                if (machineWebScoketData.getStatus().equalsIgnoreCase(Machine.CODE1.RETURNING.getMsg())
//                ) {
//                    if (!hasNew) {
//                        machineRecharge = new MachineRecharge();
//                        machineRecharge.setMachineId(machineId);
//                        machineRecharge.setRechargeId(rechargeId);
//                        machineRecharge.setCreateTime(new Date());
//                        machineRecharge.setUpdateTime(new Date());
//                        machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RETURNING.getCode());
//                        machineRechargeMapper.insert(machineRecharge);
//                        newId = machineRecharge.getId();
//                        hasNew = true;
//                        try {
//                            Thread.sleep(300);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    continue;
//                }
//                if (machineWebScoketData.getStatus().equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())
//                        && machineWebScoketData.getWirelessChargingState().equalsIgnoreCase(Machine.RECHARGE_CODE.UN_RECHARGE.getMsg())
//                ) {
//
//                    //如果未监听到returning状态 直接进入了recharging状态，则machineRecharge为null，在recharging2wireLessRecharging方法里创建machineRecharge对象
//                    recharging2wireLessRecharging(machineRecharge, rechargeId, machine, machineWebScoketData, false);
//                }
//            }
//        }
    }


    @Resource
    private RechargeMapper rechargeMapper;

    /**
     * 从recharging到
     *
     * @param newId
     * @param rechargeId
     * @param machine
     * @param machineWebScoketData
     * @param isComplete
     * @throws CustomException
     */
    private void recharging2wireLessRecharging(MachineRecharge machineRecharge, Long rechargeId, Machine machine, MachineWebScoketData machineWebScoketData, boolean isComplete) throws CustomException {
        {
            try {
                if (machineRecharge == null) {
                    machineRecharge = new MachineRecharge();
                    machineRecharge.setMachineId(machine.getId());
                    machineRecharge.setRechargeId(rechargeId);
                    machineRecharge.setCreateTime(new Date());
                    machineRecharge.setUpdateTime(new Date());
                    machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode());
                    machineRechargeMapper.insert(machineRecharge);
                } else {
                    machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode());
                    machineRecharge.setUpdateTime(new Date());
                    machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                }
                OpenDoorParam openDoorParam = new OpenDoorParam();
                openDoorParam.setId(rechargeId);
                //防止与充电桩通信失败,间隔1秒，总共发送3次开门指令
                rechargeService.openDoor(openDoorParam);
                Thread.sleep(1000);
                rechargeService.openDoor(openDoorParam);
                Thread.sleep(1000);
                rechargeService.openDoor(openDoorParam);
                //线程睡眠等待最大开门时间,如果开门超时则此次充电失败，告警充电桩开门异常,由人工手动开门尝试
                Long openStart = System.currentTimeMillis();
                Long openTimeOut = Long.parseLong(rechargeOpenDoorTimeOut);
                while (true) {
                    Long openCheck = System.currentTimeMillis();
                    String doorState = RechargeUdpService.rechargeDoorState.get(rechargeId);
                    if (StringUtil.isEmpty(doorState) && openCheck - openStart > openTimeOut) {
                        throw new CustomException(openDoorParam.getId() + "号充电桩开门超时,此次充电申请失败!");
                    } else if (StringUtil.isEmpty(doorState)) {
                        continue;
                    } else if (openCheck - openStart > openTimeOut && doorState.equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.CLOSED.getCode())) {
                        throw new CustomException(openDoorParam.getId() + "号充电桩开门超时,此次充电申请失败!");
                    } else if (doorState.equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.OPENED.getCode())) {
                        break;
                    }
                }
                String doorState = RechargeUdpService.rechargeDoorState.get(rechargeId);
                if (RechargeUdpService.rechargeDoorState.get(rechargeId).equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.CLOSED.getCode())) {

                } else if (RechargeUdpService.rechargeDoorState.get(rechargeId).equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.OPENED.getCode())) {
                    //门已经打开.通知机器人
                    MachineService.noticeMachineDoorStateChange(machine, doorState);
                    //巡检监听机器人状态:机器人成功进入充电状态，再关闭充电桩门
                    Long startTime = System.currentTimeMillis();
                    while (true) {
                        Long checkTime = System.currentTimeMillis();
                        machineWebScoketData = MachineService.heartData.get(machine.getId());
                        if (machineWebScoketData.getStatus().equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg()) && !machineWebScoketData.getWirelessChargingState().equalsIgnoreCase(Machine.RECHARGE_CODE.UN_RECHARGE.getMsg())) {

                            CloseDoorParam closeDoorParam = new CloseDoorParam();
                            closeDoorParam.setId(rechargeId);
                          //  rechargeService.closeDoor(closeDoorParam);
                            isComplete = true;
                            machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
                            machineRecharge.setUpdateTime(new Date());
                            machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                            break;
                        }
                    }
                }
            } catch (InterruptedException | CustomException e) {
                throw new CustomException("充电失败:" + e.getMessage());
            }
        }
    }

}