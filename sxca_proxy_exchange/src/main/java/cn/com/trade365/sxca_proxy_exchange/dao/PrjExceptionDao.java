package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.Map;

/**
 * 招标异常情况信息持久层接口
 *
 * @author zhangjianbing
 */
public interface PrjExceptionDao extends BaseDao {
    /**
     * @Description: 获取招标项目异常信息
     * @Param: sectionId
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    Map<String, Object> queryPrjExcInfo(String sectionId);
}

