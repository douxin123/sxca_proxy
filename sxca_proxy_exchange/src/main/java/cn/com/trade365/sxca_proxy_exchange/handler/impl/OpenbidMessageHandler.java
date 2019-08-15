package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidOpenbidDto;
import cn.com.trade365.sxca_proxy_exchange.service.BidderOfferService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.OpenbidService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开标记录同步
 */
@Component
public class OpenbidMessageHandler implements MessageHandler {


    @Autowired
    IdRelationService idRelationService;

    private static final Logger log = LoggerFactory.getLogger(OpenbidMessageHandler.class);

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private OpenbidService openbidService;

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
            Map<String, String> dataMap = msgEvent.getData();
            if (StringUtils.isNotEmpty(dataMap.get("tradeId"))) {
                // 内控的业务id (类型(0-两阶段的第一阶段，1招标，2资审))
                String sectionId = dataMap.get("tradeId");
                ResultData<Long> result;
                ResultData<Boolean> updateResult;
                // 装配请求参数
                BidOpenbidDto openbid = openbidService.getOpenBidData(msgEvent);
                sectionId = sectionId + openbid.getType();
                // 查询dataId
                Long dataId;
                // 检测是否为 重复推送数据
                if (idRelationService.isExitDataId(ObjectTypeEnum.OPENBID, sectionId)) {
                    dataId = idRelationService.getDataId(ObjectTypeEnum.OPENBID, sectionId);
                    //发送更新
                    updateResult = restComponent.put(Repositories.getUrl(Repositories.BIDOPENBID_SAVE_URL + "/" + dataId), openbid, new ParameterizedTypeReference<ResultData<Boolean>>() {
                    });
                    if (updateResult.getCode() != 200) {
                        throw new Exception(updateResult.getMessage());
                    }
                    //推送 bidderOffer 信息
                    bidderOfferService.pushBidderOfferData(msgEvent);
                    log.info("开标记录更新成功");
                } else {
                    // 根据业务需求 调用数据中心接口 推送openbid
                    result = restComponent.post(Repositories.getUrl(Repositories.BIDOPENBID_SAVE_URL), openbid, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (result.getCode() != 200) {
                        throw new Exception(result.getMessage());
                    }
                    // 关系表存储
                    RelationEntity relationEntity = new RelationEntity();
                    relationEntity.setCode(ObjectTypeEnum.OPENBID.getCode());
                    relationEntity.setTradeId(sectionId);
                    relationEntity.setData(JSONObject.toJSONString(dataMap));
                    relationEntity.setDataId(result.getData());
                    idRelationService.relation(relationEntity);
                    //推送 bidderOffer 信息
                    bidderOfferService.pushBidderOfferData(msgEvent);
                    log.info("开标记录，投标人推送成功");
                }

            }
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.OPENBID_MESSAGE_EVENT;
    }

}
