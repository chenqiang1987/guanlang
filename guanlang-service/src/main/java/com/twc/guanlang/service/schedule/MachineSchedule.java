package com.twc.guanlang.service.schedule;

import com.alibaba.fastjson.JSONObject;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.maintain.WorkOrder;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.*;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.param.machine.RechargeParam;
import com.twc.guanlang.param.machine.StandbyParam;
import com.twc.guanlang.service.*;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.service.ws.WebSocket;
import com.twc.guanlang.vo.MachineWebScoket;
import com.twc.guanlang.vo.MachineWebScoketData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *   机器人监听程序----自动任务完成/机器人心跳/充电桩心跳
 *
 * @author chenqiang
 */
@Slf4j
@Service
public class MachineSchedule {

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
    @Resource
    private WebSocket webSocket;
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

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * 监听机器人自动任务是否完成::经历过管廊头尾
     *
     */
//    @Scheduled(fixedRate = 3 * 1000)
    public void completeAutoMession() {

        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("status", AutoMession.STATUS_ENUM.EXECUTING.getCode())
                .andEqualTo("type", AutoMession.TYPE_ENUM.AUTO.getCode());
        example.orderBy("start").desc();
        //　每次查询1条
        RowBounds rowBounds = new RowBounds(0, 1);
        List<AutoMession> list = autoMessionMapper.selectByExampleAndRowBounds(example, rowBounds);
        if (list != null && list.size() > 0) {
            for (AutoMession autoMession : list) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean reachHead = false;
                        boolean reachTail = false;
                        while (true) {
                            MachineWebScoketData machineWebScoketData = MachineService.heartData.get(autoMession.getMachineId());
                            float x = machineWebScoketData.getX();
                            float y = machineWebScoketData.getY();
                            autoMessionMapper.updateByPrimaryKey(autoMession);
                            StandbyParam standbyParam = new StandbyParam();
                            standbyParam.setId(autoMession.getMachineId());
                            if (positionService.hasReachHead(x, y)) {
                                reachHead = true;
                            }
                            if (positionService.hasReachTail(x, y)) {
                                reachTail = true;
                            }
                            if (reachHead && reachTail) {
                                autoMession.setStatus(AutoMession.STATUS_ENUM.COMPLETE.getCode());
                                autoMession.setUpdateTime(new Date());
                                autoMession.setEndAt(new Date());
                                autoMession.setEndPostionX(machineWebScoketData.getX());
                                autoMession.setEndPostionY(machineWebScoketData.getY());
                                autoMessionMapper.updateByPrimaryKey(autoMession);
                                try {
                                    RechargeParam rechargeParam = new RechargeParam();
                                    Recharge recharge = positionService.searchNearestCharge(machineWebScoketData.getX(), machineWebScoketData.getY());
                                    rechargeParam.setMachineId(autoMession.getMachineId());
                                    rechargeParam.setRechrgeId(recharge.getId());
                                    try {
                                        machineService.callRecharge(rechargeParam);
                                    } catch (IOException e) {
                                        log.error("自动任务完成,开始充电发生错误:" + e.getMessage());
                                    } catch (InterruptedException e) {
                                        log.error("自动任务完成,开始充电发生错误:" + e.getMessage());
                                    }
                                } catch (CustomException e) {

                                    log.error("自动任务完成,开始充电发生错误:" + e.getMessage());
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 按时执行机器人自动任务
     */
    @Scheduled(fixedRate = 10 * 1000, initialDelay = 30 * 1000)
    public void exeAutoMession() {

        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE)
                .andNotEqualTo("status", AutoMession.STATUS_ENUM.COMPLETE.getCode())
                .andEqualTo("type", AutoMession.TYPE_ENUM.AUTO.getCode())
                .andNotEqualTo("status", AutoMession.STATUS_ENUM.EXECUTING.getCode());
        example.orderBy("start").desc();
        //　每次查询1条
        RowBounds rowBounds = new RowBounds(0, 1);
        List<AutoMession> list = autoMessionMapper.selectByExampleAndRowBounds(example, rowBounds);
        if (list != null && list.size() > 0) {
            for (AutoMession autoMession : list) {
                String taskStart = autoMession.getStart();
                SimpleDateFormat  simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                String now=simpleDateFormat.format(new Date());
                Long nowLong = System.currentTimeMillis();

                taskStart=taskStart.replace(":","");
                int taskInt=Integer.parseInt(taskStart);

                int nowInt=Integer.parseInt(now);

                Integer dis = nowInt - taskInt;
                if (dis < 5 * 60 * 1000 && dis > -5 * 60 * 1000) {
                    if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HALF_WORKING.getCode())) {

                        autoMession.setStatus(AutoMession.STATUS_ENUM.FAILED.getCode());
                        autoMession.setReason("机器正在执行半自动任务,此次自动巡检任务被拒绝");
                        autoMession.setUpdateTime(new Date());
                        autoMessionMapper.updateByPrimaryKey(autoMession);
                        return;
                    } else if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HAND_WORKING.getCode())) {
                        autoMession.setStatus(AutoMession.STATUS_ENUM.FAILED.getCode());
                        autoMession.setReason("机器正在执行手动任务,此次自动巡检任务被拒绝");
                        autoMession.setUpdateTime(new Date());
                        autoMessionMapper.updateByPrimaryKey(autoMession);
                        return;
                    } else if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.CHARGIN.getCode())) {
                        autoMession.setStatus(AutoMession.STATUS_ENUM.FAILED.getCode());
                        autoMession.setReason("机器正在执行充电任务,此次自动巡检任务被拒绝");
                        autoMession.setUpdateTime(new Date());
                        autoMessionMapper.updateByPrimaryKey(autoMession);
                        return;
                    }
                    StandbyParam stopParam = new StandbyParam();
                    stopParam.setId(autoMession.getMachineId());
                    try {
                        machineService.callPatrol(stopParam);
                        autoMession.setStatus(AutoMession.STATUS_ENUM.EXECUTING.getCode());
//                        new Thread().start();
                        autoMessionMapper.updateByPrimaryKey(autoMession);
                    } catch (CustomException e) {
                        log.error(" 机器人自动任务启动失败:" + e.getMsg());
                    } catch (IOException e) {
                        log.error(" 机器人自动任务启动失败:" + e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * 项目启动10秒后  开始检测心跳数据
     */
    @Scheduled(fixedRate = 10 * 1000, initialDelay = 30 * 1000)
    public void heart() {
        List<Machine> rechargeList = machineMapper.selectAll();
        for (Machine recharge : rechargeList) {
            if (recharge.getEnable().equalsIgnoreCase(BaseEntity.ENABLE.DISABLE)) continue;
            Long lastHeart = MachineService.heartMap.get(recharge.getId());
            Long time = System.currentTimeMillis();
            if (lastHeart == null || time - lastHeart > 3 * 10 * 1000) {
                MachineService.onLineMap.put(recharge.getId(), "off");
                log.info(recharge.getIp() + "离线.....检查时间戳:" + time + "  末次心跳时间戳:" + lastHeart);
            }
        }
    }

    @Resource
    private   MachineRechargeMapper  machineRechargeMapper;

    /**
     * 充电任务监视
     */
    @Scheduled(fixedRate = 10 * 1000, initialDelay = 30 * 1000)
    public void rechargeTaskMonitor() {

        List<Recharge>  allRe=rechargeMapper.selectAll();
        for(Recharge recharge:allRe){

            String  online=RechargeUdpService.onLineMap.get(recharge.getId());
            if(online.equalsIgnoreCase("on")){

                if(recharge.getElectricity()>0){

                    Example  example=new Example(MachineRecharge.class);
                    example.createCriteria().andEqualTo("rechargeId",recharge.getId()).andEqualTo("status",MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode());

                    List<MachineRecharge>  machineRecharges=machineRechargeMapper.selectByExample(example);
                   if(machineRecharges!=null &&  machineRecharges.size()>0){

                       MachineRecharge  machineRecharge=machineRecharges.get(0);
                       machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
                       machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                   }
                }
            }
        }

    }


      Long  rechargeFaileStart=0l;

    /**
     * 充电任务失败监视
     *
     * 机器人充电状态下，充电桩超过6分钟没有电流反馈 ，认为充电失败
     */
//    @Scheduled(fixedRate = 10 * 1000, initialDelay = 30 * 1000)
    public void rechargeTaskMonitor2() {


        String  machineState=
        MachineService.WORK_STATUS;
        if(machineState==null)
            return ;
        String machineOnine=MachineService.onLineMap.get(1l);
        if(machineOnine.equalsIgnoreCase("on") &&  machineState.equalsIgnoreCase(Machine.CODE1.RECHARGING.getMsg())){

            List<Recharge>  recharges=rechargeMapper.selectAll();
            double ele=0;
            for(Recharge recharge:recharges){

                if(RechargeUdpService.onLineMap.get(recharge.getId()).equalsIgnoreCase("on") &&  recharge.getElectricity()>0){

                    ele=recharge.getElectricity();
                }
            }
            if(ele==0){
                if( rechargeFaileStart == 0l)
                    rechargeFaileStart=System.currentTimeMillis();
                else{

                    Long now=System.currentTimeMillis();
                    if((now-rechargeFaileStart)>1000*60*6){

                        Warning  warning=new Warning();
                        warning.setCreateTime(new Date());
                        warning.setType(Warning.CODE1.RECHARGE_FAILED_TIMEOUT.getCode());
                        warning.setCreateTime(new Date());
                        warningMapper.insert(warning);

                        Example  example=new Example(MachineRecharge.class);
                        example.createCriteria().andEqualTo("status",MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
                        List<MachineRecharge>  list=machineRechargeMapper.selectByExample(example);
                        if(list!=null   && list.size()>0){

                            for(MachineRecharge  machineRecharge:list){

                                machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RECHARGE_FAILED_TIMEOUT.getCode());
                                machineRecharge.setFailed_reason(MachineRecharge.STATUS_ENUM.RECHARGE_FAILED_TIMEOUT.getMsg());
                                machineRecharge.setUpdateTime(new Date());
                                machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                            }
                        }

                        //时间重置
                        rechargeFaileStart=0l;
                    }
                }
            }
        }
    }



    public static void main(String s[]){


    }


}