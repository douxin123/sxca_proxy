package cn.com.trade365.sxca_proxy_exchange.dao;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 评审专家
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
@Component
public interface EvaluationExpertDao extends BaseDao {
	
	Map<String, Object> getTradeManagerIdbySection(String evaluationExpertGroupId, String tradeSectionId);
}
