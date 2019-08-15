package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjTenderClarifyDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.ProjectTenderClarifyService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 招标文件澄清
 * @author lhl
 */
@Component
public class ProjectTenderClarifyHandler implements MessageHandler {


    @Autowired
    IdRelationService idRelationService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RestComponent restComponent;
    @Autowired
    ProjectTenderClarifyService projectTenderClarifyService;

    private static final String CLARIFY_ID = "clarifyId";

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            String clarifyId = msgEvent.getData().get(CLARIFY_ID);
            //获取变更澄清文件分包列表
            List<PrjTenderClarifyDto> projectTenderClarifyList = projectTenderClarifyService.getProjectTenderClarify(clarifyId);
            if (!idRelationService.isExitDataId(ObjectTypeEnum.PROJECTETENDERCLARIFY, clarifyId)) {
                for (PrjTenderClarifyDto projectTenderClarify : projectTenderClarifyList) {
                    //设置消息中平台码
                    projectTenderClarify.setPlatformCode(msgEvent.getPlatformCode());

                    ResultData<Long> result = restComponent.post(Repositories.getUrl(Repositories.PROJECT_TENDER_CLARIFY_URL), projectTenderClarify, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (result.getCode() != 200) {
                        throw new Exception(result.getMessage());
                    }
                    // 关系表存储
                    RelationEntity relationEntity = new RelationEntity();
                    relationEntity.setCode(ObjectTypeEnum.PROJECTETENDERCLARIFY.getCode());
                    relationEntity.setTradeId(clarifyId);
                    relationEntity.setData(JSONObject.toJSONString(msgEvent.getData()));
                    relationEntity.setDataId(result.getData());
                    idRelationService.relation(relationEntity);
                }
            } else {
                //发送更新
                for (PrjTenderClarifyDto projectTenderClarify : projectTenderClarifyList) {
                    Long dataId = idRelationService.getDataId(ObjectTypeEnum.PROJECTETENDERCLARIFY, clarifyId);
                    ResultData<Boolean> result = restComponent.put(Repositories.getUrl(Repositories.PROJECT_TENDER_CLARIFY_URL + "/" + dataId), projectTenderClarify, new ParameterizedTypeReference<ResultData<Boolean>>() {
                    });
                    if (result.getCode() != 200) {
                        throw new Exception(result.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.PROJECT_TENDER_CLARIFY_EVENT;
    }

}
