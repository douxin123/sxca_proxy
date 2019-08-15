package cn.com.trade365.sxca_proxy_exchange.service;


import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidBidderInviteDto;

/**
 * 投标人邀请
 *
 * @author 宋建华
 * @date 2018-11-09 10:30
 **/
public interface BidderInviteService extends BaseService {
    /**
     * 组装数据
     *
     * @param msgEvent
     * @return
     */
    BidBidderInviteDto getBidderInviteData(MsgEvent msgEvent) throws ExchangeException;
}
