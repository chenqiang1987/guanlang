package com.twc.guanlang.service.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 发送指令到仿真机器人终端
 * <p>
 * <p>
 * 发送UDP的定时任务
 * 每隔10秒发送一次空气质量指令到机器人
 * <p>
 * <p>
 * <p>
 * 接收UDP的定时任务
 *
 * @author chenqiang
 */
public class SendDemo2Emulation {

    public static void xorTest() {
    }

    public static void main(String[] args) throws IOException {

        DatagramSocket ds = new DatagramSocket();
        //  String msg="AA 0A 00 99 6D 57 90 20 E6 55";
        String msg = "AA0a0065271432029355";
        byte[] o16bys = SendDemoNew.hex2byte(msg.toUpperCase());
        DatagramPacket dp1 = new DatagramPacket(o16bys, o16bys.length, InetAddress.getByName("192.168.1.75"), 24101);
        ds.send(dp1);
        //释放资源
        ds.close();
    }
}