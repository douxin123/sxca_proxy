package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjSectionDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.SectionService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 项目标包数据同步
 */
@Component
public class SectionMessageHandler implements MessageHandler{

    private static Logger logger= LoggerFactory.getLogger(TenderProjectMessageHandler.class);

	@Autowired
    private RestComponent restComponent;

    @Autowired
    private SectionService sectionService;
    
    @Autowired
    private IdRelationService idRelationService;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, String> data = msgEvent.getData();
            String sectionId = data.get("sectionId");
            ResultData<Long> resultData = null;
            PrjSectionDto sectionData = null;
            boolean exitDataId = idRelationService.isExitDataId(ObjectTypeEnum.SECTION, sectionId);
            if (exitDataId) {
            	Long dataId = idRelationService.getDataId(ObjectTypeEnum.SECTION, sectionId);
            	sectionData = sectionService.getSectionData(sectionId);
            	sectionData.setId(dataId);
            	sectionData.setPlatformCode(msgEvent.getPlatformCode());
            	resultData = restComponent.put(Repositories.COMM_PREFIX + Repositories.SECTION_SAVE_URL + "/" + dataId, sectionData, new ParameterizedTypeReference<ResultData<Long>>() {
                });
            }else {
            	sectionData = sectionService.getSectionData(sectionId);
            	sectionData.setPlatformCode(msgEvent.getPlatformCode());
            	resultData = restComponent.post(
            			Repositories.COMM_PREFIX + Repositories.SECTION_SAVE_URL, sectionData,
            			new ParameterizedTypeReference<ResultData<Long>>() {
            			});
            }
            //关系表存储
            if(resultData.getData() == null) {
            	throw new ExchangeException("推送失败; " + resultData.getMessage());
            }
            if(!exitDataId) {
            	RelationEntity relationEntity = new RelationEntity();
            	relationEntity.setCode(ObjectTypeEnum.SECTION.getCode());
            	relationEntity.setTradeId(sectionData.getSourceId());
            	relationEntity.setDataId(resultData.getData());
            	relationEntity.setData(JSON.toJSONString(sectionData));
            	idRelationService.relation(relationEntity);
			}
            log.info("返回结果：{}", resultData.getMessage());
        }catch (Exception ex){
            throw new ExchangeException("调用失败",ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.SECTION_MESSAGE_EVENT;
    }

}
