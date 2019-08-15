package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.sxca_proxy_exchange.service.BidderOfferService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 投标人投标中标信息同步
 */
@Component
public class BidderOfferMessageHandler implements MessageHandler {


    @Autowired
    IdRelationService idRelationService;

    private static final Logger log = LoggerFactory.getLogger(BidderOfferMessageHandler.class);

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private BidderOfferService bidderOfferService;

    @Override
    /**
     * @Description:
     * @Param: [msgEvent]
     * @return: void
     * @Author: JianbingZhang
     * @Date:
     */
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            bidderOfferService.pushBidderOfferData(msgEvent);
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.BIDDER_OFFER_MESSAGE_EVENT;
    }

}
