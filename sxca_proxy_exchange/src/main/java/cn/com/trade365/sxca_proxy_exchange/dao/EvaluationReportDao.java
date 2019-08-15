package cn.com.trade365.sxca_proxy_exchange.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 评审报告
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
@Component
public interface EvaluationReportDao extends BaseDao {

    /**
     * 按包序号查询资审评审结果，按时间排序
     * @param tradeSectionId
     * @return
     */
    List<Map<String,Object>> getQualifyListOrderByTime(String tradeSectionId);
    
    List<Map<String, Object>> getReportSectionId(String tradeSecitonId, Integer type);
}
