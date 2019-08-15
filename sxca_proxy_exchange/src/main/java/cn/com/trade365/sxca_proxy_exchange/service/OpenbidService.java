package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidOpenbidDto;


/**
 * @Author: JianBingZhang
 * @Date: 2018/11/8 15:49
 * @Version 1.0
 */
public interface OpenbidService  extends BaseService {

    /**
      * @Description: 封装openbid
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    BidOpenbidDto getOpenBidData(MsgEvent msgEvent) throws ExchangeException;
}
