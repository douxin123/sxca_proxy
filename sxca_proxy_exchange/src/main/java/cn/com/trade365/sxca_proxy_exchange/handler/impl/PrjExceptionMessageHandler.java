package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjExceptionDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.PrjExceptionService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 招标异常情况报告同步
 */
@Component
public class PrjExceptionMessageHandler implements MessageHandler {


    @Autowired
    IdRelationService idRelationService;

    private static final Logger log = LoggerFactory.getLogger(PrjExceptionMessageHandler.class);

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private PrjExceptionService prjExceptionService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, String> dataMap = msgEvent.getData();
            if (StringUtils.isNotEmpty(dataMap.get("sectionId"))) {
                String sectionId = dataMap.get("sectionId");
                Long dataId;
                PrjExceptionDto exception = prjExceptionService.getExceptionData(msgEvent);
                exception.setPlatformCode(msgEvent.getPlatformCode());
                ResultData<Long> result;
                ResultData<Boolean> updateResult;
                if (!idRelationService.isExitDataId(ObjectTypeEnum.PRJEXCEPTION, sectionId)) {
                    // 推送
                    result = restComponent.post(Repositories.getUrl(Repositories.PRJEXCEPTION_SAVE_URL), exception, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (result.getCode() != 200) {
                        throw new Exception(result.getMessage());
                    }
                    // 关系表存储
                    RelationEntity relationEntity = new RelationEntity();
                    relationEntity.setCode(ObjectTypeEnum.PRJEXCEPTION.getCode());
                    relationEntity.setTradeId(sectionId);
                    relationEntity.setData(JSONObject.toJSONString(dataMap));
                    relationEntity.setDataId(result.getData());
                    idRelationService.relation(relationEntity);
                    log.info("异常情况报告推送成功");
                } else {
                    dataId = idRelationService.getDataId(ObjectTypeEnum.PRJEXCEPTION, sectionId);
                    // 更新
                    updateResult = restComponent.put(Repositories.getUrl(Repositories.PRJEXCEPTION_SAVE_URL + "/" + dataId),exception , new ParameterizedTypeReference<ResultData<Boolean>>() {
                    });
                    if (updateResult.getCode() != 200) {
                        throw new Exception(updateResult.getMessage());
                    }
                    log.info("异常情况报告更新成功");
                }

            }

        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.EXCEPTION_MESSAGE_EVENT;
    }

}
