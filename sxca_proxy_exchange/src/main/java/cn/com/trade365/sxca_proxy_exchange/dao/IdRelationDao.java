package cn.com.trade365.sxca_proxy_exchange.dao;

import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;

import java.util.List;
import java.util.Map;

/**
 * @author :lhl
 * @create :2018-11-06 15:37
 */
public interface IdRelationDao {

     void save(RelationEntity relationEntity);


     RelationEntity getRelationByDataId(String code, String tradeId);

     RelationEntity getRelationByTradeId(String code, String dataId) throws ExchangeException;

     boolean isExitDataId(String code, String dataId);

     List<Map<String,Object>> getRelationIdListByTradeId(String code, List<String> tradeIdList);
}
