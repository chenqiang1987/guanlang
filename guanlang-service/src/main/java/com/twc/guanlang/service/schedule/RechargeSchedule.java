package com.twc.guanlang.service.schedule;

import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.AutoMession;
import com.twc.guanlang.entity.machine.Machine;
import com.twc.guanlang.entity.machine.UdpCommandMachine;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.mapper.entity.AutoMessionMapper;
import com.twc.guanlang.mapper.entity.MachineMapper;
import com.twc.guanlang.mapper.entity.RechargeMapper;
import com.twc.guanlang.mapper.entity.UdpCommandMachineMapper;
import com.twc.guanlang.param.machine.RechargeParam;
import com.twc.guanlang.param.machine.StandbyParam;
import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.MachineUdpService;
import com.twc.guanlang.service.PositionService;
import com.twc.guanlang.service.RechargeUdpService;
import com.twc.guanlang.service.udp.UdpUtil;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *   充电桩监听程序
 * @author chenqiang
 */
@Slf4j
@Service
public class RechargeSchedule {

    @Autowired
    UdpCommandMachineMapper udpCommandMachineMapper;
    @Autowired
    private RechargeMapper rechargeMapper;

    /**
     * 项目启动10秒后  开始检测心跳数据
     */
    @Scheduled(fixedRate = 10 * 1000, initialDelay = 10 * 1000)
    public void rechargeHeart() {
        List<Recharge> rechargeList;
        rechargeList = rechargeMapper.selectAll();
        for (Recharge recharge : rechargeList) {
            Long heart = RechargeUdpService.heartMap.get(recharge.getId());
            if (heart == null || System.currentTimeMillis() - heart > 10 * 1000) {
                RechargeUdpService.onLineMap.put(recharge.getId(), "off");
            } else if (System.currentTimeMillis() - heart < 10 * 1000 || System.currentTimeMillis() - heart == 10 * 1000) {
                RechargeUdpService.onLineMap.put(recharge.getId(), "on");
            }
        }
    }
}