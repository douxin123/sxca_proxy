package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.UnstructuredDocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author :lhl
 * @create :2018-11-15 11:17
 */
@Component
public class UnstructuredDocumentDaoImpl implements UnstructuredDocumentDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *根据变更澄清链接地址查询非结构化文档表
     * @param businessId 业务序号
     * @return 文件目录和文件名的list
     */
    @Override
    public List<Map<String, Object>> queryList(String businessId) {
        String sql = "select PATH path,realname realName from gg_fjghwd where ywxh=?";
        List<Map<String, Object>> stringObjectMap = jdbcTemplate.queryForList(sql, new Object[]{businessId});
        return stringObjectMap;
    }
}
