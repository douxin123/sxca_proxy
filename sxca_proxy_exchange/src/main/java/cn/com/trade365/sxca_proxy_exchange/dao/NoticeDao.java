package cn.com.trade365.sxca_proxy_exchange.dao;


import java.util.List;
import java.util.Map;

/**
 * 公告
 *
 * @author fanyanqi
 * @date 2018-11-13
 */
public interface NoticeDao extends BaseDao{

    /**
     * 查询公告相关发布媒体名称
     * @param tradeNoticeId 公告id
     * @return
     */
    List<String> getMediaName(String tradeNoticeId);
    
    /**
     * 通过标包获取招标文件
     * @return
     */
	List<Map<String, Object>> getTenderFile(String tradeProjectId, Integer lx);
}
