package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EbDissentDto;
import cn.com.trade365.sxca_proxy_exchange.service.EbDissentService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 质疑投诉
 * @Auther weichunjie
 */
@Component
public class EbDissentHandler implements MessageHandler {

    @Autowired
    private IdRelationService idRelationService;

    @Autowired
    private RestComponent restComponent;

    @Autowired
    private EbDissentService  edSerivce;


    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
              Map<String,String> da=msgEvent.getData();
              EbDissentDto ed=edSerivce.getEbDissent(da.get("ZYID"));
              ed.setPlatformCode("001");
              //获取项目ID
              Long xmid=this.idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT,this.edSerivce.getXMID(da.get("ZYID")));
              ed.setTenderProjectId(xmid);
              //发送数据
            if(!idRelationService.isExitDataId(ObjectTypeEnum.EB_DESSINT,da.get("ZYID"))) {
                ResultData<Long> reslut = restComponent.post(Repositories.getUrl(Repositories.EBDISSENT_SAVE_URL), ed, new ParameterizedTypeReference<ResultData<Long>>() {
                });
                if (reslut.getCode() != 200) {
                    throw new Exception(reslut.getMessage());
                }
                this.idRelationService.relation(new RelationEntity(ObjectTypeEnum.EB_DESSINT.getCode(), da.get("ZYID"), reslut.getData(), JSONUtil.toJsonStr(ed)));
            }else{
                 Long reId=idRelationService.getDataId(ObjectTypeEnum.EB_DESSINT,da.get("ZYID"));
                 ResultData<Long> reslut = restComponent.put(Repositories.getUrl(Repositories.EBDISSENT_SAVE_URL+"/"+reId), ed, new ParameterizedTypeReference<ResultData<Long>>() {
                });
                if (reslut.getCode() != 200) {
                    throw new Exception(reslut.getMessage());
                }
            }
        }catch (Exception ex){
            throw new ExchangeException("调用失败",ex);
        }

    }

    @Override
    public String getHandleName() {
        return MsgEvent.EB_DISSENT_EVENT;
    }
}
