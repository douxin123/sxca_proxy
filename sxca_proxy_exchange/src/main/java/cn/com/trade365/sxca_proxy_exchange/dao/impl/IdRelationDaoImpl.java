package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.hutool.core.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author :lhl
 * @create :2018-11-06 15:38
 */
@Component
public class IdRelationDaoImpl implements IdRelationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(IdRelationDaoImpl.class);


    @Override
    public void save(RelationEntity relationEntity) {
        String sql = "insert into ex_relation (trade_id,data_id,data,code) values(?,?,?,?)";
        jdbcTemplate.update(sql, relationEntity.getTradeId(), relationEntity.getDataId(), relationEntity.getData(), relationEntity.getCode());
    }

    @Override
    public RelationEntity getRelationByDataId(String code, String dataId) {
        String sql = "select trade_id tradeId,data_id dataId,code ,data  from ex_relation where code=? and data_id=?";
        Object[] objects = {code, dataId};
        RelationEntity relationEntity = jdbcTemplate.queryForObject(sql, objects, new BeanPropertyRowMapper<>(RelationEntity.class));
        return relationEntity;
    }

    @Override
    public RelationEntity  getRelationByTradeId(String code, String tradeId) throws ExchangeException {
        String sql = "select trade_id tradeId,data_id dataId,code ,data  from ex_relation where code=? and trade_id=?";
        Object[] objects = {code, tradeId};
        try {
        	RelationEntity relationEntity = jdbcTemplate.queryForObject(sql, objects, new BeanPropertyRowMapper<>(RelationEntity.class));
        	return relationEntity;
        }catch(Exception e){
        	throw new ExchangeException("调用失败，trade_id = " + tradeId + ", code = " + code,e);
        }
    }

    @Override
    public boolean isExitDataId(String code, String tradeId) {
        try {
             String sql = "select trade_id from ex_relation where code=? and trade_id=?";
             Object[] objects = {code, tradeId};
             return CollectionUtil.isNotEmpty(jdbcTemplate.queryForList(sql, objects));
        }catch (Exception e){
            log.info("isExitDataId异常",e);
        }
        return  false;
    }

	@Override
	public List<Map<String,Object>> getRelationIdListByTradeId(String code, List<String> tradeIdList) {
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("tradeIdList", tradeIdList);  
	    paramMap.put("code", code);
		String sql = "select data_id dataId from ex_relation where code=:code and trade_id in (:tradeIdList)";
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}
}
