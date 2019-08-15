package cn.com.trade365.sxca_proxy_exchange.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @Author weichunjie
 *
 */
public class DateUtil {

    public static Date getCurrTime(){

        return Calendar.getInstance().getTime();
    }

}
