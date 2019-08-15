package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.platform.project.dto.EbEvaluationExpertDto;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.sxca_proxy_exchange.service.EvaluationExpertService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评标专家同步
 *
 * @author admin
 * @since 2018-11-09
 */
@Component
public class EvaluationExpertMessageHandler implements MessageHandler {

	@Autowired
	IdRelationService idRelationService;

	@Autowired
	RestComponent restComponent;

	@Autowired
	EvaluationExpertService evaluationExpertService;

	@Override
	public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
		Map<String, String> dataMap = msgEvent.getData();
		boolean isExit = idRelationService.isExitDataId(ObjectTypeEnum.EB_EVALUATION_EXPERT,
				dataMap.get("evaluationExpertGroupId"));
		if (isExit) {
			throw new ExchangeException(ObjectTypeEnum.EB_EVALUATION_EXPERT.getCode() + "重复推送数据");
		}
		try {
			List<EbEvaluationExpertDto> evaluationExpertList = new ArrayList<EbEvaluationExpertDto>();
			evaluationExpertService.setEvaluationExpert(evaluationExpertList, dataMap.get("evaluationExpertGroupId"));
			for (EbEvaluationExpertDto evaluationExpert : evaluationExpertList) {
				evaluationExpert.setPlatformCode("A00001");

				ResultData<Long> resultData = restComponent.post(
						Repositories.COMM_PREFIX + Repositories.EB_EVALUATION_EXPERT_SAVE_URL, evaluationExpert,
						new ParameterizedTypeReference<ResultData<Long>>() {
						});
				if (StringUtils.isBlank(String.valueOf(resultData.getData()))) {
					throw new ExchangeException("数据交换平台存储评标专家失败");
				}
				// 关系表存储
				idRelationService.relation(ObjectTypeEnum.EB_EVALUATION_EXPERT, dataMap.get("evaluationExpertGroupId"),
						resultData.getData(), JSON.toJSONString(evaluationExpert));
			}
		} catch (Exception e) {
			throw new ExchangeException("调用失败", e);
		}
	}

	@Override
	public String getHandleName() {
		return MsgEvent.EB_EVALUATION_EXPERT_MESSAGE_EVENT;
	}

}
