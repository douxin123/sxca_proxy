package cn.com.trade365.sxca_proxy_exchange.handler;

import cn.com.trade365.sxca_proxy_exchange.core.MessageStatusEnum;
import cn.com.trade365.sxca_proxy_exchange.entity.MessageEntity;
import cn.com.trade365.sxca_proxy_exchange.service.MessageService;
import cn.com.trade365.sxca_proxy_exchange.utils.ExceptionUtil;
import cn.com.trade365.sxca_proxy_exchange.utils.MessageHandlerComponent;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

/**
 * 默认消息处理器
 */
@Component
public class MessageAdapter {

    private static final Logger log = LoggerFactory.getLogger(MessageAdapter.class);

    @Autowired
    MessageService messageService;

    @Autowired
    private MessageHandlerComponent messageHandlerComponent;

    public void adapter(String message) {
        //TODO 存储mq消息
        MessageEntity messageEntity = new MessageEntity();
        try {
            MsgEvent msgEvent = JSON.toJavaObject(JSON.parseObject(message), MsgEvent.class);
            Long messageId = msgEvent.getMessageId();
            if (StringUtils.isEmpty(messageId)) {
                messageEntity.setMsgData(message);
                messageEntity.setStatus(MessageStatusEnum.INIT.getCode());
                messageEntity.setAcceptTime(new Timestamp(System.currentTimeMillis()));
                messageService.insertMsg(messageEntity);
            }else{
                messageEntity.setId(messageId);
                messageEntity.setStatus(MessageStatusEnum.INIT.getCode());
                messageEntity.setRetry(messageService.findAndIncreatmentRetry(messageId));
                messageService.updateRetry(messageEntity);
            }
            //TODO 根据消息类型找到执行器,用工厂模式获取相应的处理对象(MessageHandler.getHandleName())
            MessageHandler messageHandler;
            messageHandler = messageHandlerComponent.getMessageHandler(msgEvent.getEvent());
            messageHandler.handleMessage(msgEvent);
            messageEntity.setStatus(MessageStatusEnum.SUCCESS.getCode());
            messageEntity.setFinishTime(new Timestamp(System.currentTimeMillis()));
        } catch (Exception ex) {
            ex.printStackTrace();
            String stackTraceString = ExceptionUtil.getStackTraceString(ex);
            log.error(stackTraceString, ex);
            messageEntity.setStatus(MessageStatusEnum.FAIL.getCode());
            messageEntity.setFinishTime(new Timestamp(System.currentTimeMillis()));
            messageEntity.setException(stackTraceString);
        } finally {
            //TODO 变更mq执行状态，如果异常记录异常信息
            messageService.updateMsg(messageEntity);
        }

    }


}
