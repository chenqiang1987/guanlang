package com.twc.guanlang.service;

import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.common.time.TwcTimeUtil;
import com.twc.guanlang.entity.BaseEntity;
import com.twc.guanlang.entity.machine.*;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.entity.user.SystemUser;
import com.twc.guanlang.mapper.entity.*;
import com.twc.guanlang.param.machine.OpenDoorParam;
import com.twc.guanlang.service.udp.UdpUtil;
import com.twc.guanlang.service.ws.WebSocket;
import com.twc.guanlang.vo.ExpertVO;
import com.twc.guanlang.vo.RechargeWebScoketData;
import io.swagger.annotations.ApiImplicitParam;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 充电桩udp服务类
 */

@Slf4j
@Configuration
@EnableScheduling
public class RechargeUdpService {

    @Value("${recharge.udp.port}")
    private String port;
    @Resource
    private RechargeMapper rechargeMapper;
    public static Map<Long, Long> heartMap = new HashMap();
    public static Map<Long, String> onLineMap = new HashMap();
    public static Map<Integer, String> rechargeDoorState = new HashMap();
    @Autowired
    private MachineMapper machineMapper;

    public static Map<Long, RechargeWebScoketData> rechargeHeartData = new HashMap();

    /**
     * 构造函数
     * 接收监听充电桩udp消息
     *
     * @throws IOException
     */
    public RechargeUdpService() throws IOException {
        //创建接收端的Socket服务对象，并且指定端口号

        DatagramSocket ds = new DatagramSocket(4000);
        /**
         * 这里需要异步启动，否则springboot会被阻塞在 while(true)语句块里
         */
        log.info("启动充电桩 udp监听..........................");
        //创建一个数据包，用于接收数据
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    byte[] bys = new byte[1024];
//                    DatagramPacket dp = new DatagramPacket(bys, bys.length);
//                    //接收数据
//                    try {
//                        ds.receive(dp);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //解析数据
//                    //获取ip地址
//                    String ip = dp.getAddress().getHostAddress();
//
//                    System.out.println("充電樁IP："+ip);
//                    Example search = new Example(Recharge.class);
//                    search.createCriteria().andEqualTo("ip", ip);
//                    Recharge machine = rechargeMapper.selectOneByExample(search);
//
//                    RechargeWebScoketData rechargeWebScoketData = new RechargeWebScoketData();
//                    rechargeWebScoketData.setIsOnline(1);
//                    heartMap.put(machine.getId(), System.currentTimeMillis());
//                    onLineMap.put(machine.getId(), "on");
//
//
//
//                    //确定接收的数据报文的长度，来建立缓冲区
//                    byte[] buf = new byte[36];
//                    //创建接收类型的数据包，数据先储存在缓冲区
//                    DatagramPacket getPacket = new DatagramPacket(buf, buf.length);
//                    //通过套接字接收数据
//                    try {
//                        ds.receive(getPacket);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    //解析接收到到16机制数据
//                    byte[] bytes = getPacket.getData();
//                    String data = getBufHexStr(bytes);
//
//                    //获取数据
////                    String data = new String(dp.getData(), 0, dp.getLength());
//                    data=resolveMsg(data);
//                    String[] arr = data.split(" ");
//                    //电流电压报文
//                    if (data.split(" ").length == 8) {
//                        String count = arr[0];
//                        //门编号
//                        String number = arr[1];
//                        //电压
//                        String volmeter = arr[2] + arr[3];
//                        //电流
//                        String electricity = arr[4] + arr[5];
//                        //如果门状态为打开，则通知相关机器人门已经打开
//                        String doorState = arr[6];
//                        String sum = arr[7];
//                        if (count.equalsIgnoreCase("06")) {
//                            number = number.replace("0", "");
//                            int doorInt = UdpUtil.hex2decimal(number);
//                            Double voltage = Double.parseDouble(hex2decimal(volmeter) + "") / 10;
//                            Double ele = Double.parseDouble(hex2decimal(electricity) + "") / 10;
//                            String hexSum = UdpUtil.makeChecksum(count + number + volmeter + voltage + doorState);
//                            hexSum = hexSum.substring(2, 4);
//                            if (hexSum.equalsIgnoreCase(sum)) {
//                                Example example = new Example(Recharge.class);
//                                example.createCriteria().andEqualTo("number", number);
//                                List<Recharge> list = rechargeMapper.selectByExample(example);
//                                Recharge recharge = list.get(0);
//                                recharge.setVoltage(voltage);
//                                recharge.setElectricity(ele);
//                                recharge.setDoorState(Integer.parseInt(doorState));
//                                rechargeWebScoketData.setOpenStatus(doorState);
//                                rechargeMapper.updateByPrimaryKey(recharge);
//                                //获取们编号
//                                number = number.substring(1, 1);
//                                Integer numberInt = UdpUtil.hex2decimal(number);
//                                rechargeDoorState.put(numberInt, doorState);
//                                //通知对应机器人
//                                Long machineId = MachineService.machineRechargeCallOpenMap.get(numberInt);
//                                if (machineId != null) {
//                                    Machine machine1 = machineMapper.selectByPrimaryKey(machineId);
//                                    try {
//                                        MachineService.noticeMachineDoorStateChange(machine1, doorState);
//                                    } catch (CustomException e) {
//
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });


        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {


                    try{

                        byte[] bys = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(bys, bys.length);
                        //接收数据
                        try {
                            ds.receive(dp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //解析数据
                        //获取ip地址
                        String ip = dp.getAddress().getHostAddress();

//                        System.out.println("充電樁IP："+ip);
                        Example search = new Example(Recharge.class);
                        search.createCriteria().andEqualTo("ip", ip);
                        Recharge machine = rechargeMapper.selectOneByExample(search);

                        RechargeWebScoketData rechargeWebScoketData = new RechargeWebScoketData();
                        rechargeWebScoketData.setIsOnline(1);
                        heartMap.put(machine.getId(), System.currentTimeMillis());
                        onLineMap.put(machine.getId(), "on");



//                    //确定接收的数据报文的长度，来建立缓冲区
//                    byte[] buf = new byte[36];
//                    //创建接收类型的数据包，数据先储存在缓冲区
//                    DatagramPacket getPacket = new DatagramPacket(buf, buf.length);
//                    //通过套接字接收数据
//                    try {
//                        ds.receive(getPacket);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                        //解析接收到到16机制数据
                        byte[] bytes = dp.getData();
                        String data = getBufHexStr(bytes);

                        //获取数据
//                    String data = new String(dp.getData(), 0, dp.getLength());

                        if(data.equalsIgnoreCase(""))
                            return;

                        data=resolveMsg(data);


                      //  System.out.println("充电桩data："+data);

                        //0701000000000109


                        //701000000000109

                        //电流电压报文
                        if (data.length() == 16) {

                            String number=data.substring(2,4);
                            String state=data.substring(12,14);
                            String count = data.substring(0, 2);
                            //门编号
                            number = data.substring(2, 4);
                            //电压
                            String volmeter = data.substring(4, 6) + data.substring(6, 8);
                            //电流
                            String electricity = data.substring(8, 10) + data.substring(10, 12);
                            //如果门状态为打开，则通知相关机器人门已经打开
                            String doorState = data.substring(12, 14);
                            String sum = data.substring(14, 16);
                            if (count.equalsIgnoreCase("07")) {
//                number = number.replace("0", "");
                                int doorInt = UdpUtil.hex2decimal(number);
                                Double voltage = Double.parseDouble(hex2decimal(volmeter) + "") / 100;
                                Double ele = Double.parseDouble(hex2decimal(electricity) + "") / 100;

                                String hexSum = UdpUtil.makeChecksum(count + number + volmeter + electricity + doorState);
                                hexSum = hexSum.substring(2, 4);
                                if (hexSum.equalsIgnoreCase(sum)) {
                                    Example example = new Example(Recharge.class);

//                                number=number.replace("0","");
//                                example.createCriteria().andEqualTo("id", number);
                                    Recharge recharge = rechargeMapper.selectByPrimaryKey(number);
                                    recharge.setVoltage(voltage);
                                    recharge.setElectricity(ele);
                                    recharge.setDoorState(Integer.parseInt(doorState));
                                    recharge.setUpdateTime(new Date());
                                    rechargeWebScoketData.setOpenStatus(doorState);
                                    rechargeMapper.updateByPrimaryKey(recharge);
                                    //获取们编号
//                                number = number.substring(1, 1);
//                                Integer numberInt = UdpUtil.hex2decimal(number);
                                    rechargeDoorState.put(Integer.parseInt(number), doorState);
                                    //通知对应机器人

                                    /**
                                     *
                                     *
                                     * 通知机器人门打开  start
                                     */
                                    //3 如果機器人處於recharging狀態  保持所有的充電門為打開狀態.并且通知機器人門已開
                                    //監聽到門關閉狀態 則開啓所有的門
//                                    Example machineRechargeEx=new Example(MachineRecharge.class);
//                                    machineRechargeEx.createCriteria().andEqualTo("status");
                                    if(state.equalsIgnoreCase("02")){
                                        //当前充电桩被打开，且有充电任务绑定，通知機器人门已经打开
                                        Machine machine1=machineMapper.selectByPrimaryKey(1);

                                        Example example1 = new Example(MachineRecharge.class);
                                        example1.createCriteria().andEqualTo("status", MachineRecharge.STATUS_ENUM.WAIT_DOOR_PEN.getCode()).andEqualTo("rechargeId",number);
                                        example1.orderBy("createTime").desc();
                                        RowBounds rowBounds = new RowBounds(0, 1);
                                        List<MachineRecharge> machineRechargeList = machineRechargeMapper.selectByExampleAndRowBounds(example1, rowBounds);
                                        MachineRecharge exists = null;
                                        String stateName = null;
                                        if (machineRechargeList != null && machineRechargeList.size() >0) {

                                            MachineRecharge  machineRecharge=machineRechargeList.get(0);
                                            if(ele>0){
                                                machineRecharge.setStatus(MachineRecharge.STATUS_ENUM.RECHARG_STARTED.getCode());
                                                machineRechargeMapper.updateByPrimaryKey(machineRecharge);
                                            }

                                            MachineService.noticeMachineDoorStateChange(machine1,state);
                                        }


                                    }else if(state.equalsIgnoreCase("01")){


                                        OpenDoorParam openDoorParam = new OpenDoorParam();
                                        openDoorParam.setId(Long.parseLong(number));
                                        //门如果关闭的 则再通知机器人开门
                                       // rechargeService.openDoor(openDoorParam);
                                    }
                                    /**
                                     *
                                     *
                                     * 通知机器人门打开  end
                                     */
                                }
                            }
                        }

                    }catch (Exception e){

                        log.error(" 充电桩接受数据错误:"+e.getMessage());
                        e.printStackTrace();;
                    }




                }




            }
        }).start();
    }



    @Resource
    private MachineRechargeMapper machineRechargeMapper;

    @Resource
    private RechargeService rechargeService;

    /**
     * 对充电桩udp报文进行解析
     */
    private static String resolveMsg(String msg) {
        /**
         * 合法报文解析，非法报文舍弃
         */
//        System.out.println("resolveMsg  1  :"+msg);
        if (msg.lastIndexOf("AA55") != -1 && msg.lastIndexOf("55AA") != -1) {


//            System.out.println("resolveMsg 2  :"+msg);
            int start = msg.lastIndexOf("AA55") + 4;
            int end = msg.lastIndexOf("55AA");
            msg = msg.substring(start, end);
            msg = msg.trim();

            System.out.println(msg.split(" ").length);
            return msg;
        } else
            return "";
    }

    public static void main(String[] s) throws UnsupportedEncodingException {
        resolveMsg("========== AA 55 XX SDF xx 09 02 55 AA0000000000000000000000==============");
//        String tt = new String(parseHexStr2Byte("AA"), "utf-8");
//        System.out.println(tt);
//        System.out.println(hexStringToByte("0246"));
//        System.out.println(hex2decimal("0246"));
//        System.out.println(hex2decimal("FF"));
//        System.out.println(hex2decimal("00") + "==========" + hex2decimal("0"));
//        System.out.println(hex2decimal("01") + "==========" + hex2decimal("1"));
//        System.out.println(Integer.parseInt("01") + "==========" + Integer.parseInt("00"));

        String data = "0701000000000109";

        String number = data.substring(2, 4);
        String state = data.substring(12, 14);
        //701000000000109

        String[] arr = data.split(" ");
        //电流电压报文
        if (data.length() == 16) {


            String count = data.substring(0, 2);
            //门编号
            number = data.substring(2, 4);
            //电压
            String volmeter = data.substring(4, 6) + data.substring(6, 8);
            //电流
            String electricity = data.substring(8, 10) + data.substring(10, 12);
            //如果门状态为打开，则通知相关机器人门已经打开
            String doorState = data.substring(12, 14);
            String sum = data.substring(14, 16);
            if (count.equalsIgnoreCase("07")) {
//                number = number.replace("0", "");
                int doorInt = UdpUtil.hex2decimal(number);
                Double voltage = Double.parseDouble(hex2decimal(volmeter) + "") / 10;
                Double ele = Double.parseDouble(hex2decimal(electricity) + "") / 10;
                String hexSum = UdpUtil.makeChecksum(count + number + volmeter + electricity + doorState);
                hexSum = hexSum.substring(2, 4);
                if (hexSum.equalsIgnoreCase(sum)) {

                }
            }
        }
    }
    /**
     * 16进制转2进制
     *
     * @param hex
     * @return
     */
    public static String hexStringToByte(String hex) {
        int i = Integer.parseInt(hex, 16);
        String str2 = Integer.toBinaryString(i);
        return str2;
    }

    /**
     * 16进制转10进制
     *
     * @param hex
     * @return
     */
    public static int hex2decimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    //将16进制的byte数组转换成字符串
    public static String getBufHexStr(byte[] raw) {
        String HEXES = "0123456789ABCDEF";
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    //回复数据
    private static void backHeadle(DatagramSocket getSocket, DatagramPacket getPacket) {
        //通过数据包得到发送方的套接字ip
        SocketAddress sendAddress = getPacket.getSocketAddress();
        //确定要回复给发送方的消息内容，并转换成字符数组
        String feedback = "211502a0";
        //由于16进制字符发送时只能发送字节，这里讲字符串转换成字节
        byte bytes[] = getHexBytes(feedback);
        //创建发送类型的数据包
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, sendAddress);
        //通过套接字发送数据
        try {
            getSocket.send(sendPacket);
            log.info("发送成功");
        } catch (IOException e) {
            log.info("发送失败");
            e.printStackTrace();
        }
    }

    //将16进制的字符串转成字符数组
    public static byte[] getHexBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

}

