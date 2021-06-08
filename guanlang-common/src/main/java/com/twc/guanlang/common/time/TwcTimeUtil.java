package com.twc.guanlang.common.time;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class TwcTimeUtil {


    public static final String format_default = "yyyy-MM-dd HH:mm:ss";

    public static final String reverseTime2String(Date date) {

        return DateFormatUtils.format(date, format_default);
    }
}
