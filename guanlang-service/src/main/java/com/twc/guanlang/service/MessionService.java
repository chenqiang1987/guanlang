package com.twc.guanlang.service;


import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.AutoMession;
import com.twc.guanlang.entity.machine.AutoMessionHistory;
import com.twc.guanlang.entity.machine.Machine;
import com.twc.guanlang.mapper.entity.AutoMessionHistoryMapper;
import com.twc.guanlang.mapper.entity.AutoMessionMapper;
import com.twc.guanlang.mapper.entity.MachineMapper;
import com.twc.guanlang.param.machine.*;
import com.twc.guanlang.vo.MachineWebScoketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 机器人任务服务类
 */
@Service
public class MessionService {

    @Resource
    private MachineMapper machineMapper;
    @Autowired
    private AutoMessionMapper autoMessionMapper;
    @Autowired
    MachineService machineService;
    @Autowired
    private AutoMessionHistoryMapper autoMessionHistoryMapper;

    /**
     * 设置巡检周期
     *
     * @param startParam
     * @throws CustomException
     */
    public void setPeriod(SetPeriodParam startParam) throws CustomException {

        Example example = new Example(Machine.class);
        example.createCriteria().andEqualTo("enable", BaseEntity.ENABLE.ENABLE).andEqualTo("serialNumber", startParam.getNumber());
        Machine machine = machineMapper.selectOneByExample(example);
        if (machine == null)
            throw new CustomException("机器人不存在");
//        machine.setStatus(Machine.STATUS.StandingBy);
        machineMapper.updateByPrimaryKey(machine);

//        MachineService.map.remove(startParam.getNumber());
    }

    @Transactional
    public void addAutoMession(AutoMessionParam autoMessionParam) throws CustomException {

//        if (MachineService.WORK_STATUS == null) {
//            throw new CustomException("正在获取机器人状态,请稍后重试");
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AutoMession autoMession = new AutoMession();
        if (autoMessionParam.getType().equalsIgnoreCase(AutoMession.TYPE_ENUM.AUTO.getCode())) {

            Example example = new Example(AutoMession.class);
            example.createCriteria().andEqualTo("machineId", autoMessionParam.getMachineId()).andEqualTo("type", "AUTO");
            autoMessionMapper.deleteByExample(example);
            Date nowDate = new Date();
            for (String startStr : autoMessionParam.getStart()) {
//                    Date startDate = simpleDateFormat.parse(startStr);
//                    if (startDate.before(nowDate)) {
//                        throw new CustomException("开始时间不能小于当前时间");
//                    }

                try{

                    AutoMession autoMession1 = new AutoMession();
                    autoMession1.setCreateTime(new Date());
                    autoMession1.setUpdateTime(new Date());
                    autoMession1.setStart(startStr);
                    autoMession1.setMachineId(autoMessionParam.getMachineId());
                    autoMession1.setEnable(BaseEntity.ENABLE.ENABLE);
                    autoMession1.setType(AutoMession.TYPE_ENUM.AUTO.getCode());
                    autoMession1.setStatus(AutoMession.STATUS_ENUM.WAIT_EXECUTE.getCode());
                    autoMessionMapper.insert(autoMession1);
                }catch(Exception e){


                    e.printStackTrace();
                }

            }
        } else if (autoMessionParam.getType().equalsIgnoreCase(AutoMession.TYPE_ENUM.HALF.getCode())) {


            if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HOLDING.getCode())
                    || MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HALF_WORKING.getCode())
                    || MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HAND_WORKING.getCode())
            ) {
                AutoMession autoMession1 = new AutoMession();
                autoMession1.setCreateTime(new Date());
                autoMession1.setUpdateTime(new Date());
//                    autoMession1.setStart(new Date());
                autoMession1.setMachineId(autoMessionParam.getMachineId());
                autoMession1.setEnable(BaseEntity.ENABLE.ENABLE);
                autoMession1.setType(AutoMession.TYPE_ENUM.HALF.getCode());
                autoMession1.setStatus(AutoMession.STATUS_ENUM.EXECUTING.getCode());
                autoMession1.setHalfTarget(autoMessionParam.getTargetPositon());
                autoMession1.setStartedAt(new Date());
                autoMessionMapper.insert(autoMession1);
                goToTarget(autoMessionParam.getTargetPositon());
                return;
            }

            if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.CHARGIN.getCode())) {
                throw new CustomException("机器正在执行充电任务,任务创建失败");
            }
            AutoMession autoMession1 = new AutoMession();
            autoMession1.setCreateTime(new Date());
            autoMession1.setUpdateTime(new Date());
