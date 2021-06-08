package com.twc.guanlang;

import com.twc.guanlang.service.MachineService;
import com.twc.guanlang.service.VedioService;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.FileNotFoundException;


@SpringBootApplication
//@MapperScan(value = "com.twc.guanlang.mapper.entity")
@EnableTransactionManagement

@EnableScheduling
public class GuanlangApplication {
    public static void main(String[] args) throws FileNotFoundException {

        SpringApplication.run(GuanlangApplication.class, args);

//        try {
//            VedioService.push("rtmp://open.stratosphere.mobi:1935/live/1", "rtsp://admin:admin123@192.168.1.63:554/mainstream", 0, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}