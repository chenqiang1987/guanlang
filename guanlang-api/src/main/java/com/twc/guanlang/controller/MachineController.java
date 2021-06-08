package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.log.LogUtil;
import com.twc.guanlang.param.machine.*;
import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.RechargeWorkTask;
import com.twc.guanlang.vo.MachineStateVO;
import com.twc.guanlang.vo.MachineWebScoketData;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 机器人相关http请求响应
 *
 * @author chenqiang
 */
@RestController
@RequestMapping("machine")
@Slf4j
public class MachineController extends BaseController {

    @Resource
    private RechargeWorkTask rechargeWorkTask;
    @Resource
    private MachineService machineService;

    /**
     * 获取机器人列表
     *
     * @return
     */
    @ApiOperation("机器人列表")

    @PostMapping("/authc/all")
    public Object all() {
        LogUtil.controllerLog(MachineController.class, "all", null);
        List list = machineService.getMachineList();
        if (list != null && list.size() > 0) {
            return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), list);
        } else {
            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA);
        }
    }

    /**
     * 半自动任务设置巡检区间
     *
     * @param sectionParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("半自动设置巡检区间")
    @PostMapping("/authc/setSection")
    public Object setSection(@RequestBody SectionParam sectionParam) throws CustomException {
        LogUtil.controllerLog(MachineController.class, "setSection", sectionParam);
        machineService.setSection(sectionParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 机器人进入待机
     *
     * @return
     */
    @ApiOperation("机器人待机")
    @PostMapping("/authc/standby")
    public Object standby() throws CustomException {
        LogUtil.controllerLog(MachineController.class, "start", null);

        StandbyParam stopParam=new StandbyParam();
        stopParam.setId(1L);
        machineService.callStandby(stopParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 状态上报：定时上报机器人状态信息.位置+电量等信息
     *
     * @return
     */
    @ApiOperation("状态上报：定时上报机器人状态信息.位置+电量等信息")
    @PostMapping("/authc/reportStatus")
    public Object reportStatus(@RequestBody RechargeParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "recharge", rechargeParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 故障上报：机器人主动上报故障信息
     *
     * @param setPeriodParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("故障上报：机器人主动上报故障信息")
    @PostMapping("/authc/reportFault")
    public Object reportFault(@RequestBody ReportFaultParam setPeriodParam) throws CustomException {
        LogUtil.controllerLog(MachineController.class, "setPeriod", setPeriodParam);
        machineService.saveReportFalut(setPeriodParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 查询机器人udp状态
     *
     * @return
     */
    @ApiOperation("查询机器人udp状态")
    @PostMapping("/authc/udpState")
    public Object udpState(@RequestBody RechargeParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "recharge", rechargeParam);
        try {
            machineService.callRecharge(rechargeParam);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        } catch (IOException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        } catch (InterruptedException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 请求机器人充电
     *
     * @param rechargeParam
     * @return
     */
    @ApiOperation("请求机器人充电")
    @PostMapping("/authc/callRecharge")
    public Object recharge(@RequestBody RechargeParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "callRecharge", rechargeParam);
        try {
            rechargeWorkTask.startRechargeWorkTask(rechargeParam);
        } catch (CustomException | InterruptedException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }


    @ApiOperation("发送初始位置编号给机器人")
    @PostMapping("/authc/sendInitNumber")
    public Object sendInitNumber(@RequestBody RechargeParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "sendInitNumber", rechargeParam);
        try {
            machineService.sendInitNumber(rechargeParam);
        } catch (CustomException | IOException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    @ApiOperation("发送初始位置编号给机器人12")
    @PostMapping("/authc/sendInitNumber12")
    public Object sendInitNumber12(@RequestBody RechargeParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "sendInitNumber", rechargeParam);
        try {
            machineService.sendInitNumber12(rechargeParam);
        } catch (CustomException | IOException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }



    /**
     * 请求机器人进入standy状态
     *
     * @param rechargeParam
     * @return
     */
    @ApiOperation("原地进入standy状态指令")
    @PostMapping("/authc/callStandby")
    public Object callStandby(@RequestBody StandbyParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "callStandby", rechargeParam);
        try {
            machineService.callStandby(rechargeParam);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 请求机器人进入自动任务状态
     *
     * @param rechargeParam
     * @return
     */
    @ApiOperation("进入自动状态")
    @PostMapping("/authc/callPatrol")
    public Object callPatrol(@RequestBody StandbyParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "callPatrol", rechargeParam);
        try {
            machineService.callPatrol(rechargeParam);
        } catch (CustomException | IOException | InterruptedException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 调整机器人升降机高度
     *
     * @param rechargeParam
     * @return
     */
    @ApiOperation("调整机器人升降机高度")
    @PostMapping("/authc/callHeight")
    public Object callHeight(@RequestBody HeightParam rechargeParam) {
        LogUtil.controllerLog(MachineController.class, "callHeight", rechargeParam);
        try {
            machineService.callHeight(rechargeParam);
        } catch (CustomException | IOException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }



    /**
     * 机器人运维总览状态
     *
     * @param number
     * @return
     */
    @ApiOperation("机器人运维总览状态")
    @PostMapping("/authc/state")
    public Object state(@RequestParam String number) {

        MachineWebScoketData machineWebScoketData = MachineService.heartData.get(Long.parseLong(number));
        MachineStateVO machineStateVO = new MachineStateVO();


        String onLine=MachineService.onLineMap.get(Long.parseLong(number));
        if(onLine.equalsIgnoreCase("on")){

            machineStateVO.setRestElc( machineWebScoketData.getEnergy()+"");
            machineStateVO.setHardWareState("正常");
            List list = new ArrayList();
//            list.add("管道损坏");
//            list.add("二氧化碳浓度超标");
            machineStateVO.setFaults(list);
            machineStateVO.setMessionState("正常");
        }else{

            machineStateVO.setRestElc("");
            machineStateVO.setHardWareState("离线");
            List list = new ArrayList();
//            list.add("管道损坏");
//            list.add("二氧化碳浓度超标");
//            machineStateVO.setFaults(list);
            machineStateVO.setMessionState("离线");
        }

        //machineStateVO.setRepairState("已申请维修");
       // machineStateVO.setExpertState("协助中");
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), machineStateVO);
    }

    /**
     * 新增机器人
     *
     * @param number
     * @param name
     * @return
     */
    @ApiOperation("新增机器人")
    @PostMapping("/authc/add")
    public Object add(@RequestParam String number, @RequestParam String name) {
        try {
            machineService.add(number, name);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 设置默认机器人
     *
     * @param number
     * @param name
     * @return
     */
    @ApiOperation("设置默认机器人")
    @PostMapping("/authc/setDefalut")
    public Object setDefalut(@RequestParam String number, @RequestParam String name) {
        try {
            machineService.add(number, name);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

}
