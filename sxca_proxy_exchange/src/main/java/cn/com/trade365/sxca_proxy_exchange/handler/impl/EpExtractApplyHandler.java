package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EpAvoidUnitDto;
import cn.com.trade365.platform.project.dto.EpExtractApplyDto;
import cn.com.trade365.sxca_proxy_exchange.service.EpAvoidUnitService;
import cn.com.trade365.sxca_proxy_exchange.service.EpExtractApplyService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 专家抽取申请和回避单位同步
 */
@Component
public class EpExtractApplyHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(EpExtractApplyHandler.class);

    @Autowired
    RestComponent restComponent;

    @Autowired
    EpExtractApplyService epExtractApplyService;

    @Autowired
    EpAvoidUnitService epAvoidUnitService;

    @Autowired
    IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            //1.推送专家申请信息
            sendEpExtractApply(msgEvent);
            //2.推送回避单位信息
            sendEpAvoidUnit(msgEvent);
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.EP_EXTRACT_APPLY_EVENT;
    }

    /**
     * 推送专家申请信息
     *
     * @param msgEvent msgEvent
     * @throws Exception
     */
    private void sendEpExtractApply(MsgEvent msgEvent) throws Exception {
        Map<String, EpExtractApplyDto> epExtractApplyMap = epExtractApplyService.getEpExtractApplyData(msgEvent);
        for (Map.Entry<String, EpExtractApplyDto> entry : epExtractApplyMap.entrySet()) {
            //获取zjsqfbId，用来当作存储关系表的tradeId
            String zjsqfbId = entry.getKey();
            EpExtractApplyDto sendData = entry.getValue();
            //发送数据
            sendData(zjsqfbId, ObjectTypeEnum.EPEXTRACTAPPLY, sendData);

        }
    }

    /**
     * 推送回避单位信息
     *
     * @param msgEvent msgEvent
     * @throws Exception
     */
    private void sendEpAvoidUnit(MsgEvent msgEvent) throws Exception {
        Map<String, EpAvoidUnitDto> epAvoidUnitMap = epAvoidUnitService.getEpAvoidUnit(msgEvent);
        for (Map.Entry<String, EpAvoidUnitDto> entry : epAvoidUnitMap.entrySet()) {
            //获取hbdwId，用来当作存储关系表的tradeId
            String hbdwId = entry.getKey();
            EpAvoidUnitDto sendData = entry.getValue();
            //发送数据
            sendData(hbdwId, ObjectTypeEnum.EPAVOIDUNIT, sendData);
        }
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
            e.printStackTrace();
        }
        log.info("sendData：{}", JSONUtil.parse(sendData).toString());
        String sendUrl = Repositories.COMM_PREFIX + (ObjectTypeEnum.EPAVOIDUNIT.equals(objectTypeEnum) ? Repositories.EP_AVOID_UNIT_SAVE_URL : Repositories.EP_EXTRACT_APPLY_SAVE_URL);
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

}
