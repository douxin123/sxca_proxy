package cn.com.trade365.sxca_proxy_exchange.utils;

import cn.com.trade365.sxca_proxy_exchange.exception.NotFoundHandlerClassException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author :lhl
 * @create :2018-11-07 08:35
 */
@Component
public class MessageHandlerComponent {

    private static final Map<String,MessageHandler> messageHandlerMap=new ConcurrentHashMap<String, MessageHandler>();

    @Autowired
    private ApplicationContext applicationContext;

    public MessageHandler getMessageHandler(String name) throws NotFoundHandlerClassException {
        MessageHandler handler=messageHandlerMap.get(name);
        if(handler==null){
            throw new NotFoundHandlerClassException(name);
        }
        return handler;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Map<String,MessageHandler> handlerMap=applicationContext.getBeansOfType(MessageHandler.class);
        for(MessageHandler messageHandler:handlerMap.values()){
            messageHandlerMap.put(messageHandler.getHandleName(),messageHandler);
        }
    }

}
