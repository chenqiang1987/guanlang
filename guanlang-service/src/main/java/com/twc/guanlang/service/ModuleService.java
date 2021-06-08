package com.twc.guanlang.service;


import com.twc.guanlang.common.api.ApiResultBean;
import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.mapper.entity.*;
import com.twc.guanlang.param.machine.*;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ModuleService {


    @Autowired
    private ModuleMapper moduleMapper;


    @Autowired
    private Module2MachineMapper module2MachineMapper;


    public List<Module2Machine> getValue(Integer machineId) {

        Example example = new Example(Module2Machine.class);
        example.createCriteria().andEqualTo("machineId", machineId);
        return module2MachineMapper.selectByExample(example);

    }


    public List<Module> getAll() {

        return moduleMapper.selectAll();

    }


    public List<Module> getModuleFromMachine(String machineId) {

        Example example = new Example(Module2Machine.class);
        example.createCriteria().andEqualTo("machineId", machineId);
        List<Module2Machine> list = module2MachineMapper.selectByExample(example);


        List<Module> list1 = new ArrayList<>();
        for (Module2Machine module2Machine : list) {

            Integer mod = module2Machine.getModuleId();

            list1.add(moduleMapper.selectByPrimaryKey(mod));
        }

        return list1;
    }


    @Transactional
    public void updateValueForModule(SetValueForModuleParam setValueForModuleParam) throws CustomException {


        /**
         * 更新目标机器人阈值
         */
        Example example = new Example(Module2Machine.class);
        example.createCriteria().andEqualTo("machineId", setValueForModuleParam.getMachineId());


        module2MachineMapper.deleteByExample(example);


        /**
         * 每次设定机器人 则更新默认阈值
         */
        List<Module> all = moduleMapper.selectAll();
        for (Module module : all) {

            Module2Machine module2Machine = new Module2Machine();
            module2Machine.setMachineId(setValueForModuleParam.getMachineId().longValue());
            module2Machine.setModuleId(module.getId());
            String values[] = null;
            if (module.getType().equalsIgnoreCase(Module.CODE1.CO.getCode())) {

                values = setValueForModuleParam.getCO().split(",");
                module2Machine.setModuleCode("CO");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.HUMIDITY.getCode())) {

                values = setValueForModuleParam.getHUMIDITY().split(",");
                module2Machine.setModuleCode("HUMIDITY");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.HYDROTHION.getCode())) {

                values = setValueForModuleParam.getHYDROTHION().split(",");
                module2Machine.setModuleCode("HYDROTHION");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.METHANCE.getCode())) {

                values = setValueForModuleParam.getMETHANCE().split(",");
                module2Machine.setModuleCode("METHANCE");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.OXYGEN.getCode())) {

                values = setValueForModuleParam.getOXYGEN().split(",");
                module2Machine.setModuleCode("OXYGEN");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.PM1.getCode())) {

                values = setValueForModuleParam.getPM1().split(",");
                module2Machine.setModuleCode("PM1");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.PM10.getCode())) {
                values = setValueForModuleParam.getPM10().split(",");
                module2Machine.setModuleCode("PM10");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.PM25.getCode())) {

                values = setValueForModuleParam.getPM25().split(",");
                module2Machine.setModuleCode("PM25");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.TEMPERATURE.getCode())) {

                values = setValueForModuleParam.getTEMPERATURE().split(",");
                module2Machine.setModuleCode("TEMPERATURE");
            } else if (module.getType().equalsIgnoreCase(Module.CODE1.SMOKE.getCode())) {
                values = setValueForModuleParam.getSMOKE().split(",");
                module2Machine.setModuleCode("SMOKE");

            }
            Double max = Double.parseDouble(values[0]);
            Double min = Double.parseDouble(values[1]);
            if (max < min) {
                throw new CustomException(ApiResultBean.CODE1.PARAM_ERROR.getCode(), "最大值不能小于最小值");
            }
            module.setMaxValue(Double.parseDouble(values[0]));
            module2Machine.setMax(Double.parseDouble(values[0]));
            module.setMinValue(Double.parseDouble(values[1]));
            module2Machine.setMin(Double.parseDouble(values[1]));
            module2Machine.setIsShow(1);
