package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjTenderProjectDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.TenderProjectService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 招标项目数据同步
 */
@Component
public class TenderProjectMessageHandler implements MessageHandler{

    private static Logger logger= LoggerFactory.getLogger(TenderProjectMessageHandler.class);

	@Autowired
    private RestComponent restComponent;

    @Autowired
    private TenderProjectService tenderProjectService;

    @Autowired
    IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
             Map<String, String> data = msgEvent.getData();
             String tenderProjectId = data.get("tenderProjectId");
             if (idRelationService.isExitDataId(ObjectTypeEnum.TENDER_PROJECT, tenderProjectId)) {
                 logger.warn("重复推送招标项目信息["+tenderProjectId+"]");
                 return ;
             }
             PrjTenderProjectDto tenderProjectData = tenderProjectService.getTenderProjectData(tenderProjectId);
             tenderProjectData.setPlatformCode(msgEvent.getPlatformCode());
             ResultData<Long> resultData = restComponent.post(
                     Repositories.COMM_PREFIX + Repositories.TENDER_PROJECT_SAVE_URL, tenderProjectData,
                     new ParameterizedTypeReference<ResultData<Long>>() {
                     });
             //关系表存储
             if(null == resultData.getData()) {
            	 throw new ExchangeException("推送失败; " + resultData.getMessage());
             }
             RelationEntity relationEntity = new RelationEntity();
             relationEntity.setCode(ObjectTypeEnum.TENDER_PROJECT.getCode());
             relationEntity.setTradeId(tenderProjectData.getSourceId());
             relationEntity.setDataId(resultData.getData());
             relationEntity.setData(JSON.toJSONString(tenderProjectData));
             idRelationService.relation(relationEntity);
        }catch (Exception ex){
            throw new ExchangeException("调用失败",ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.TENDER_PROJECT_MESSAGE_EVENT;
    }

}
