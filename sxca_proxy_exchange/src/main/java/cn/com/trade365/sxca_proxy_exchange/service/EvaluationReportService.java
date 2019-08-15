package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.EbEvaluationReportDto;

/**
 * 内控与数据中心数据关联 评标报告
 */
public interface EvaluationReportService extends BaseService {

    void setEbEvaluationReport(EbEvaluationReportDto ebEvaluationReport, String tradeSecitonId) throws Exception;
}
