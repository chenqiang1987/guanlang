package com.twc.guanlang.controller;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;

import com.twc.guanlang.entity.machine.Warning;
import com.twc.guanlang.entity.maintain.RepairType;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.param.user.MaintainParam;

import com.twc.guanlang.param.user.WorkOrderCreteParam;
import com.twc.guanlang.service.MaintainService;
import com.twc.guanlang.vo.MaintainVO;
import com.twc.guanlang.vo.PageData;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 运维工单管理
 * @author chenqiang
 */
@RestController
@RequestMapping("maintain")

@Slf4j
public class MaintainController extends BaseController {

    @Resource
    private MaintainService maintainService;

    /**
     * 运维工单列表
     *
     * @param request
     * @param response
     * @param userPageParam
     * @return
     */
    @ApiOperation(value = "分页获取工单列表", httpMethod = "POST")
    @PostMapping("/authc/selectPage")
    public Object selectPage(HttpServletRequest request, HttpServletResponse response, @RequestBody MaintainParam userPageParam) {


        SystemUser systemUser = getLoginUserByShiro();

        if (systemUser == null)
            return new ApiResultBean(ApiResultBean.CODE1.UNLOGIN.getCode(), "请重新登陆系统!");
        PageData pageData = maintainService.selectPage(userPageParam, systemUser);
        List<MaintainVO> list = pageData.getData();
//        if (list != null && list.size() > 0) {
//            list.forEach((e) -> {
//                List<RoleVO> roles = roleService.findRolesByUserId(Integer.parseInt(e.getId()+""));
//                UserVO userVO = new UserVO();
//                userVO.setRoles(roles);
//                BeanUtils.copyProperties(e, userVO);
//                userVOS.add(userVO);
//            });
//        }
        pageData.setData(list);
        return returnPageData(pageData);

    }

    /**
     * 运维故障类型
     *
     * @return
     */
    @ApiOperation(value = "故障类型", httpMethod = "POST")
    @PostMapping("/authc/faultType")
    public Object faultType() {


        Warning.CODE1[] arr = Warning.CODE1.values();

        List list = new ArrayList();
        for (Warning.CODE1 code1 : arr) {

            RepairType repairType = new RepairType();
            repairType.setCode(code1.getCode());
            repairType.setName(code1.getMsg());
            list.add(repairType);
        }


        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), list);
    }

    /**
     * 开始维修
     *
     * @param workId
     * @return
     */
    @ApiOperation(value = "开始维修", httpMethod = "POST")
    @PostMapping("/authc/startRepair")
    @Transactional
    public Object startRepair(@RequestParam Long workId) {

        try {
            maintainService.startRepair(workId);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }

        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 解决故障
     *
     * @param workId
     * @param content
     * @return
     */
    @ApiOperation(value = "解决维修", httpMethod = "POST")
    @PostMapping("/authc/resolveRepair")
    @Transactional
    public Object resolveRepair(@RequestParam Long workId, @RequestParam String content) {

        try {
            maintainService.resolveRepair(workId, content, getLoginUserByShiro());
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }

        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 未解决故障
     *
     * @param workId
     * @param content
     * @return
     */
    @ApiOperation(value = "未解决维修", httpMethod = "POST")
    @PostMapping("/authc/unResolveRepair")
    @Transactional
    public Object unResolveRepair(@RequestParam Long workId, @RequestParam String content) {

        try {
            maintainService.unResolveRepair(workId, content);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }

        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

    /**
     * 维修人员名下工单列表
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取专家的擅长领域", httpMethod = "POST")
    @PostMapping("/authc/getFaultByUser")
    public Object getFaultByUser(@RequestParam String userId) {

        List list = maintainService.getFaultByUser(userId);
        if (list == null || list.size() == 0) {
            return new ApiResultBean(ApiResultBean.CODE1.NULL_DATA.getCode(), "尚未给该专家配置擅长领域");
        }
        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE.getCode(), list);
    }

    /**
     * 手动创建工单
     *
     * @param workOrderCreteParam
     * @return
     */
    @ApiOperation(value = "创建工单", httpMethod = "POST")
    @PostMapping("/authc/createWorkOrder")
    @Transactional
    public Object createWorkOrder(@RequestBody WorkOrderCreteParam workOrderCreteParam) {


        try {

            maintainService.createWorkOrder(workOrderCreteParam);
        } catch (CustomException e) {
            return new ApiResultBean(ApiResultBean.CODE1.SYS_ERROR, e.getMessage());
        }


        return new ApiResultBean(ApiResultBean.CODE1.SUCCESS_CODE);
    }

}
