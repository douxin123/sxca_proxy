package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.EvaluationReportDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评审报告
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
@Component
public class EvaluationReportDaoImpl extends BaseDaoImpl implements EvaluationReportDao {

    /**
     * 按包序号查询资审评审结果，按时间排序
     * @param tradeSectionId
     * @return
     */
    @Override
    public List<Map<String, Object>> getQualifyListOrderByTime(String tradeSectionId) {
        String sql = "select * from xm_zsjg where bxh = ? order by scsj desc";
        return jdbcTemplate.queryForList(sql, tradeSectionId);
    }

	@Override
	public List<Map<String, Object>> getReportSectionId(String tradeSecitonId, Integer type) {
		String sql =  "select * from xm_pswdfb where bxh = :bxh and lx = :lx";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("bxh", tradeSecitonId);  
	    paramMap.put("lx", type);
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}
}
