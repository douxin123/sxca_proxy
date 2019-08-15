package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内控与数据中心数据关联
 */
@Component
public class IdRelationServiceImpl implements IdRelationService {

    @Autowired
    private IdRelationDao idRelationDao;
    /**
     * 类型
     * @param relationEntity 关联对象
     */
    @Override
    public int relation(RelationEntity relationEntity){
        idRelationDao.save(relationEntity);
        return relationEntity.getId();
    }

    @Override
    public int relation(ObjectTypeEnum objectTypeEnum,String tradeId,Long dataId,String data){
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setCode(objectTypeEnum.getCode());
        relationEntity.setTradeId(tradeId);
        relationEntity.setDataId(dataId);
        relationEntity.setData(data);
        return relation(relationEntity);
    }

    /**
     * 根据内控id获取数据中心id
     * @param objectType
     * @param tradeId
     * @return
     */
    @Override
    public Long getDataId(ObjectTypeEnum objectType,String tradeId) throws ExchangeException {
        return idRelationDao.getRelationByTradeId(objectType.getCode(),tradeId).getDataId();
    }

    /**
     * 根据数据中心ID获取内控id
     * @param objectType
     * @param dataId
     * @return
     */
    @Override
    public String getTradeId(ObjectTypeEnum objectType,String dataId){
        return idRelationDao.getRelationByDataId(objectType.getCode(),dataId).getTradeId();
    }

    /**
     * 根据内控id判断是否已经推送过数据
     * @param objectType
     * @param tradeId
     * @return
     */
    @Override
    public boolean isExitDataId(ObjectTypeEnum objectType, String tradeId) {
        return idRelationDao.isExitDataId(objectType.getCode(),tradeId);
    }

    /**
     * 根据内控id获取数据中心id List
     * @param objectType
     * @param tradeId
     * @return List
     */
	@Override
	public List<Long> getDataIdList(ObjectTypeEnum objectType, List<String> tradeId) {
		List<Map<String,Object>> resultList =idRelationDao.getRelationIdListByTradeId(objectType.getCode(), tradeId);
		List<Long> list = new ArrayList<Long>();
		if (CollectionUtil.isNotEmpty(resultList)) {
			for (Map<String,Object> map : resultList) {
				list.add(Convert.toLong(map.get("dataId")));
			}
		}
		return list;
	}
}
