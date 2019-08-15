package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.ProjectSubpackageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author :lhl
 * @create :2018-11-23 12:22
 */
@Component
public class ProjectSubpackageDaoImpl implements ProjectSubpackageDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *  根据文件分包id查询项目分包的开标地点
     * @param id 文件分包id
     * @return
     */
    @Override
    public String getBidOpenSpace(String id) {
        String sql="select KBDD from xm_xmfb where bbh=?";
        String bidOpenSpace = jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
        return bidOpenSpace;
    }
}
