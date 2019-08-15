package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.EvaluationReportDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.EbEvaluationReportDto;
import cn.com.trade365.sxca_proxy_exchange.service.EvaluationReportService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.utils.FileUtil;
import cn.com.trade365.store.Client;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内控与数据中心数据关联 评标报告
 * 
 */
@Service
public class EvaluationReportImpl implements EvaluationReportService {

	@Autowired
	EvaluationReportDao evaluationReportDao;

	@Autowired
	IdRelationService idRelationService;

	@Autowired
	Client fileClient;

	@Autowired
	BeanConfig beanConfig;

	@Override
	public void setEbEvaluationReport(EbEvaluationReportDto ebEvaluationReport, String tradeSecitonId)
			throws Exception {
		Integer type = Convert.toInt(evaluationReportDao.selectByParamColumn("lx", "xm_xmfb", "id", tradeSecitonId));
		List<Map<String, Object>> tradeEvaluationReportList = evaluationReportDao.getReportSectionId(tradeSecitonId,
				type);

		String reportSectionId = Convert.toStr(tradeEvaluationReportList.get(0).get("id"));
		String tradeReportRelationId = Convert.toStr(tradeEvaluationReportList.get(0).get("bgxh"));
		// 评审文档数据id
		List<Map<String, Object>> tradeEvaluationReportMapList = evaluationReportDao.selectByParamColumn("xm_pswd",
				"bgxh", tradeReportRelationId);
		Map<String, Object> tradeEvaluationReportMap = evaluationReportDao.selectById("xm_pswd", tradeReportRelationId);
		if (idRelationService.isExitDataId(ObjectTypeEnum.EB_EVALUATION_REPORT,
				reportSectionId + Convert.toStr(tradeEvaluationReportMap.get("lx")))) {
			return;
		}
		if (StringUtils.isEmpty(tradeReportRelationId)) {
			throw new ExchangeException("调用失败，未查询到相关评标报告信息，包id=" + tradeSecitonId);
		}
		String tradeProjectId = Convert.toStr(tradeEvaluationReportMap.get("zbxh"));

		Long dataSectionId = idRelationService.getDataId(ObjectTypeEnum.SECTION, tradeSecitonId);
		Long dataProjectId = idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, tradeProjectId);
		// 招标项目编号
		ebEvaluationReport.setTenderProjectId(dataProjectId);
		// 标包主键
		ebEvaluationReport.setSectionId(dataSectionId);
		// 类型
		ebEvaluationReport.setProjectType(Convert.toInt(tradeEvaluationReportMap.get("lx")));
		// 上传文件doc
		String docNum = Convert.toStr(tradeEvaluationReportMap.get("wd"));
		Map<String, Object> attachment = evaluationReportDao.selectMapByParamColumn("gg_fjghwd", "ywxh", docNum);
		if (CollectionUtil.isNotEmpty(attachment)) {
			Long attachmentDocId = fileClient.store(
					new File(beanConfig.getFilePath() + attachment.get("path") + attachment.get("realname")), null);
			ebEvaluationReport.setEvaluationDoc(attachmentDocId);
			// html
			ebEvaluationReport.setEvaluationHtml(FileUtil.getDocFileContent(
					beanConfig.getFilePath() + attachment.get("path"), Convert.toStr(attachment.get("realname"))));
		}
		// 评标报告内容
		ebEvaluationReport.setEvaluationContent("详见文档");
		// 评标报告提交时间
		ebEvaluationReport.setSubmitTime(DateUtil.parse(Convert.toStr(tradeEvaluationReportMap.get("qcsj"))));
		// 评标结束时间
		ebEvaluationReport.setEndTime(DateUtil.parse(Convert.toStr(tradeEvaluationReportMap.get("pbjssj"))));
		// 评标结果
		ebEvaluationReport.setEvaluationResult(3);
		// 附件关联标识号以项文逗号隔开
		List<Long> attachList = new ArrayList<Long>();
		for (Map<String, Object> map : tradeEvaluationReportMapList) {
			String fileNum = Convert.toStr(map.get("wd"));
			if (StringUtils.isNotEmpty(fileNum) && !fileNum.equals(docNum)) {
				attachment = evaluationReportDao.selectMapByParamColumn("gg_fjghwd", "ywxh", fileNum);
				if (CollectionUtil.isNotEmpty(attachment)) {
					Long attachmentDocId = fileClient.store(
							new File(beanConfig.getFilePath() + attachment.get("path") + attachment.get("realname")),
							null);
					attachList.add(attachmentDocId);
				}
			}
		}
		ebEvaluationReport.setAttchmentCode(StringUtils.join(attachList, ","));

		ebEvaluationReport.setSourceId(reportSectionId + Convert.toStr(tradeEvaluationReportMap.get("lx")));
	}
}
