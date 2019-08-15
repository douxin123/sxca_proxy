package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.Map;

/**
 * 招标文件持久层接口
 * @author zhanghongbing
 * 
 */
public interface TenderFileDao extends BaseDao {
	
	/**
	 * 通过分包的id查询标包的保证金等相关信息
	 * @param projectType 标包的类型
	 * @param sectionId 分包的id
	 * @param costCategory 费用类别
	 * @return
	 */
	Map<String, Object> querySectionInfo(int projectType, String sectionId, int costCategory);
}
