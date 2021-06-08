package com.twc.guanlang.service.udp;

public class XorTest {


    public static void main(String[] args) {


        //16进制字符串转字节数组
        byte[] AAbytes = SendDemoNew.hex2byte("AA 0A 00 99 6D 57 90 20 55");


        //数组转16进制字符串
        System.out.println("异或运算前还原16进制：===========" + bytesToHex(AAbytes));

        byte temp = 0;
        //异或运算
//        for (int i = 0; i < AAbytes.length-1; i++) {
//
//            for(int j=i+1;j<AAbytes.length;j++){
//
//                 AAbytes[i]^AAbytes[j];
//
//            }
//        }
        //数组转16进制字符串
//        System.out.println("异或运算后16进制：===========" + bytesToHex(AAbytes));

        int rs = test(AAbytes, AAbytes.length - 1);


        System.out.println(rs + "========异或运算前还原16进制：===========" + bytesToHex(toLH(rs)));

        System.out.println(rs + "========异或运算后16进制：===========" + bytesToHex(toHH(rs)));


        byte b=getXor(AAbytes);
        byte[] bs=new byte[1];


        bs[0]=getXor(AAbytes);
        System.out.println(rs + "========异或运算后16进制：===========" + bytesToHex(bs));

    }

    public static int test(byte[] bytes, int index) {

        if (index == 0) {
            return bytes[0];
        } else {
            return bytes[index] ^ test(bytes, index - 1);
        }
    }

    public static byte getXor(byte[] data){
        byte temp = data[0];
        for(int i=1;i<data.length;i++){
            temp^=data[i];
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
}