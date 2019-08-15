package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EpAvoidUnitDto;

import java.util.Map;

/**
 * 回避单位 服务类
 *
 * @author 宋建华
 * @date 2018-11-14 10:28
 */
public interface EpAvoidUnitService {
    /**
     * 组装数据
     * @param msgEvent
     * @return
     */
    Map<String, EpAvoidUnitDto> getEpAvoidUnit(MsgEvent msgEvent) throws ExchangeException;
}
