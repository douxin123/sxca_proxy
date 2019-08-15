package cn.com.trade365.sxca_proxy_exchange.handler;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;

/**
 * 消息执行器
 */
public interface MessageHandler extends BaseService {
    /**
     * 处理消息
     * @return
     */
    void handleMessage(MsgEvent msgEvent) throws ExchangeException;

    /**
     * 处理器名称
     * @return
     */
    String getHandleName();
}
