package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.CtContractDto;
import cn.com.trade365.sxca_proxy_exchange.service.ContractService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 合同信息同步
 */
@Component
public class ContractMessageHandler implements MessageHandler {


    @Autowired
    IdRelationService idRelationService;

    private static final Logger log = LoggerFactory.getLogger(ContractMessageHandler.class);

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private ContractService contractService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, String> dataMap = msgEvent.getData();
            if (StringUtils.isNotEmpty(dataMap.get("contractId"))) {
                String contractId = dataMap.get("contractId");
                Long dataId ;
                CtContractDto contract = contractService.getContractData(msgEvent);
                ResultData<Long> result;
                ResultData<Boolean> updateResult;
                if (!idRelationService.isExitDataId(ObjectTypeEnum.CTCONTRACT, contractId)) {
                    // 推送
                    result = restComponent.post(Repositories.getUrl(Repositories.CTCONTRACT_SAVE_URL), contract, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (result.getCode() != 200) {
                        throw new Exception(result.getMessage());
                    }
                    // 关系表存储
                    RelationEntity relationEntity = new RelationEntity();
                    relationEntity.setCode(ObjectTypeEnum.CTCONTRACT.getCode());
                    relationEntity.setTradeId(contractId);
                    relationEntity.setData(JSONObject.toJSONString(dataMap));
                    relationEntity.setDataId(result.getData());
                    idRelationService.relation(relationEntity);
                    log.info("合同信息推送成功");
                } else {
                    dataId = idRelationService.getDataId(ObjectTypeEnum.CTCONTRACT, contractId);
                    // 更新
                    updateResult = restComponent.put(Repositories.getUrl(Repositories.CTCONTRACT_SAVE_URL + "/" + dataId), contract, new ParameterizedTypeReference<ResultData<Boolean>>() {
                    });
                    if (updateResult.getCode() != 200) {
                        throw new Exception(updateResult.getMessage());
                    }
                    log.info("合同信息更新成功");
                }

            }

        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.CONTRACT_MESSAGE_EVENT;
    }

}
