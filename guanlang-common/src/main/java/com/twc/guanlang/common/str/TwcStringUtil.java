package com.twc.guanlang.common.str;

public class TwcStringUtil {

    public static boolean isEmpty(String str) {

        if (tk.mybatis.mapper.util.StringUtil.isEmpty(str) || str.equalsIgnoreCase("null")) {
            return true;
        } else {
            str = str.trim();
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {

        if (tk.mybatis.mapper.util.StringUtil.isNotEmpty(str) && !str.equalsIgnoreCase("null")) {
            str = str.trim();
            return true;
        }
        return false;
    }
}
