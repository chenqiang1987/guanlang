package com.twc.guanlang.service;

import com.twc.guanlang.service.udp.UdpUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Test {


    public static void main(String[] args) {
        //40a80000
        long l = Long.parseLong("40a80000", 16);
        String d = Long.toBinaryString(l);
        System.out.println(BinaryStringToFloat(d));
    }


    static float BinaryStringToFloat(final String binaryString) {
        // float是32位，将这个binaryString左边补0补足32位，如果是Double补足64位。
        final String stringValue = LeftPad(binaryString, '0', 32);
        // 首位是符号部分，占1位。
        // 如果符号位是0则代表正数，1代表负数
        final int sign = stringValue.charAt(0) == '0' ? 1 : -1;
        // 第2到9位是指数部分，float占8位，double占11位。
        final String exponentStr = stringValue.substring(1, 9);
        // 将这个二进制字符串转成整数，由于指数部分加了偏移量（float偏移量是127，double是1023）
        // 所以实际值要减去127
        final int exponent = Integer.parseInt(exponentStr, 2) - 127;
        // 最后的23位是尾数部分，由于规格化数，小数点左边隐含一个1，现在加上
        final String mantissaStr = "1".concat(stringValue.substring(9, 32));
        // 这里用double，尽量保持精度，最好用BigDecimal，这里只是方便计算所以用double
        double mantissa = 0.0;
        for (int i = 0; i < mantissaStr.length(); i++) {
            final int intValue = Character.getNumericValue(mantissaStr.charAt(i));
            // 计算小数部分，具体请查阅二进制小数转10进制数相关资料
            mantissa += (intValue * Math.pow(2, -i));


        }
        // 根据IEEE 754 标准计算：符号位 * 2的指数次方 * 尾数部分
        return (float) (sign * Math.pow(2, exponent) * mantissa);
    }


    static String LeftPad(final String str, final char padChar, int length) {
        final int repeat = length - str.length();
        if (repeat <= 0) {
            return str;
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf).concat(str);
    }


}
