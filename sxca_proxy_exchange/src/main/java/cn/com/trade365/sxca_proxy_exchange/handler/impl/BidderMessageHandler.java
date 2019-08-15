package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidBidderDto;
import cn.com.trade365.sxca_proxy_exchange.service.BidderService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 投标人同步
 */
@Component
public class BidderMessageHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(BidderMessageHandler.class);

    @Autowired
    RestComponent restComponent;

    @Autowired
    BidderService bidderService;

    @Autowired
    IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, BidBidderDto> bidderMap = bidderService.getBidderData(msgEvent);
            for (Map.Entry<String, BidBidderDto> entry : bidderMap.entrySet()) {
                //获取bmgysId，用来当作存储关系表的tradeId
                String bmgysId = entry.getKey();
                BidBidderDto sendData = entry.getValue();
                //判断数据中心是否存在数据，存在则执行更新操作，不存在则执行插入操作
                Long dataId = null;
                try {
                    dataId = idRelationService.getDataId(ObjectTypeEnum.BIDBIDDER, bmgysId);
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                ResultData<Long> resultData;
                RelationEntity relationEntity = new RelationEntity();
                if (dataId == null) {
                    resultData = restComponent.post(Repositories.COMM_PREFIX + Repositories.BID_BIDDER_SAVE_URL, sendData, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    log.info("返回结果：{}", resultData);
                    relationEntity.setCode(ObjectTypeEnum.BIDBIDDER.getCode());
                    relationEntity.setTradeId(bmgysId);//内控平台唯一标识
                    relationEntity.setData(JSONUtil.parse(sendData).toString());//往数据中心推送的数据
                    relationEntity.setDataId(resultData.getData());//数据中心唯一标识
                    //关系表存储
                    idRelationService.relation(relationEntity);
                } else {
                    resultData = restComponent.put(Repositories.COMM_PREFIX + Repositories.BID_BIDDER_SAVE_URL + "/" + dataId, sendData, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    log.info("返回结果：{}", resultData);
                }

            }
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.BIDDER_MESSAGE_EVENT;
    }

}
