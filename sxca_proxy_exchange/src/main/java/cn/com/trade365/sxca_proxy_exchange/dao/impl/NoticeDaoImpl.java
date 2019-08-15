package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.NoticeDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
@Component
public class NoticeDaoImpl extends BaseDaoImpl implements NoticeDao {

    /**
     * 查询公告相关发布媒体名称
     * @param tradeNoticeId 公告id
     * @return
     */
    @Override
    public List<String> getMediaName(String tradeNoticeId) {
        String sql =  "select wzmc from xm_ggwz where ggxh = ?";
        List<String> mediaList = jdbcTemplate.queryForList(sql, String.class, tradeNoticeId);
        return mediaList;
    }

	@Override
	public List<Map<String, Object>> getTenderFile(String tradeProjectId, Integer lx) {
		String sql =  "select * from xm_cgwj where zbxh = :zbxh and lx = :lx";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("zbxh", tradeProjectId);  
	    paramMap.put("lx", lx);
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}
}
