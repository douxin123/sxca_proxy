package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.ProcurementDocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author :lhl
 * @create :2018-11-21 16:55
 */
@Component
public class ProcurementDocumentDaoImpl implements ProcurementDocumentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Map getProcurementDocument(String id) {
        String sql="select kbsj from xm_cgwj where id = ?";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(sql, id);
        return stringObjectMap;
    }
}