//                autoMession1.setStart(new Date());
            autoMession1.setMachineId(autoMessionParam.getMachineId());
            autoMession1.setEnable(BaseEntity.ENABLE.ENABLE);
            autoMession1.setType(AutoMession.TYPE_ENUM.AUTO.getCode());
            autoMession1.setStatus(AutoMession.STATUS_ENUM.EXECUTING.getCode());
            autoMession1.setHalfTarget(autoMessionParam.getTargetPositon());
            autoMessionMapper.insert(autoMession1);

            StandbyParam standbyParam = new StandbyParam();
            standbyParam.setId(autoMessionParam.getMachineId());
            standbyParam.setHalfOrHand("HALF");
            machineService.callStandby(standbyParam);
        } else if (autoMessionParam.getType().equalsIgnoreCase(AutoMession.TYPE_ENUM.HAND.getCode())) {

            if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HOLDING.getCode())
                    || MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HALF_WORKING.getCode())
                    || MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.HAND_WORKING.getCode())
            ) {
                AutoMession autoMession1 = new AutoMession();
                autoMession1.setCreateTime(new Date());
                autoMession1.setUpdateTime(new Date());
//                    autoMession1.setStart(new Date());
                autoMession1.setMachineId(autoMessionParam.getMachineId());
                autoMession1.setEnable(BaseEntity.ENABLE.ENABLE);
                autoMession1.setType(AutoMession.TYPE_ENUM.HAND.getCode());
                autoMession1.setStatus(AutoMession.STATUS_ENUM.EXECUTING.getCode());
                autoMession1.setStartedAt(new Date());
                autoMessionMapper.insert(autoMession1);
                return;
            }

            if (MachineService.WORK_STATUS.equalsIgnoreCase(Machine.WORK_STATUS.CHARGIN.getCode())) {
                throw new CustomException("机器正在执行充电任务,任务创建失败");
            }
            AutoMession autoMession1 = new AutoMession();
            autoMession1.setCreateTime(new Date());
            autoMession1.setUpdateTime(new Date());
//                autoMession1.setStart(new Date());
            autoMession1.setMachineId(autoMessionParam.getMachineId());
            autoMession1.setEnable(BaseEntity.ENABLE.ENABLE);
            autoMession1.setType(AutoMession.TYPE_ENUM.AUTO.getCode());
            autoMession1.setStatus(AutoMession.STATUS_ENUM.EXECUTING.getCode());
            autoMessionMapper.insert(autoMession1);

            StandbyParam standbyParam = new StandbyParam();
            standbyParam.setId(autoMessionParam.getMachineId());
            standbyParam.setHalfOrHand("HAND");
            machineService.callStandby(standbyParam);
        }
    }

    @Transactional
    public void endHalfAndHandMession(Long messionId) throws CustomException {
        AutoMession autoMession = autoMessionMapper.selectByPrimaryKey(messionId);
        autoMession.setStatus(AutoMession.STATUS_ENUM.COMPLETE.getCode());
        autoMession.setUpdateTime(new Date());
        //全局标记机器人进入待机状态
        MachineService.WORK_STATUS = Machine.WORK_STATUS.HOLDING.getCode();

    }

    /**
     * 发送指令让机器人移动到某个位置
     *
     * @param target
     */
    public void goToTarget(String target) {

    }

    @Transactional
    public List<AutoMession> getAutoMessions(Integer machineId) {

        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("machineId", machineId);
        return autoMessionMapper.selectByExample(example);

    }

    @Transactional
    public void delAutoMession(Integer messionId) {

        Example example = new Example(AutoMession.class);
        example.createCriteria().andEqualTo("id", messionId);
        AutoMession autoMession = autoMessionMapper.selectByPrimaryKey(messionId);
        autoMession.setEnable(BaseEntity.ENABLE.DISABLE);
        autoMessionMapper.updateByPrimaryKey(autoMession);
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //不同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else    //同一年
        {
            return day2 - day1;
        }
    }

    /**
     * 把过期的任务移动到历史表中
     *
     * @param messionId
     * @throws ParseException
     */
//    @Scheduled(cron = "0/20 * * * * *")
    @Transactional
    public void moveAutoMession2History() throws ParseException {
//        List<AutoMession> all = autoMessionMapper.selectAll();
//        Date now = new Date();
//        if (all != null && all.size() > 0) {
//            for (AutoMession autoMession : all) {
//                int dif = differentDays(autoMession.getStart(), new Date());
//                if (dif > 0) {
//                    autoMessionMapper.delete(autoMession);
//                    AutoMessionHistory autoMessionHistory = new AutoMessionHistory();
//                    autoMessionHistory.setCreateTime(new Date());
//                    autoMessionHistory.setUpdateTime(new Date());
//                    autoMessionHistory.setContent(autoMession.getContent());
//                    autoMessionHistory.setStart(autoMession.getStart());
//                    autoMessionHistory.setEnd(autoMession.getEnd());
//                    autoMessionHistoryMapper.insert(autoMessionHistory);
//                }
//            }
//        }
    }
}
