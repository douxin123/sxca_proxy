package cn.com.trade365.sxca_proxy_exchange.listener.trade;

import cn.com.trade365.sxca_proxy_exchange.handler.MessageAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/**
 * @author :lhl
 * @create :2018-11-05 15:07
 */
@Component
public class TradeListener {

    private static final Logger log = LoggerFactory.getLogger(TradeListener.class);

    @Autowired
    MessageAdapter messageAdapter;


    @JmsListener(destination = "${active.trade.message}")
    public void receiveQueue(String message) {
        messageAdapter.adapter(message);
    }


}
