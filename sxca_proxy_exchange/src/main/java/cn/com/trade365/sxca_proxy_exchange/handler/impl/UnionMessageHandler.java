package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 联合体数据同步
 */
@Component
public class UnionMessageHandler implements MessageHandler{


    @Autowired
    IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {

        }catch (Exception ex){
            throw new ExchangeException("调用失败",ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.UNION_MESSAGE_EVENT;
    }

}
