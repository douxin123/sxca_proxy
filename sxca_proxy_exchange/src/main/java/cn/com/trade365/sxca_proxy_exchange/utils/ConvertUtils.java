package cn.com.trade365.sxca_proxy_exchange.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 工具转换类
 * @author weichunjie
 *
 */
public class ConvertUtils {

    /**
     * 获取String 值
     * @param obj
     * @return
     */
    public static String convert2str(Object obj){
        String value=String.valueOf(obj);
       return ((StringUtils.isEmpty(value)||value.equals("null"))?"":value);
    }

    /**
     * 转换成int
     * @param obj object
     * @return
     */
    public  static  Integer convert2Int(Object obj)
    {
        if(obj==null){return  0;}
        return  Integer.parseInt(convert2str(obj));
    }

    /**
     * 转换时间
     * @param date
     * @return
     */
    public static Date convert2Date(Object date){
        return convert2Date(convert2Long(date));
    }


    /**
     * 转换时间
     * @param date long 20181011 15:36:43
     * @return
     */
    public static Date convert2Date(Long date){
        if (date == null) {
            return  new Date();
        } else {
            String fmDate = String.valueOf(date.longValue());
            String year = "";
            String month = "";
            String day = "";
            String hour = "";
            String minute = "";
            // 日期格式不合法则转化为空串
            if (fmDate.length() < 8) {
                fmDate = "";
            }
            if (fmDate.length() >= 8) {
                year = fmDate.substring(0, 4);
                month = fmDate.substring(4, 6);
                day = fmDate.substring(6, 8);
                fmDate = year + "-" + month + "-" + day + " ";
            }
            if ((date.toString()).length()>= 12) {
                hour = (date.toString()).substring(8, 10);
                minute = (date.toString()).substring(10, 12);
                fmDate +=hour + ":" + minute ;
            }
            if ((date.toString()).length() == 14) {
                fmDate +=":"+(date.toString()).substring(12, 14);
            }
            if((date.toString()).length() == 12){
                fmDate +=":"+"00";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(fmDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    /**
     * 转换成日期
     * @param obj  Object
     * @return  Date
     */
    public static  Date obj2Date(Object obj){
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         if(StringUtils.isEmpty(obj)&&isNumber(obj.toString())){
             return  convert2Date(Long.parseLong(obj.toString()));
         }
        return  null;
    }

    /**
     * @Description:  元、万元转分
     * @Param: obj,obj
     * @return: long
     * @Author: JianbingZhang
     * @Date:
     */
    public static Long convert2Cent(Object amount,Object unit){
        Long money = 0L;
        if(com.alibaba.druid.util.StringUtils.equals(unit.toString(),"元") || com.alibaba.druid.util.StringUtils.equals(unit.toString(),"1") ){
            money = (new BigDecimal(amount.toString()).multiply(new BigDecimal(100))).longValue();
        }else if(com.alibaba.druid.util.StringUtils.equals(unit.toString(),"万元") || com.alibaba.druid.util.StringUtils.equals(unit.toString(),"2")){
            money = (new BigDecimal(amount.toString()).multiply(new BigDecimal(1000000))).longValue();
        }
        return money;
    }
    private static boolean isNumber(String str) {
        return Pattern.compile("^[1-9]\\d*$").matcher(str).find();
    }

    public static Long convert2Long(Object obj) {
        if (obj == null) {
            return null;
        }
        return Long.parseLong(convert2str(obj));
    }
}
