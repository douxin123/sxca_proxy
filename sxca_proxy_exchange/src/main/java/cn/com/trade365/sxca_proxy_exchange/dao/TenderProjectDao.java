package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;

/**
 * 招标项目持久层接口
 * @author zhanghongbing
 * 
 */
public interface TenderProjectDao extends BaseDao {
    List<String> getSupervisePlatformsByXmid(String xmid);
}
