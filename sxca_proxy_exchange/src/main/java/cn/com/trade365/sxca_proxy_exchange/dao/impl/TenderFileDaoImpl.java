package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.TenderFileDao;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 招标文件持久层实现
 * @author zhanghongbing
 *
 */
@Component
public class TenderFileDaoImpl extends BaseDaoImpl implements TenderFileDao {
	
	@Override
	public Map<String, Object> querySectionInfo(int projectType, String sectionId, int costCategory){
		String sql = "SELECT xm_xmfb.zgyq, xm_xmfb.kbdd, xm_sfbz.* FROM xm_xmfb, xm_sfbz WHERE "
				+ "xm_xmfb.id = xm_sfbz.bxh AND xm_sfbz.lx = ? AND xm_xmfb.id = ? AND xm_sfbz.fylb = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, projectType, sectionId, costCategory);
		return CollectionUtil.isEmpty(resultList) ? null : resultList.get(0);
	}
	
}
