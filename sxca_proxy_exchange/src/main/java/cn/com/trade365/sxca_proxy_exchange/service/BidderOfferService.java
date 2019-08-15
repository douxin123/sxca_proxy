package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;


/**
 * @Author: JianBingZhang
 * @Date: 2018/11/12
 * @Version 1.0
 */
public interface BidderOfferService extends BaseService{

    /**
      * @Description:  推送bidderOffer
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    void pushBidderOfferData(MsgEvent msgEvent) throws Exception;
}
