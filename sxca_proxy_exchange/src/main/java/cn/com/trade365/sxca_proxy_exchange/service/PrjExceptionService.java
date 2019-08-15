package cn.com.trade365.sxca_proxy_exchange.service;


import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjExceptionDto;

/**
 * @Author: JianBingZhang
 * @Date: 2018/11/12
 * @Version 1.0
 */
public interface PrjExceptionService extends BaseService {
    /**
      * @Description:获取异常信息
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    PrjExceptionDto getExceptionData(MsgEvent msgEvent) throws ExchangeException;

}
