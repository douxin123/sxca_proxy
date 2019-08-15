package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.Map;

/**
 * 投标邀请书
 *
 * @author 宋建华
 * @date 2018-11-09 15:09
 **/
public interface BidInviteDao extends BaseDao {
    /**
     * 根据xm_tbyqxx.id获取xm_cgwj表数据
     *
     * @param tbyqxxId xm_tbyqxx.id
     * @return
     */
    Map<String, Object> getCgwjMap(String tbyqxxId);

    /**
     * 获取邀请书附件的文件路径
     * @param fileId
     * @return
     */
    String getFilePath(Object fileId);
}
