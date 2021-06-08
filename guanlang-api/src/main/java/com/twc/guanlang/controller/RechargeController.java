package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.log.LogUtil;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.param.machine.OpenDoorParam;
import com.twc.guanlang.param.machine.RebootRechargeParam;
import com.twc.guanlang.service.RechargeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 充电桩管理
 * @author chenqiang
 */
@RestController
@RequestMapping("recharge")
@Slf4j
public class RechargeController extends BaseController {

    @Resource
    private RechargeService rechargeService;

    /**
     * 请求打开充电桩门
     * @param openDoorParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("请求打开充电桩门")
    @PostMapping("/authc/openDoor")
    public Object openDoor(@RequestBody OpenDoorParam openDoorParam) throws CustomException {

        rechargeService.openDoor(openDoorParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 请求关闭充电桩门
     * @param closeDoorParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("请求关闭充电桩门")
    @PostMapping("/authc/closeDoor")
    public Object closeDoor(@RequestBody CloseDoorParam closeDoorParam) throws CustomException {
        LogUtil.controllerLog(MachineController.class, "closeDoor", closeDoorParam);
        rechargeService.closeDoor(closeDoorParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 请求重启充电桩
     * @param rebootRechargeParam
     * @return
     * @throws CustomException
     */
    @ApiOperation("请求重启充电桩")
    @PostMapping("/authc/reboot")
    public Object reboot(@RequestBody RebootRechargeParam rebootRechargeParam) throws CustomException {

        LogUtil.controllerLog(MachineController.class, "reboot", rebootRechargeParam);
        rechargeService.reboot(rebootRechargeParam);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 获取所有充电桩
     * @return
     */
    @ApiOperation("所有充电桩")
    @PostMapping("/authc/all")
    public Object all() {
        LogUtil.controllerLog(MachineController.class, "all", null);
        rechargeService.all();
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }
}