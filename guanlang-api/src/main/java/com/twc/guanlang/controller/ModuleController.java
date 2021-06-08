package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.param.machine.CurveGraphParam;
import com.twc.guanlang.param.machine.SetValueForModuleParam;
import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 感应器管理
 * <p>
 * authc表示需要登陆才能访问
 */
@RestController
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Autowired
    private MachineService machineService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 给机器人设置感应器模块
     * @param machineId
     * @param modules
     * @return
     */
    @ApiOperation("给机器人设置接收模块")
    @PostMapping("/authc/setModulesForMachine")
    public Object setModulesForMachine(@RequestParam Integer machineId, @RequestParam String modules) {

        machineService.updateModules(machineId, modules);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 获取机器人的感应器模块
     * @param machineId
     * @return
     */
    @ApiOperation("获取机器人已经设置的模块")
    @PostMapping("/authc/getModuleFromMachine")
    public Object getModuleFromMachine(@RequestParam String machineId) {

        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), moduleService.getModuleFromMachine(machineId));
    }

    /**
     * 设置机器人感应模块的阈值
     * @param setValueForModuleParam
     * @return
     */
    @ApiOperation("设置感应模块的阈值")
    @PostMapping("/authc/setValue")
    public Object setModules(@RequestBody SetValueForModuleParam setValueForModuleParam) {

        try {
            moduleService.updateValueForModule(setValueForModuleParam);
        } catch (CustomException e) {
            return new ApiResultBean(e.getCode(), e.getMsg());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 获取机器人感应模块的阈值
     * @param machineId
     * @return
     */
    @ApiOperation("获取机器人感应模块的阈值")
    @PostMapping("/authc/getValue")
    public Object getValue(@RequestParam Integer machineId) {

        List list = moduleService.getValue(machineId);
        if (list == null || list.size() == 0) {

            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA.getCode(), "尚未给该机器人设置各模块阈值!");
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), moduleService.getValue(machineId));
    }

    /**
     * 获取所有得感应模块
     * @return
     */
    @ApiOperation("获取所有得感应模块")
    @PostMapping("/authc/getAll")
    public Object getAll() {

        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), moduleService.getAll());
    }

    /**
     * 感应模块数据曲线图
     *
     * @param machineId
     * @return
     */
    @ApiOperation("curveGraph")
    @PostMapping("/authc/curveGraph")
    public Object reportCurve(@RequestBody CurveGraphParam curveGraphParam) {

        Map<String, List> mapMap = null;
        try {
            mapMap = moduleService.reportCurve(curveGraphParam);
        } catch (CustomException custo) {

            return new ApiResultBean(custo.getCode(), custo.getMsg());
        }
        if (mapMap == null || mapMap.size() == 0) {

            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA.getCode(), "未查询到相关数据!");
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), mapMap);
    }

}


