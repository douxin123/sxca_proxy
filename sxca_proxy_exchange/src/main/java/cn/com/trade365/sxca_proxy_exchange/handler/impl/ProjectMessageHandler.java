package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjProjectDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.ProjectService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 项目数据同步
 */
@Component
@Transactional
public class ProjectMessageHandler implements MessageHandler{

    private static Logger logger= LoggerFactory.getLogger(ProjectMessageHandler.class);

    @Autowired
    private IdRelationService idRelationService;

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private ProjectService projectMsgService;
    
    @Override
    @Transactional
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, String> data = msgEvent.getData();
            String projectId = data.get("projectId");
            if (idRelationService.isExitDataId(ObjectTypeEnum.PROJECT, projectId)) {
                logger.warn("重复推送项目信息["+projectId+"]");
                return ;
            }
            PrjProjectDto projectData = projectMsgService.getProjectData(projectId);
            projectData.setPlatformCode(msgEvent.getPlatformCode());
            String url = Repositories.COMM_PREFIX + Repositories.PROJECT_SAVE_URL;
            ResultData<Long> resultData = restComponent.post(url, projectData,
                    new ParameterizedTypeReference<ResultData<Long>>() {
                    });
            //关系表存储
            if(resultData.getData() == null) {
            	throw new ExchangeException("推送失败; " + resultData.getMessage());
            }
        	RelationEntity relationEntity = new RelationEntity();
        	relationEntity.setCode(ObjectTypeEnum.PROJECT.getCode());
        	relationEntity.setTradeId(projectData.getSourceId());
        	relationEntity.setDataId(resultData.getData());
        	relationEntity.setData(JSON.toJSONString(projectData));
        	idRelationService.relation(relationEntity);
        } catch (Exception ex) {
            throw new ExchangeException("项目调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.PROJECT_MESSAGE_EVENT;
    }

}