//            module.setWarningValue(values[2]);
            module.setUpdateTime(new Date());
            moduleMapper.updateByPrimaryKey(module);
            module2Machine.setCreateTime(new Date());
            module2Machine.setUpdateTime(new Date());
            module2MachineMapper.insert(module2Machine);
        }
    }


    @Autowired
    private MonitorDataMapper monitorDataMapper;

    /**
     * Map<String,Map<String,List>>
     * <p>
     * 模块key--> 数据-->时间key--数据
     *
     * @param curveGraphParam
     * @return
     */
    public  Map<String, List> reportCurve(CurveGraphParam curveGraphParam) throws CustomException {

        if (curveGraphParam.getMachineId() == null) {
            throw new CustomException(ApiResultBean.CODE1.PARAM_NULL.getCode(), "机器id不能为空");
        }
        Example example = new Example(MonitorData.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("machineId", curveGraphParam.getMachineId());
        if (curveGraphParam.getStart() != null && StringUtil.isNotEmpty(curveGraphParam.getStart())
                && curveGraphParam.getEnd() != null && StringUtil.isNotEmpty(curveGraphParam.getEnd())
        ) {
            criteria.andBetween("createTime", curveGraphParam.getStart(), curveGraphParam.getEnd());
        } else {
            throw new CustomException(ApiResultBean.CODE1.PARAM_NULL.getCode(), "开始和结束时间不能为空");
        }
        List<MonitorData> datas = monitorDataMapper.selectByExample(example);
        Map<String, List> map = new HashMap();
        for (Module.CODE1 code1 : Module.CODE1.values()) {
            map.put(code1.getCode(), new ArrayList<>());
        }
        for (MonitorData monitorData : datas) {
            for (String key : map.keySet()) {
                if (key.equalsIgnoreCase(Module.CODE1.CO.getCode())) {
                    map.get(Module.CODE1.CO.getCode()).add(monitorData.getCo());
                } else if (key.equalsIgnoreCase(Module.CODE1.HYDROTHION.getCode())) {
                    map.get(Module.CODE1.HYDROTHION.getCode()).add(monitorData.getHydrothion());
                } else if (key.equalsIgnoreCase(Module.CODE1.SMOKE.getCode())) {
                    map.get(Module.CODE1.SMOKE.getCode()).add(monitorData.getSmoke());
                } else if (key.equalsIgnoreCase(Module.CODE1.TEMPERATURE.getCode())) {
                    map.get(Module.CODE1.TEMPERATURE.getCode()).add(monitorData.getTemperature());
                } else if (key.equalsIgnoreCase(Module.CODE1.OXYGEN.getCode())) {
                    map.get(Module.CODE1.OXYGEN.getCode()).add(monitorData.getOxygen());
                } else if (key.equalsIgnoreCase(Module.CODE1.METHANCE.getCode())) {
                    map.get(Module.CODE1.METHANCE.getCode()).add(monitorData.getMethance());
                } else if (key.equalsIgnoreCase(Module.CODE1.HUMIDITY.getCode())) {
                    map.get(Module.CODE1.HUMIDITY.getCode()).add(monitorData.getHumidity());
                } else if (key.equalsIgnoreCase(Module.CODE1.PM10.getCode())) {
                    map.get(Module.CODE1.PM10.getCode()).add(monitorData.getPm10());
                } else if (key.equalsIgnoreCase(Module.CODE1.PM25.getCode())) {
                    map.get(Module.CODE1.PM25.getCode()).add(monitorData.getPm25());
                } else if (key.equalsIgnoreCase(Module.CODE1.PM1.getCode())) {
                    map.get(Module.CODE1.PM1.getCode()).add(monitorData.getPm1());
                }
            }
        }
        return map;
    }
}
