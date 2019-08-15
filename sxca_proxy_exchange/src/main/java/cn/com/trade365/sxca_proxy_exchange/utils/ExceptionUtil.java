package cn.com.trade365.sxca_proxy_exchange.utils;


import com.alibaba.fastjson.util.IOUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author :lhl
 * @create :2018-11-07 08:21
 */
public class ExceptionUtil {


    /**
     * 获取异常详细信息，异常信息保存到字符串中
     *
     * @param e
     * @return
     * @author lizhihui
     */
    public static String getStackTraceString(Throwable e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "bad getStackTraceString";
        } finally {
            IOUtils.close(pw);
            IOUtils.close(sw);
        }
    }
}
