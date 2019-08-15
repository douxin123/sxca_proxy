package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.TenderProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 招标项目持久层实现
 * @author zhanghongbing
 *
 */
@Component
public class TenderProjectDaoImpl extends BaseDaoImpl implements TenderProjectDao {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getSupervisePlatformsByXmid(String xmid){
        String sql = " select concat_ws(':',name,code) from gg_supervise where id in (select gg_supervise_id from gg_supervise_zbxm where xm_zbxm_id = ?) and status=1 ";
        List<String> supervisePlatforms = jdbcTemplate.queryForList(sql,String.class,xmid);
        return supervisePlatforms;
    }
}
