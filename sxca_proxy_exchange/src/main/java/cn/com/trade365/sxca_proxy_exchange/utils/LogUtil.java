package cn.com.trade365.sxca_proxy_exchange.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * @Auther weichunjie
 */
public class LogUtil {

    private static final Logger log = LoggerFactory.getLogger("trade-proxy-exchage");


    /**
     * 输出日志
     * @param message
     */
     public static void info(String message){
        LogUtil.log.info(message);
     }

    /**
     * 输出日志
     * @param message 消息
     * @param throwable
     */
     public static void info(String message,Throwable throwable){
        LogUtil.log.info(message,throwable);
     }

    /**
     * 错误日志
     * @param message
     */
    public static   void error(String message){
         LogUtil.error(message);
    }

    /**
     * 输出错误日志
     * @param msg
     * @param throwable
     */
    public static void error(String msg, Throwable throwable){
       LogUtil.error(msg,throwable);
    }
}
