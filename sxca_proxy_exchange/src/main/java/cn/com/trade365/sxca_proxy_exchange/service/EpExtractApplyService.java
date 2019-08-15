package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EpExtractApplyDto;

import java.util.Map;

/**
 * 专家抽取申请 服务类
 */
public interface EpExtractApplyService {

    /**
     * 组装数据
     * @param msgEvent
     * @return
     */
    Map<String, EpExtractApplyDto> getEpExtractApplyData(MsgEvent msgEvent) throws ExchangeException;
}
