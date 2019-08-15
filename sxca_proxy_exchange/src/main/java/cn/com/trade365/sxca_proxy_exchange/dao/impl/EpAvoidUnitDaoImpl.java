package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.EpAvoidUnitDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 回避单位
 *
 * @author 宋建华
 * @date 2018-11-14 14:18
 **/
@Component
public class EpAvoidUnitDaoImpl extends BaseDaoImpl implements EpAvoidUnitDao {

    @Override
    public List<Map<String, Object>> selectHbdwList(String bxh) {
        String sql = "select zb_hbdw.*,gg_jgbh.ZTDM from zb_zjsq_fb,zb_hbdw,gg_jgbh where zb_zjsq_fb.ZJSQBH = zb_hbdw.ZJSQBH and zb_hbdw.DWBH = gg_jgbh.JGBH and zb_zjsq_fb.bxh = ?";
        return jdbcTemplate.queryForList(sql, bxh);
    }
}
