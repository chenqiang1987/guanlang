package com.twc.guanlang.service.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * 充电桩门send模拟
 */
public class SendDemoNew {
    public static void main(String[] args) throws IOException {

        DatagramSocket ds = new DatagramSocket(4000);


//        byte[] bys =hex2byte("AA 55 03 91 00 94 55 AA");
//        String s = makeChecksum("03 91 00");

        //重启
        byte[] bys = hex2byte("AA 55 03 A1 01 A5 55 AA");
        String s = makeChecksum("03 A1 00");

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


    public static String makeChecksum(String hexdata) {
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

    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    private static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }


}