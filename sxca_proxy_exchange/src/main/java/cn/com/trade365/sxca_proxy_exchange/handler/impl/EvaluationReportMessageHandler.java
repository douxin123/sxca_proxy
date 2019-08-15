package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EbEvaluationReportDto;
import cn.com.trade365.sxca_proxy_exchange.service.EvaluationReportService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评标报告同步
 */
@Component
public class EvaluationReportMessageHandler implements MessageHandler {

	@Autowired
	IdRelationService idRelationService;

	@Autowired
	RestComponent restComponent;

	@Autowired
	EvaluationReportService evaluationReportService;

	@Override
	public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
		Map<String, String> dataMap = msgEvent.getData();
		String[] tradeSectionArr = dataMap.get("tradeSectionId").split(",");
		try {
			List<EbEvaluationReportDto> dtoList = new ArrayList<EbEvaluationReportDto>();
			for (String tradeSectionId : tradeSectionArr) {
				EbEvaluationReportDto ebEvaluationReport = new EbEvaluationReportDto();
				ebEvaluationReport.setPlatformCode(msgEvent.getPlatformCode());
				evaluationReportService.setEbEvaluationReport(ebEvaluationReport, tradeSectionId);
				if (StringUtils.isNotBlank(String.valueOf(ebEvaluationReport.getSectionId()))){
					dtoList.add(ebEvaluationReport);
				}
			}
			
			if (CollectionUtil.isEmpty(dtoList)){
				throw new ExchangeException(ObjectTypeEnum.EB_EVALUATION_REPORT.getCode() + "重复推送数据");
			}

			for (EbEvaluationReportDto ebEvaluationReport : dtoList) {
				ResultData<Long> resultData = restComponent.post(
						Repositories.COMM_PREFIX + Repositories.EB_EVALUATION_REPORT_SAVE_URL, ebEvaluationReport,
						new ParameterizedTypeReference<ResultData<Long>>() {
						});
				if (StringUtils.isBlank(String.valueOf(resultData.getData()))) {
					throw new ExchangeException("数据交换平台存储评审报告失败");
				}
				// 关系表存储
				idRelationService.relation(ObjectTypeEnum.EB_EVALUATION_REPORT, ebEvaluationReport.getSourceId(),
						resultData.getData(), JSON.toJSONString(ebEvaluationReport));
			}

		} catch (Exception e) {
			throw new ExchangeException("调用失败", e);
		}
	}

	@Override
	public String getHandleName() {
		return MsgEvent.EB_EVALUATION_REPORT_MESSAGE_EVENT;
	}

}
