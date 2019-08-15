package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-12 11:20
 **/
public interface BaseDao {

    /**
     * 根据id查询表tableName的单条数据
     *
     * @param tableName
     * @param id
     * @return
     */
	Map<String, Object> selectById(String tableName, String id);
    
    /**
     * 用id查询单列
     * @param selectCloum 查询的列
     * @param tableName 表名称
     * @param id 主键
     * @return
     */
	Object selectById(String selectCloum, String tableName, String id);
	

	/**
	 * 按条件查询指定对象
	 * @param tableName 表名称
	 * @param paramColumn 条件列
	 * @param param 查询的参数值
	 * @return
	 */
	List<Map<String, Object>> selectByParamColumn(String tableName, String paramColumn, String param);


	/**
	 * 按条件查询指定列
	 * @param selectColumn 查询的列
	 * @param tableName 表名称
	 * @param paramColumn 条件列
	 * @param param 查询的参数值
	 * @return
	 */
	Object selectByParamColumn(String selectColumn, String tableName, String paramColumn, String param);


	/**
	 * 按条件查询指定列
	 * @param selectColumn 查询的列
	 * @param tableName 表名称
	 * @param paramColumn 条件列
	 * @param param 查询的参数值
	 * @return
	 */
	Map<String, Object> selectMapByParamColumn(String tableName, String paramColumn, String param);
}
