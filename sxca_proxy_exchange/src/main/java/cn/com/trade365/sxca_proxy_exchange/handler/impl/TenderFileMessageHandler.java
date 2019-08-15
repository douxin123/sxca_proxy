package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjTenderFileDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.TenderFileService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 招标项目数据同步
 */
@Component
public class TenderFileMessageHandler implements MessageHandler{

	@Autowired
    private RestComponent restComponent;

    @Autowired
    private TenderFileService tenderFileService;

    @Autowired
    private IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
             Map<String, String> data = msgEvent.getData();
             String tenderFileSectionId = data.get("tenderFileSectionId");
             if (idRelationService.isExitDataId(ObjectTypeEnum.TENDER_FILE, tenderFileSectionId)) {
            	 throw new ExchangeException(ObjectTypeEnum.TENDER_FILE.getCode() + "重复推送数据");
             }
             PrjTenderFileDto prjTenderFile = tenderFileService.getTenderFileData(tenderFileSectionId);
             prjTenderFile.setPlatformCode(msgEvent.getPlatformCode());
             prjTenderFile.setSourceId(tenderFileSectionId);
             ResultData<Long> resultData = restComponent.post(
                     Repositories.COMM_PREFIX + Repositories.TENDER_FILE_SAVE_URL, prjTenderFile,
                     new ParameterizedTypeReference<ResultData<Long>>() {
                     });
             //关系表存储
             if(null == resultData.getData()) {
            	 throw new ExchangeException("推送失败; " + resultData.getMessage());
             }
             RelationEntity relationEntity = new RelationEntity();
             relationEntity.setCode(ObjectTypeEnum.TENDER_FILE.getCode());
             relationEntity.setTradeId(tenderFileSectionId);
             relationEntity.setDataId(resultData.getData());
             relationEntity.setData(JSON.toJSONString(prjTenderFile));
             idRelationService.relation(relationEntity);
        }catch (Exception ex){
            throw new ExchangeException("调用失败",ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.TENDER_FILE_MESSAGE_EVENT;
    }

}
