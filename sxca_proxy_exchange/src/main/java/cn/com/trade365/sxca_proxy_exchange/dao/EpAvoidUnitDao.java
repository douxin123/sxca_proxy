package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;
import java.util.Map;

/**
 * 回避单位Dao
 */
public interface EpAvoidUnitDao extends BaseDao {
    /**
     * 获取回避单位信息
     *
     * @param bxh xm_xmfb.id
     * @return
     */
    List<Map<String, Object>> selectHbdwList(String bxh);
}
