package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;

import java.util.List;

/**
 * 内控与数据中心数据关联
 */
public interface IdRelationService  extends BaseService {
    /**
     * 存储关系
     * @param relationEntity 关联对象
     */
     int relation(RelationEntity relationEntity);

    /**
     * 存储关系
     * @param objectTypeEnum
     * @param tradeId
     * @param dataId
     * @param data
     */
     int relation(ObjectTypeEnum objectTypeEnum, String tradeId, Long dataId, String data);
    /**
     * 根据内控id获取数据中心id
     * @param objectType
     * @param tradeId
     * @return
     */
     Long getDataId(ObjectTypeEnum objectType, String tradeId) throws ExchangeException;

    /**
     * 根据数据中心ID获取内控id
     * @param objectType
     * @param dataId
     * @return
     */
    String getTradeId(ObjectTypeEnum objectType, String dataId);

    /**
     * 根据内控id判断是否已经推送过数据
     * @param objectType
     * @param tradeId
     * @return
     */
    boolean isExitDataId(ObjectTypeEnum objectType, String tradeId);
    
    /**
     * 根据内控id获取数据中心id
     * @param objectType
     * @param tradeId
     * @return
     */
    List<Long> getDataIdList(ObjectTypeEnum objectType, List<String> tradeId);

}
