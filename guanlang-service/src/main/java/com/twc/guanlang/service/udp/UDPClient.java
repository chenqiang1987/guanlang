//package com.twc.guanlang.service.udp;
//
//import java.io.IOException;
//import java.io.InterruptedIOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//
//public class UDPClient {
//
//
//    public static void main(String args[]) throws IOException {
////        String str_send = "AA0B00012348660301FA55";
////        byte[] buf = new byte[1024];
////        //客户端在24102端口监听接收到的数据
////        DatagramSocket ds = new DatagramSocket(24102);
////        InetAddress loc = InetAddress.getLocalHost();
////
////        String machineIp = "192.168.1.75";
////        //定义用来发送数据的DatagramPacket实例
////
////
////        byte[] o16bys = SendDemoNew.hex2byte(str_send);
////        DatagramPacket dp_send = new DatagramPacket(o16bys, o16bys.length, InetAddress.getByName("192.168.1.75"), 24101);
////        //定义用来接收数据的DatagramPacket实例
////        DatagramPacket dpReceive = new DatagramPacket(buf, 1024);
////        //设置接收数据时阻塞的最长时间
////        ds.setSoTimeout(TIMEOUT);
////        //重发数据的次数
////        int tries = 0;
////        //是否接收到数据的标志位
////        boolean receivedResponse = false;
////        //直到接收到数据，或者重发次数达到预定值，则退出循环n
////        while (!receivedResponse && tries < MAXNUM) {
////            //发送数据
////            ds.send(dp_send);
////            try {
////                //接收从服务端发送回来的数据
////                ds.receive(dpReceive);
////                //如果接收到的数据不是来自目标地址，则抛出异常
////                if (!dpReceive.getAddress().equals(loc)) {
////                    throw new IOException("Received packet from an umknown source");
////                }
////                //如果接收到数据。则将receivedResponse标志位改为true，从而退出循环
////                receivedResponse = true;
////            } catch (InterruptedIOException e) {
////                //如果接收数据时阻塞超时，重发并减少一次重发的次数
////                tries += 1;
////                System.out.println("Time out," + (MAXNUM - tries) + " more tries...");
////            }
////        }
////        if (receivedResponse) {
////            //如果收到数据，则打印出来
////            System.out.println("client received data from server：");
////            String str_receive = new String(dpReceive.getData(), 0, dpReceive.getLength()) +
////                    " from " + dpReceive.getAddress().getHostAddress() + ":" + dpReceive.getPort();
////            System.out.println(str_receive);
////            //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
////            //所以这里要将dp_receive的内部消息长度重新置为1024
////            dpReceive.setLength(1024);
////        } else {
////            //如果重发MAXNUM次数据后，仍未获得服务器发送回来的数据，则打印如下信息
////            System.out.println("No response -- give up.");
////        }
////        ds.close();
//
//
//        sendUdpOrderAndWaitReceive("AA0B00012348660301FA55", "192.168.1.75", 24101, 24102);
//    }
//
//
//}
