package com.houxiaokang.common.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesimplDateFormat {
    /**
     * 格式化时间类型:yyyy-MM-dd hh:mm:ss
     *
     * @param date
     * @return
     */
    public static String DatesimplDateFormatoString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newdate = simpleDateFormat.format(date);
        return newdate;
    }
}
