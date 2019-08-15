package cn.com.trade365.sxca_proxy_exchange.service;


import cn.com.trade365.platform.project.dto.BidBidderDto;
import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;

import java.util.Map;

/**
 * 投标人
 *
 * @author 宋建华
 * @date 2018-11-09 10:30
 **/
public interface BidderService   extends BaseService {
    /**
     * 组装数据
     *
     * @param dataMap
     * @return
     */
    Map<String, BidBidderDto> getBidderData(MsgEvent dataMap) throws ExchangeException;
}
