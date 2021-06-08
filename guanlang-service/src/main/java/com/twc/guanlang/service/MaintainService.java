package com.twc.guanlang.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.str.TwcStringUtil;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.Warning;
import com.twc.guanlang.entity.maintain.Repair2User;
import com.twc.guanlang.entity.maintain.RepairType;
import com.twc.guanlang.entity.maintain.WorkOrder;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.MaintainMapper;
import com.twc.guanlang.mapper.entity.Repair2UserMapper;
import com.twc.guanlang.mapper.entity.UserMapper;
import com.twc.guanlang.mapper.entity.WorkOrderMapper;
import com.twc.guanlang.param.user.MaintainParam;
import com.twc.guanlang.param.user.UserPageParam;
import com.twc.guanlang.param.user.WorkOrderCreteParam;
import com.twc.guanlang.vo.MaintainVO;
import com.twc.guanlang.vo.PageData;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.applet.Main;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class MaintainService {


    @Autowired
    private MaintainMapper maintainMapper;


    @Autowired
    private Repair2UserMapper repair2UserMapper;

    public PageData selectPage(MaintainParam userPageParam,SystemUser systemUser) {
        Page<MaintainVO> page = PageHelper.startPage(userPageParam.getPageNum(), userPageParam.getPageSize(), "word.update_time desc");




        List list = maintainMapper.selectPage(userPageParam,systemUser);
        return new PageData(page);
    }


    public List getFaultByUser(String userId) {

        Example example = new Example(Repair2User.class);
        example.createCriteria().andEqualTo("userId", userId);
        List<Repair2User> list = repair2UserMapper.selectByExample(example);

        Warning.CODE1[] arr = Warning.CODE1.values();
        List list1 = new ArrayList();
        for (Repair2User repair2User : list) {

            for (Warning.CODE1 code1 : arr) {

                if (repair2User.getRepairCode().equalsIgnoreCase(code1.getCode())) {
                    list1.add(code1.getMsg());
                }
            }

        }
        return list1;
    }


    @Autowired
    private WorkOrderMapper workOrderMapper;


    public void startRepair(Long workId) throws CustomException {

        WorkOrder workOrder = workOrderMapper.selectByPrimaryKey(workId);
        workOrder.setStatus(WorkOrder.CODE1.REPAIRING.getCode());
        workOrder.setStatusName(WorkOrder.CODE1.REPAIRING.getMsg());
        workOrder.setUpdateTime(new Date());
        if (workOrder.getUserId() == null || workOrder.getUserId() == 0l) {
            throw new CustomException("该工单尚未指派维修人员!");
        }
        workOrderMapper.updateByPrimaryKey(workOrder);
    }

    public void resolveRepair(Long workId, String content,SystemUser systemUser) throws CustomException {


        WorkOrder workOrder = workOrderMapper.selectByPrimaryKey(workId);
        workOrder.setResolveUserId(systemUser.getId());
        workOrder.setResult(content);
        workOrder.setStatus(WorkOrder.CODE1.COMPLETED.getCode());
        workOrder.setStatusName(WorkOrder.CODE1.COMPLETED.getMsg());
        workOrder.setUpdateTime(new Date());
        if (workOrder.getUserId() == null || workOrder.getUserId() == 0l) {
            throw new CustomException("该工单尚未指派维修人员!");
        }
        workOrderMapper.updateByPrimaryKey(workOrder);
    }


    public void unResolveRepair(Long workId, String content) throws CustomException {

        WorkOrder workOrder = workOrderMapper.selectByPrimaryKey(workId);
        workOrder.setResult(content);
        workOrder.setStatus(WorkOrder.CODE1.FAILED.getCode());
        workOrder.setStatusName(WorkOrder.CODE1.FAILED.getMsg());
        workOrder.setUpdateTime(new Date());
        if (workOrder.getUserId() == null || workOrder.getUserId() == 0l) {
            throw new CustomException("该工单尚未指派维修人员!");
        }
        workOrderMapper.updateByPrimaryKey(workOrder);
    }

    public void createWorkOrder(WorkOrderCreteParam workOrderCreteParam) throws CustomException {


        WorkOrder workOrder = new WorkOrder();
        workOrder.setIsAuto(1);
        workOrder.setContent(workOrderCreteParam.getContent());
        workOrder.setEnable(BaseEntity.ENABLE.ENABLE);
        workOrder.setCreateTime(new Date());
        workOrder.setUpdateTime(new Date());
        workOrder.setFaultCode(workOrderCreteParam.getFaultCode());

        String imgUrl = workOrderCreteParam.getImgsUrl();
        if (imgUrl != null && !imgUrl.equalsIgnoreCase("null") || StringUtil.isNotEmpty(imgUrl)) {
            String[] imgs = workOrderCreteParam.getImgsUrl().split(",");

            if (imgs.length > 3)
                throw new CustomException("照片不能多余3张!");

            workOrder.setImgsUrl(workOrderCreteParam.getImgsUrl());
        }
        Warning.CODE1[] code1s = Warning.CODE1.values();
        for (Warning.CODE1 code1 : code1s) {
            if (code1.getCode().equalsIgnoreCase(workOrder.getFaultCode())) {

                workOrder.setFaultName(code1.getMsg());
            }
        }
        workOrder.setStatus(WorkOrder.CODE1.WAIT_REPAIR.getCode());
        workOrder.setStatusName(WorkOrder.CODE1.WAIT_REPAIR.getMsg());
        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getSession().getAttribute("user");
        if (user == null) throw new CustomException("登陆状态失效,请重新登陆系统!");
        workOrder.setExecuterId(user.getId());
        workOrder.setUserId(workOrderCreteParam.getUserId());

        if (workOrderCreteParam.getVedioUrl() != null && !workOrderCreteParam.getVedioUrl().equalsIgnoreCase("null") || StringUtil.isNotEmpty(workOrderCreteParam.getVedioUrl())) {
            String[] imgs = workOrderCreteParam.getVedioUrl().split(",");

            if (imgs.length > 1)
                throw new CustomException("1个工单只能上传1个视频!");

            workOrder.setVedioUrl(workOrderCreteParam.getVedioUrl());
        }

        workOrderMapper.insert(workOrder);
    }


}
