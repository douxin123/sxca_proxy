package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.platform.project.dto.BidBidderInviteDto;
import cn.com.trade365.platform.project.dto.BidInviteDto;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.sxca_proxy_exchange.service.BidderInviteService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.InviteService;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 邀请书同步
 */
@Component
public class InviteMessageHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(InviteMessageHandler.class);


    @Autowired
    RestComponent restComponent;
    @Autowired
    IdRelationService idRelationService;
    @Autowired
    InviteService inviteService;

    @Autowired
    BidderInviteService bidderInviteService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            //推送邀请书数据
            sendInvite(msgEvent);
            //推送投标人邀请信息数据
            sendBidderInvite(msgEvent);
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    /**
     * 推送投标人邀请信息数据
     * @param msgEvent
     * @return
     * @throws Exception
     */
    private ResultData<Long> sendBidderInvite(MsgEvent msgEvent) throws Exception {
        Map<String, String> dataMap = msgEvent.getData();
        String tradeId = dataMap.get("tradeId");

        BidBidderInviteDto sendData = bidderInviteService.getBidderInviteData(msgEvent);
        return sendData(tradeId,ObjectTypeEnum.BIDBIDDERINVITE,sendData);
    }


    /**
     * 推送邀请书数据
     * @param msgEvent
     * @return
     * @throws Exception
     */
    private ResultData<Long> sendInvite(MsgEvent msgEvent) throws Exception {
        Map<String, String> dataMap = msgEvent.getData();
        String tradeId = dataMap.get("tradeId");
        BidInviteDto sendData = inviteService.getBidInviteData(msgEvent);
        return sendData(tradeId,ObjectTypeEnum.BIDINVITE,sendData);
    }

    /**
     * 推送数据
     *
     * @param tradeId        关系表tradeId
     * @param objectTypeEnum 关系表code
     * @param sendData       发送的消息数据
     * @return
     * @throws Exception
     */
    private ResultData<Long> sendData(String tradeId, ObjectTypeEnum objectTypeEnum, Object sendData) throws Exception {
        ResultData<Long> resultData;
        RelationEntity relationEntity = new RelationEntity();
        //判断数据中心是否存在数据，存在则执行更新操作，不存在则执行插入操作
        Long dataId = null;
        try {
            dataId = idRelationService.getDataId(objectTypeEnum, tradeId);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        log.info("sendData：{}", JSONUtil.parse(sendData).toString());
        String sendUrl = Repositories.COMM_PREFIX + (ObjectTypeEnum.BIDINVITE.equals(objectTypeEnum) ? Repositories.BID_INVITE_SAVE_URL : Repositories.BID_BIDDER_INVITE_SAVE_URL);
        if (dataId == null) {
            resultData = restComponent.post(sendUrl, sendData, new ParameterizedTypeReference<ResultData<Long>>() {
            });
            log.info("返回结果：{}", resultData);
            relationEntity.setCode(objectTypeEnum.getCode());
            relationEntity.setTradeId(tradeId);//内控平台唯一标识
            relationEntity.setData(JSONUtil.parse(sendData).toString());//往数据中心推送的数据
            relationEntity.setDataId(resultData.getData());//数据中心唯一标识
            //关系表存储
            idRelationService.relation(relationEntity);
        } else {
            resultData = restComponent.put(sendUrl + "/" + dataId, sendData, new ParameterizedTypeReference<ResultData<Long>>() {
            });
            log.info("返回结果：{}", resultData);
        }
        return resultData;
    }

    @Override
    public String getHandleName() {
        return MsgEvent.INVITE_MESSAGE_EVENT;
    }

}
