package com.twc.guanlang.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketTest {

    public SocketTest() throws IOException {
    }

    public static void main(String[] args) throws IOException {

//        try {
//            DatagramSocket ds = new DatagramSocket();
//            String s = "hello";
//            byte[] bys = new byte[1024];
//            DatagramPacket dp = new DatagramPacket(bys, bys.length, InetAddress.getByName("192.168.1.123"), 1234);
//            ds.send(dp);
//            //释放资源
//            ds.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    Socket socket=new Socket("127.0.0.1",1234);
    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
}
