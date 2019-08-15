package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;
import java.util.Map;

/**
 * 开标记录持久层接口
 * @author zhangjianbing
 * 
 */
public interface OpenBidDao extends BaseDao {
    /**
      * @Description: 获取开标记录信息
      * @Param:  sectionId
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    List<Map<String,Object>> queryOpenBidRecord(String sectionId);
}

