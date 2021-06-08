package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.log.LogUtil;
import com.twc.guanlang.entity.machine.AutoMession;
import com.twc.guanlang.param.machine.*;
import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.MessionService;
import com.twc.guanlang.vo.MachineStateVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 计算机任务请求响应
 *
 * @author chenqiang
 */
@RestController
@RequestMapping("mession")

@Slf4j
public class MessionController extends BaseController {

    @Resource
    private MessionService messionService;

    /**
     * 添加自动任务
     * @param autoMessionParam
     * @return
     */
    @ApiOperation("给机器人添加自动任务")
    @PostMapping("/authc/addAutoMession")
    public Object addAutoMession(@RequestBody AutoMessionParam autoMessionParam) {

        try {
            messionService.addAutoMession(autoMessionParam);
        } catch (Exception e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 停止半自动任务
     * @param messionId
     * @return
     */
    @ApiOperation("结束自动和半自动任务")
    @PostMapping("/authc/endHalfAndHandMession")
    public Object endHalfAndHandMession(@RequestParam Long messionId) {

        try {
            messionService.endHalfAndHandMession(messionId);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 获取任务列表
     * @param machineId
     * @return
     */
    @ApiOperation("获取机器人自动任务列表")
    @PostMapping("/authc/getAutoMessions")
    public Object getAutoMessions(@RequestParam Integer machineId) {

        List<AutoMession> list = messionService.getAutoMessions(machineId);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), list);
    }

    /**
     * 删除任务
     * @param machineId
     * @return
     */
    @ApiOperation("删除机器人自动任务")
    @PostMapping("/authc/delMessionAuto")
    public Object delMessionAuto(@RequestParam Integer machineId) {

        messionService.delAutoMession(machineId);
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }
}
