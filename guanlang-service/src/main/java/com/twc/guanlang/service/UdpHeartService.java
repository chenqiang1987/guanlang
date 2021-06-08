package com.twc.guanlang.service;

import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.MonitorData;
import com.twc.guanlang.mapper.entity.MonitorDataMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;


/**
 * udp心跳检测
 */
@Slf4j
@Configuration
public class UdpHeartService {


    @Resource
    private MonitorDataMapper monitorDataMapper;



}
