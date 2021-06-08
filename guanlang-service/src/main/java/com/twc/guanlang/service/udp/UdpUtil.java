package com.twc.guanlang.service.udp;

import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.machine.Machine;
import com.twc.guanlang.service.MachineService;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UdpUtil {

    /**
     * udp指令生命周期
     */
    public static Map<String, Long> udpOrderPeriod = new ConcurrentHashMap<>();

    /**
     * 机器人udp检验码算法：
     * <p>
     * 所有字节（从头部字段到尾部字段，除校验码字段外）进行逐字节异或所得
     * 获取16进制字节组的校验码
     *
     * @return
     */
    public static String getCheckCodeByhexByte(String hexStr) {

        byte[] AAbytes = hex2byte(hexStr);
        byte[] bs = new byte[1];
        bs[0] = getXor(AAbytes);
        return bytesToHex(bs);
    }

    /**
     * 机器人udp send模板
     *
     * @return
     */
    public static StringBuffer machineUdpSendTemlate() {

        StringBuffer simulateUdpData = new StringBuffer();
        simulateUdpData.append("AA")
                .append("length")
                .append("timeStamp")
                .append("type")
                .append("data")
                .append("check")
                .append("55");
        return simulateUdpData;
    }

    /**
     * 16进制字符串转成16进制字节
     *
     * @param hex
     * @return
     */
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

    /**
     * 机器人udp send
     *
     * @return
     */
    public static void sendMachineUdp(String udpTemp, String ip, int port) throws IOException {

        udpTemp = udpTemp.toUpperCase();
        DatagramSocket ds = new DatagramSocket();
        byte[] o16bys = hex2byte(udpTemp);
        DatagramPacket dp1 = new DatagramPacket(o16bys, o16bys.length, InetAddress.getByName(ip), port);
        ds.send(dp1);
        //释放资源
        ds.close();
    }

    /**
     * 充电桩udp send模板
     *
     * @return
     */
    public static void sendRechargeUdp(String udpTemp, String ip, int port) throws IOException {

        DatagramSocket ds = new DatagramSocket();
        byte[] bys = hex2byte(udpTemp);
        DatagramPacket dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(ip), port);
        ds.send(dp);
        ds.close();
    }

    /**
     * 充电桩udp send模板
     *
     * @return
     */
    public static StringBuffer rechargeUdpSendTemlate() {

        /**
         *  bit7 -发起源  0表示由充电桩发起  1表示由客户端发起
         *  bit6-4 类型 000反馈类型 001反馈状态
         *  bit3-0 充电桩编号
         *
         *  example:
         *      客户端发起1号充电桩开门请求
         *
         */
        String originalBinary = "source";
        StringBuffer simulateUdpData = new StringBuffer();
        simulateUdpData.append("AA55")
                //length=03
                .append("03")
                //发起标识+状态+充电桩编号  1 001 0001
                .append("type")
                //00关/01开
                .append("data")
                .append("check")
                .append("55AA");
        return simulateUdpData;
    }


    /**
     * 计算十六进制校验位
     *
     * @param hexdata
     * @return
     */
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

    /**
     * 获得机器人udp发送时间戳
     *
     * @return
     */
    public static String machineUdpTimeStamp() {

        String timestampStr = System.currentTimeMillis() + "";

        StringBuffer stringBuffer = new StringBuffer(System.currentTimeMillis() + "");

        stringBuffer.reverse();

        String str = stringBuffer.toString();


        return str.substring(0, 8);
    }

    /**
     * 16进制转10进制
     *
     * @param hex
     * @return
     */
    public static int hex2decimal(String hex) {
        return Integer.parseInt(hex.toUpperCase(), 16);
    }


    /**
     * 10进制整数转成16进制字符串
     *
     * @param number
     * @return
     */
    public static String intToHex(int number) {

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        char[] S = new char[100];
        if (number == 0) {
            stringBuffer.append("00");
        } else {
            while (number != 0) {
                int t = number % 16;
                if (t >= 0 && t < 10) {
                    S[i] = (char) (t + '0');
                    i++;
                } else {
                    S[i] = (char) (t + 'A' - 10);
                    i++;
                }
                number = number / 16;
            }

            for (int j = i - 1; j >= 0; j--) {
                stringBuffer.append(S[j]);
            }
        }


        return stringBuffer.toString();
    }

    /**
     * 小尾短16进制转浮点
     *
     * @param hexStr
     * @return
     */
    public static float hexStr2foloatBylittle(String hexStr) {
        byte[] bytes = hex2byte(hexStr.toUpperCase());
        float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return f;
    }


    /**
     * 小尾短16进制转short
     *
     * @param hexStr
     * @return
     */
    public static short hexStr2ShortBylittle(String hexStr) {
        byte[] bytes = hex2byte(hexStr.toUpperCase());
        Short f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return f;
    }


    public static void main(String[] args) throws CustomException, InterruptedException {


        System.out.println(hexStr2foloatBylittle("9a99d141"));
//        System.out.println(getCheckCodeByhexByte("AA0a0090673343030155").toUpperCase());
//        System.out.println(String.format("%02d", 0));
//        System.out.println(intToHex(13));
//
//        byte[] bytes = hex2byte("CD CC 8C 3F");
//        float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
//        System.out.println(f);
//
//
//        System.out.println(hex2decimal("0c"));
//
//
//        System.out.println(makeChecksum(" 03 91 00"));

//        syncTest();
//
//        syncTest();




    }

    public static void syncTest() throws InterruptedException {

        System.out.println("start========"+Thread.currentThread().getName()+System.currentTimeMillis());
        Thread.sleep(1000*5);
        System.out.println("end========"+Thread.currentThread().getName()+System.currentTimeMillis());
    }

    public static int test(byte[] bytes, int index) {

        if (index == 0) {
            return bytes[0];
        } else {
            return bytes[index] ^ test(bytes, index - 1);
        }
    }


    /**
     * 字节逐个异或
     *
     * @param data
     * @return
     */
    public static byte getXor(byte[] data) {
        byte temp = data[0];
        for (int i = 1; i < data.length; i++) {
            temp ^= data[i];
        }
        return temp;
    }


    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }


    /**
     * 采用这种，取末尾两位作为校验位
     *
     * @param n
     * @return
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }


    //System.arraycopy()方法
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }


    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    //设置接收数据的超时时间
    private static final int TIMEOUT = 100;
    //设置重发数据的最多次数
    private static final int MAXNUM = 3;

    /**
     * 给机器人发送udp指令
     * 重连机制
     * 响应超时
     *
     * @param order
     * @return
     */
    public static synchronized String sendUdpOrderAndWaitReceive(String order, Machine machine, Integer revPort) throws CustomException {

        DatagramSocket ds = null;
        String str_receive = null;
        String rs;
        try {
            ds = new DatagramSocket(revPort);
            byte[] buf = new byte[1024];
            //客户端在9000端口监听接收到的数据
            InetAddress loc = InetAddress.getLocalHost();
            byte[] o16bys = SendDemoNew.hex2byte(order.toUpperCase());
            //定义用来发送数据的DatagramPacket实例
            DatagramPacket dp_send = new DatagramPacket(o16bys, o16bys.length, InetAddress.getByName(machine.getIp()), machine.getPort());
            //定义用来接收数据的DatagramPacket实例
            DatagramPacket dp_receive = new DatagramPacket(buf, 1024);
            //设置接收数据时阻塞的最长时间
            ds.setSoTimeout(TIMEOUT);
            //重发数据的次数
            int tries = 0;
            //是否接收到数据的标志位
            boolean receivedResponse = false;
            //直到接收到数据，或者重发次数达到预定值，则退出循环
            while (!receivedResponse && tries < MAXNUM) {
                //发送数据
                ds.send(dp_send);
                try {
                    //接收从服务端发送回来的数据
                    ds.receive(dp_receive);
                    //如果接收到的数据不是来自目标地址，则抛出异常
                    if (!dp_receive.getAddress().getHostAddress().equals(machine.getIp())) {
                        throw new IOException("Received packet from an umknown source");
                    }
                    //如果接收到数据。则将receivedResponse标志位改为true，从而退出循环
                    receivedResponse = true;
                } catch (InterruptedIOException e) {
                    //如果接收数据时阻塞超时，重发并减少一次重发的次数
                    tries += 1;
//                    System.out.println("Time out," + (MAXNUM - tries) + " more tries...");
                }
            }
            if (receivedResponse) {
                //如果收到数据，则打印出来
                //获取数据

                MachineService.heartMap.put(machine.getId(), System.currentTimeMillis());
                MachineService.onLineMap.put(machine.getId(), "on");
                String data = new String(dp_receive.getData(), 0, dp_receive.getLength());
                StringBuffer stringBuffer = new StringBuffer(data);
                for (byte b : dp_receive.getData()) {
                    stringBuffer.append(String.format("%02x", b));
                }
                str_receive = stringBuffer.toString();
                //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
                //所以这里要将dp_receive的内部消息长度重新置为1024
                dp_receive.setLength(1024);
            } else {
                //如果重发MAXNUM次数据后，仍未获得服务器发送回来的数据，则打印如下信息
//                System.out.println("No response -- give up.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("发送UDP指令失败" + e.getMessage());
        } finally {
            if(ds!=null)
            ds.close();
        }

        if (StringUtil.isEmpty(str_receive)) return null;
        rs = str_receive;
//        System.out.println(" 收到机器人" + machine.getIp() + "udp原始消息============:" + rs);
        int start = rs.indexOf("bb");
        int end = rs.lastIndexOf("66");
        if (start == -1 || end == -1) {
            throw new CustomException("接收到机器人错误的udp消息:" + rs);
        }
        rs = rs.substring(start, end + 2);
//        System.out.println(" 收到机器人" + machine.getIp() + "udp消息，去除乱码============:" + rs);
        return rs;
    }

}