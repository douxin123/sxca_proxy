package cn.com.trade365.sxca_proxy_exchange.handler;

import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.service.MessageService;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @Author: JianBingZhang
 * @Description: ${description}
 * @Version 1.0
 */
//@Component
//@Configuration     //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class ExceptionMessageTimerTask{
    private static final Logger log = LoggerFactory.getLogger(ExceptionMessageTimerTask.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Value("${active.trade.message}")
    private String destination;
    //直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate=60*1000)
    private void RetryMessageTask() throws ExchangeException{
        JSONObject result=new JSONObject();
        List<Map<String,Object>> messageEntitys = messageService.getExceptionMessages();
        for(int i=0;i<messageEntitys.size();i++){
            try{
                String data = Convert.toStr(messageEntitys.get(i).get("msg_data"));
                JSONObject jsonObject = JSON.parseObject(data);
                jsonObject.put("messageId", messageEntitys.get(i).get("id"));
                jmsMessagingTemplate.convertAndSend(destination, jsonObject.toJSONString());
                messageService.updateRetryById(Convert.toLong(messageEntitys.get(i).get("id")),Convert.toInt(messageEntitys.get(i).get("retry")));
            }catch(Exception e){
                throw new ExchangeException("异常消息重试失败，共"+messageEntitys.size()+"条，当前处理到"+i+"条",e);
            }
        }
    }

}
