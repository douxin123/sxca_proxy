package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.EvaluationExpertDao;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 评审专家
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
@Component
public class EvaluationExpertDaoImpl extends BaseDaoImpl implements EvaluationExpertDao {

	@Override
	public Map<String, Object> getTradeManagerIdbySection(String evaluationExpertGroupId, String tradeSectionId) {
		String sql = "select * from zb_zjsq_fb where ZJSQBH = ? and bxh = ?";
		Object[] obj = { evaluationExpertGroupId, tradeSectionId };
		return jdbcTemplate.queryForMap(sql, obj);
	}

}
