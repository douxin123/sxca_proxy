package cn.com.trade365.sxca_proxy_exchange.service;


import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidInviteDto;

/**
 * 投标人邀请书
 *
 * @author 宋建华
 * @date 2018-11-09 10:30
 **/
public interface InviteService  extends BaseService {
    /**
     * 组装数据
     *
     * @param dataMap
     * @return
     */
    BidInviteDto getBidInviteData(MsgEvent dataMap) throws ExchangeException;
}
