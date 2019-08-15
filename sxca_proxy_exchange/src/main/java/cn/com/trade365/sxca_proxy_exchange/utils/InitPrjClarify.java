package cn.com.trade365.sxca_proxy_exchange.utils;

import cn.com.trade365.platform.project.dto.PrjTenderClarifyDto;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author :lhl
 * @create :2018-11-21 15:46
 */
public class InitPrjClarify {
    private static final Logger log = LoggerFactory.getLogger(InitPrjClarify.class);
    private static final Integer defaultInt = 0;
    private static final Long defaultLong = 0L;
    private static final String defaultString = "";

    public static void initPrjClarifyField(PrjTenderClarifyDto clarifyDto) {
        Class<? extends PrjTenderClarifyDto> aClass = clarifyDto.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object obj = declaredField.get(clarifyDto);
                if (ObjectUtil.isNull(obj)) {
                    if (declaredField.getType() == String.class) {
                        declaredField.set(clarifyDto, defaultString);
                    } else if (declaredField.getType() == int.class) {
                        declaredField.set(clarifyDto, defaultInt);
                    } else if (declaredField.getType() == Integer.class) {
                        declaredField.set(clarifyDto, defaultInt);
                    } else if (declaredField.getType() == Long.class) {
                        declaredField.set(clarifyDto, defaultLong);
                    } else if (declaredField.getType() == Date.class) {
                        declaredField.set(clarifyDto, new Date());
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("变更澄清 实体类初始化失败 {}", e);
            }
        }
    }

    public static void main(String[] args) {
        PrjTenderClarifyDto prjTenderClarifyDto = new PrjTenderClarifyDto().setClarifyContent("asd");
        initPrjClarifyField(prjTenderClarifyDto);
        System.out.println(prjTenderClarifyDto);
    }
}
