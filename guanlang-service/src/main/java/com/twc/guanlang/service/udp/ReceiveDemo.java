package com.twc.guanlang.service.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * @author chenqiang
 */
@Slf4j
public class ReceiveDemo {
    public static void main(String[] args) throws IOException {

        //创建接收端的Socket服务对象，并且指定端口号
        DatagramSocket ds = new DatagramSocket(24102);
        while (true) {
            //创建一个数据包，用于接收数据
            byte[] bys = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bys, bys.length);
            //接收数据
            ds.receive(dp);
            //解析数据
            //获取ip地址
            String ip = dp.getAddress().getHostAddress();

            //获取数据
            String data = new String(dp.getData(), 0, dp.getLength());

            for(byte b:dp.getData()){
                System.out.print( String.format("%02x",b));
            }
//            System.out.println("监听到数据========="+data);

        }


        //释放资源(服务器一般永远是开着的）
        //ds.close();
    }

    public ReceiveDemo() throws IOException {

        //创建接收端的Socket服务对象，并且指定端口号
        DatagramSocket ds = new DatagramSocket(10010);
        log.info("启动udp监听..........................");
        while (true) {
            //创建一个数据包，用于接收数据
            byte[] bys = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bys, bys.length);
            //接收数据
            ds.receive(dp);

            //解析数据
            //获取ip地址
            String ip = dp.getAddress().getHostAddress();

            //获取数据
            String data = new String(dp.getData(), 0, dp.getLength());

            log.info(" 监听到数据==========:" + data);

        }
    }


    public static class SendDemoNew {
        public static void main(String[] args) throws IOException {

            DatagramSocket ds = new DatagramSocket();


            byte[] bys =hex2byte("AA 55 03 91 00 13 55 AA");
            String s=makeChecksum("03 91 00");

            DatagramPacket dp = new DatagramPacket(bys, bys.length, InetAddress.getByName("192.168.9.176"), 4001);

            //通过Socket对象的发送功能发送数据包0
            //public void send(DatagramPacket p)
            ds.send(dp);

            //释放资源
            ds.close();
        }



        public static byte[] hex2byte(String hex) {
            String digital = "0123456789ABCDEF";
            String hex1 = hex.replace(" ", "");
            char[] hex2char = hex1.toCharArray();
            byte[] bytes = new byte[hex1.length() / 2];
            byte temp;
            for (int p = 0; p < bytes.length; p++) {
                temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
                temp += digital.indexOf(hex2char[2 * p + 1]);
                bytes[p] = (byte) (temp & 0xff);
            }
            return bytes;
        }

    //    byte [] asd =hex2byte(message);



        public  static String makeChecksum(String hexdata) {
            if (hexdata == null || hexdata.equals("")) {
                return "00";
            }
            hexdata = hexdata.replaceAll(" ", "");
            int total = 0;
            int len = hexdata.length();
            if (len % 2 != 0) {
                return "00";
            }
            int num = 0;
            while (num < len) {
                String s = hexdata.substring(num, num + 2);
                total += Integer.parseInt(s, 16);
                num = num + 2;
            }
            return hexInt(total);
        }

        private  static String hexInt(int total) {
            int a = total / 256;
            int b = total % 256;
            if (a > 255) {
                return hexInt(a) + format(b);
            }
            return format(a) + format(b);
        }

        private  static String format(int hex) {
            String hexa = Integer.toHexString(hex);
            int len = hexa.length();
            if (len < 2) {
                hexa = "0" + hexa;
            }
            return hexa;
        }
    }
}
