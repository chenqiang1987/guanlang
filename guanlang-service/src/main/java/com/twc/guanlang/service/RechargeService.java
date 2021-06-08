package com.twc.guanlang.service;

import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.mapper.entity.RechargeMapper;
import com.twc.guanlang.param.machine.CloseDoorParam;
import com.twc.guanlang.param.machine.OpenDoorParam;

import com.twc.guanlang.param.machine.RebootRechargeParam;
import com.twc.guanlang.service.udp.UdpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.util.List;


@Service
public class RechargeService {


    @Resource
    private RechargeMapper rechargeMapper;
    private List<Recharge> rechargeList;

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

    public  static  void openAll(List<Recharge>  list) throws CustomException {


        DatagramSocket ds = null;

        for(Recharge recharge:list){


            try {
                ds = new DatagramSocket(recharge.getPort());
            } catch (SocketException e) {

                e.printStackTrace();
                throw new CustomException("充电桩开门申请失败:" + e.getMessage());
            }

            String numberHex = UdpUtil.intToHex(recharge.getId().intValue());
            numberHex = "9" + numberHex;
            String orderStr = "03 " + numberHex + " 01";
            String check = makeChecksum("03 " + numberHex + " 01");
            check = check.substring(2, 4);
            orderStr = "AA 55 " + orderStr + " " + check + " 55 AA";
            byte[] bys = hex2byte(orderStr);
            DatagramPacket dp = null;
            try {
                dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(recharge.getIp()), recharge.getPort());
            } catch (UnknownHostException e) {

                e.printStackTrace();
                throw new CustomException("充电桩开门申请失败:" + e.getMessage());
            }
            try {
                ds.send(dp);
                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);


                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

                Thread.sleep(1*1000);
                ds.send(dp);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new CustomException("关门请求upd指令发送失败:" + e.getMessage());
            } finally {
                //释放资源
                ds.close();
            }
        }
    }


    public void openDoor(OpenDoorParam openDoorParam) throws CustomException {
        Example example = new Example(Recharge.class);
        example.createCriteria().andEqualTo("number", openDoorParam.getId());
        List<Recharge> recharges = rechargeMapper.selectByExample(example);
        Recharge recharge = recharges.get(0);
        if (recharges == null || recharges.size() == 0)
            throw new CustomException("充电桩编号不存在!");

        if (openDoorParam.getId() > 15l) {
            throw new CustomException("充电桩id不能大于15");
        }
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(recharge.getPort());
        } catch (SocketException e) {

            e.printStackTrace();
            throw new CustomException("充电桩开门申请失败:" + e.getMessage());
        }

        String numberHex = UdpUtil.intToHex(openDoorParam.getId().intValue());
        numberHex = "9" + numberHex;
        String orderStr = "03 " + numberHex + " 01";
        String check = makeChecksum("03 " + numberHex + " 01");
        check = check.substring(2, 4);
        orderStr = "AA 55 " + orderStr + " " + check + " 55 AA";
        byte[] bys = hex2byte(orderStr);
        DatagramPacket dp = null;
        try {
            dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(recharge.getIp()), recharge.getPort());
        } catch (UnknownHostException e) {

            e.printStackTrace();
            throw new CustomException("充电桩开门申请失败:" + e.getMessage());
        }
        //通过Socket对象的发送功能发送数据包0
        //public void send(DatagramPacket p)
        try {
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);



//            Thread.sleep(16*1000);
//            String state=RechargeUdpService.onLineMap.get(openDoorParam.getId());
//            if(!state.equalsIgnoreCase(Recharge.RECHARGE_UDP_DOOR_RES_CODE.OPENED.getCode())){
//
//                System.out.println("     16秒后门依然没有打开，重新发送.....................  ");
//                ds.send(dp);
//            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException("关门请求upd指令发送失败:" + e.getMessage());
        } finally {
            //释放资源
            ds.close();
        }
    }

    public void closeDoor(CloseDoorParam closeDoorParam) throws CustomException {
        Example example = new Example(Recharge.class);
        example.createCriteria().andEqualTo("number", closeDoorParam.getId());
        List<Recharge> recharges = rechargeMapper.selectByExample(example);
        Recharge recharge = recharges.get(0);
        if (recharges == null || recharges.size() == 0)
            throw new CustomException("充电桩编号不存在!");

        if (closeDoorParam.getId() > 15l) {
            throw new CustomException("充电桩id不能大于15");
        }
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(recharge.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }

        String numberHex = UdpUtil.intToHex(closeDoorParam.getId().intValue());
        numberHex = "9" + numberHex;
        String orderStr = "03 " + numberHex + " 00";
        String check = makeChecksum("03 " + numberHex + " 00");
        check = check.substring(2, 4);
        orderStr = "AA 55 " + orderStr + " " + check + " 55 AA";
        byte[] bys = hex2byte(orderStr);
        DatagramPacket dp = null;
        try {
            dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(recharge.getIp()), recharge.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //通过Socket对象的发送功能发送数据包0
        //public void send(DatagramPacket p)
        try {
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);


            Thread.sleep(1*1000);
            ds.send(dp);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException("关门请求upd指令发送失败:" + e.getMessage());
        } finally {
            //释放资源
            ds.close();
        }
    }


    public void reboot(RebootRechargeParam closeDoorParam) throws CustomException {
        Example example = new Example(Recharge.class);
        example.createCriteria().andEqualTo("number", closeDoorParam.getId());
        List<Recharge> recharges = rechargeMapper.selectByExample(example);
        Recharge recharge = recharges.get(0);
        if (recharges == null || recharges.size() == 0)
            throw new CustomException("充电桩编号不存在!");

        if (closeDoorParam.getId() > 15l) {
            throw new CustomException("充电桩id不能大于15");
        }
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(recharge.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String numberHex = UdpUtil.intToHex(closeDoorParam.getId().intValue());
        numberHex = "A" + numberHex;
        String orderStr = "03 " + numberHex + " 01";
        String check = makeChecksum("03 " + numberHex + " 01");
        check = check.substring(2, 4);
        check=check.toUpperCase();
        orderStr = "AA 55 " + orderStr + " " + check + " 55 AA";
        byte[] bys = hex2byte(orderStr);
        DatagramPacket dp = null;
        try {
            dp = new DatagramPacket(bys, bys.length, InetAddress.getByName(recharge.getIp()), recharge.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //通过Socket对象的发送功能发送数据包0
        //public void send(DatagramPacket p)
        try {
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("关门请求upd指令发送失败:" + e.getMessage());
        } finally {
            //释放资源
            ds.close();
        }
    }


    public List<Recharge> all() {

        return rechargeMapper.selectAll();
    }
}