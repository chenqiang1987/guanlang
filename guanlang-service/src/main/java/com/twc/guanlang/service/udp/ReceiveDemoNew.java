package com.twc.guanlang.service.udp;

import java.io.IOException;
import java.net.*;

import static com.twc.guanlang.service.RechargeUdpService.getHexBytes;

/**
 *
 * 20210329
 * @author chenqiang
 */
public class ReceiveDemoNew {
    public static void main(String[] args) {
//        //创建发送端的Socket服务对象
//        //public DatagramSocket()
//        DatagramSocket ds = null;
//        try {
//            ds = new DatagramSocket(4000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        byte[] bys = new byte[1024];
//        DatagramPacket dp = new DatagramPacket(bys, bys.length);
//        //接收数据
//        try {
//            ds.receive(dp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //解析数据
        //获取ip地址
//        String ip = dp.getAddress().getHostAddress();
//
//        //获取数据
//        String data = new String(dp.getData(), 0, dp.getLength());
//
//        System.out.println(" 接收到的数据======" + data);


        //定义一个端口号
        int port = 4000;
        try {
            //创建接收方的套接字,监听端口号
            DatagramSocket getSocket = new DatagramSocket(port);
            while (true) {
                //确定接收的数据报文的长度，来建立缓冲区
                byte[] buf = new byte[36];
                //创建接收类型的数据包，数据先储存在缓冲区
                DatagramPacket getPacket = new DatagramPacket(buf, buf.length);
                //通过套接字接收数据
                getSocket.receive(getPacket);
                //解析接收到到16机制数据
                byte[] bytes = getPacket.getData();
                String data = getBufHexStr(bytes);
                //获取ip地址
                String ip = getPacket.getAddress().getHostAddress();

                System.out.println("充電樁IP："+ip);
                System.out.println(data+"======================");
              //  backHeadle(getSocket, getPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    //将16进制的byte数组转换成字符串
    public static String getBufHexStr(byte[] raw){
        String HEXES = "0123456789ABCDEF";
        if ( raw == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }


    //回复数据
    private static void backHeadle( DatagramSocket getSocket,DatagramPacket getPacket) {
        //通过数据包得到发送方的套接字ip
        SocketAddress sendAddress = getPacket.getSocketAddress();
        //确定要回复给发送方的消息内容，并转换成字符数组
        String feedback = "211502a0";
        //由于16进制字符发送时只能发送字节，这里讲字符串转换成字节
        byte bytes[] = getHexBytes(feedback);
        //创建发送类型的数据包
        DatagramPacket sendPacket = new DatagramPacket(bytes,bytes.length,sendAddress);
        //通过套接字发送数据
        try {
            getSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}