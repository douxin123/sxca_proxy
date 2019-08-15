package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.platform.project.dto.UsOrganizationDto;
import cn.com.trade365.platform.project.dto.UsUserDto;
import cn.com.trade365.platform.project.dto.UsUserOrganizationDto;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.UserMsgService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据同步
 *
 * @AUTHER weichunjie
 */
@Component
public class UserMessageHandler implements MessageHandler {
    @Autowired
    IdRelationService idRelationService;
    @Autowired
    private UserMsgService userMsgService;

    @Autowired
    private RestComponent restComponent;

    @Override
    public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
        try {
            Map<String, String> data = msgEvent.getData();
            UsUserOrganizationDto uo = new UsUserOrganizationDto();
            ResultData<Long> reslut = ResultData.ERROR().setMessage("操作失败");
            Map<String, Long> entMap = new HashMap<>();
            //传入orgId但企业不存在的情况
            if (!StringUtils.isEmpty(data.get("orgId"))) {
                //保存机构信息
                UsOrganizationDto org = this.userMsgService.getOrg(data.get("orgId"));
                if (org.getId() == null) {
                    reslut = restComponent.post(Repositories.getUrl(Repositories.ORG_SAVE_URL), org, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (reslut.getCode() != 200) {
                        throw new Exception(reslut.getMessage());
                    }
                    RelationEntity orgEnt = new RelationEntity();
                    orgEnt.setData(JSONUtil.toJsonStr(org));
                    orgEnt.setTradeId(data.get("orgId"));
                    orgEnt.setDataId(reslut.getData());
                    orgEnt.setCode(ObjectTypeEnum.ORG.getCode());
                    idRelationService.relation(orgEnt);
                    entMap.put("orgId", reslut.getData());
                    this.userMsgService.updateOrgData(data.get("orgId"), orgEnt.getDataId());
                } else {
                    reslut = restComponent.put(Repositories.getUrl(Repositories.ORG_SAVE_URL + "/" + org.getId()), org, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (reslut.getCode() != 200) {
                        throw new Exception(reslut.getMessage());
                    }
                    entMap.put("orgId", org.getId());
                }
            }
            if (!StringUtils.isEmpty(data.get("userId"))) {
                UsUserDto user = this.userMsgService.getUser(data.get("userId"));
                if (user.getId() == null) {
                    //设置企业ID
                    user.setOrganizationId(entMap.get("orgId"));
                    reslut = restComponent.post(Repositories.getUrl(Repositories.USER_SAVE_URL), user, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (reslut.getCode() != 200) {
                        throw new Exception(reslut.getMessage());
                    }
                    RelationEntity userEnt = new RelationEntity();
                    userEnt.setCode(ObjectTypeEnum.USER.getCode());
                    userEnt.setDataId(reslut.getData());
                    userEnt.setData(JSONUtil.toJsonStr(user));
                    userEnt.setTradeId(data.get("userId"));
                    idRelationService.relation(userEnt);
                    entMap.put("userId", userEnt.getDataId());
                    this.userMsgService.updateUserData(data.get("userId"), userEnt.getDataId());
                } else {
                    //发送更新
                    reslut = restComponent.put(Repositories.getUrl(Repositories.USER_SAVE_URL + "/" + user.getId()), user, new ParameterizedTypeReference<ResultData<Long>>() {
                    });
                    if (reslut.getCode() != 200) {
                        throw new Exception(reslut.getMessage());
                    }
                    entMap.put("userId", user.getId());
                }
            }
            if (!StringUtils.isEmpty(entMap) && !StringUtils.isEmpty(entMap.get("userId")) && !StringUtils.isEmpty(entMap.get("orgId"))) {
                //查询关系存不存在
                uo.setUserId(ConvertUtils.convert2Long(entMap.get("userId")));
                uo.setOrganizationId(ConvertUtils.convert2Long(entMap.get("orgId")));
                reslut = restComponent.post(Repositories.getUrl(Repositories.USER_ORG_SAVE_URL), uo, new ParameterizedTypeReference<ResultData<Long>>() {
                });
                if (reslut.getCode() != 200) {
                    throw new Exception("用户和机构关系保存失败");
                }
            }

        } catch (Exception ex) {
            throw new ExchangeException("调用失败", ex);
        }
    }

    @Override
    public String getHandleName() {
        return MsgEvent.USER_MESSAGE_EVENT;
    }

}
