package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.BaseDao;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-12 11:20
 **/
public class BaseDaoImpl implements BaseDao {
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 通过主键和表名称查询 表中id对应的行
	 * 
	 * @param tableName
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> selectById(String tableName, String id) {
		List<Map<String, Object>> resultList = selectByParamColumn(tableName, "id", id);
		return CollectionUtil.isEmpty(resultList) ? null : resultList.get(0);
	}

	/**
	 * 通过主键，表名称，指定列名查询指定id行的selectColumn列的值
	 * 
	 * @param selectColumn
	 * @param tableName
	 *            表名称
	 * @param id
	 *            主键
	 * @return
	 */
	@Override
	public Object selectById(String selectColumn, String tableName, String id) {
		return selectByParamColumn(selectColumn, tableName, "id", id);
	}

	/**
	 * 通过tableName表名称，查询所有符合 paramColumn = param 的列
	 * 
	 * @param paramColumn
	 *            条件列
	 * @param tableName
	 *            表名称
	 * @param param
	 *            查询的参数值
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectByParamColumn(String tableName, String paramColumn, String param) {
		String sql = "select * from " + tableName + " where " + paramColumn + " = ?";
		return jdbcTemplate.queryForList(sql, param);
	}

	/**
	 * 通过tableName表名称，查询符合 paramColumn = param 的列中selectColumn的值
	 * 
	 * @param selectColumn
	 *            查询的列
	 * @param tableName
	 *            表名称
	 * @param paramColumn
	 *            条件列
	 * @param param
	 *            查询的参数值
	 * @return
	 */
	@Override
	public Object selectByParamColumn(String selectColumn, String tableName, String paramColumn, String param) {
		List<Map<String, Object>> resultList = selectByParamColumn(tableName, paramColumn, param);
		return CollectionUtil.isEmpty(resultList) ? null : resultList.get(0).get(selectColumn);
	}


	/**
	 * 通过tableName表名称，查询符合 paramColumn = param 的列中selectColumn的值
	 *
	 * @param tableName
	 *            表名称
	 * @param paramColumn
	 *            条件列
	 * @param param
	 *            查询的参数值
	 * @return
	 */
	@Override
	public Map<String, Object> selectMapByParamColumn(String tableName, String paramColumn, String param) {
		List<Map<String, Object>> resultList = selectByParamColumn(tableName, paramColumn, param);
		return CollectionUtil.isEmpty(resultList) ? null : resultList.get(0);
	}

}
