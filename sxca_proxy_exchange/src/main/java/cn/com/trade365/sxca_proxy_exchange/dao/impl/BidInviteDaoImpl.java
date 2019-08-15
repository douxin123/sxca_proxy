package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.BidInviteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-12 13:22
 **/
@Component
public class BidInviteDaoImpl extends BaseDaoImpl implements BidInviteDao {
    private static final Logger log = LoggerFactory.getLogger(BidInviteDaoImpl.class);

    @Override
    public Map<String, Object> getCgwjMap(String tbyqxxId) {
        String sql = "select xm_cgwj.* from xm_cgwj,xm_wjfb,xm_tbyqxx where xm_cgwj.id = xm_wjfb.wjxh and xm_wjfb.bxh = xm_tbyqxx.bxh and xm_tbyqxx.id = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, tbyqxxId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    @Override
    public String getFilePath(Object fileId) {
        String sql = "SELECT CONCAT(path,REALNAME) as filePath FROM gg_fjghwd WHERE ID = (SELECT YQHNR FROM xm_tbyqxx WHERE id = ? )";
        return jdbcTemplate.queryForMap(sql, fileId).get("filePath").toString();
    }
}
