package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.EpExtractApplyDao;
import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家抽取申请
 *
 * @author 宋建华
 * @date 2018-11-14 14:22
 **/
@Component
public class EpExtractApplyDaoImpl extends BaseDaoImpl implements EpExtractApplyDao {

    @Override
    public Map<String, Object> selectZjsqList(String bxh) {

        String sql = "SELECT * FROM zb_zjsq WHERE id IN (SELECT ZJSQBH FROM zb_zjsq_fb WHERE bxh = ? and lx = (select lx from xm_xmfb where id = ?))";
        List<Map<String, Object>>  zjsqList = jdbcTemplate.queryForList(sql, bxh, bxh);
        return CollUtil.isEmpty(zjsqList)?new HashMap<>():zjsqList.get(0);
    }

    @Override
    public List<Map<String, Object>> selectCqtj(Object zjsqbh) {
        String sql = "SELECT * FROM zb_cqtj WHERE zjsqbh = ?";
        return jdbcTemplate.queryForList(sql,zjsqbh);
    }

    @Override
    public List<String> selectRegionName(List<String> list) {
        String sql = "SELECT dmmc FROM gg_dmnr WHERE dmbz in(:list) ";
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        return namedParameterJdbcTemplate.queryForList(sql,map,String.class);
    }

    @Override
    public List<String> selectPbzyName(List<String> list) {
        String sql = "SELECT zymc FROM jc_pbzy WHERE id in(:list) ";
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        return namedParameterJdbcTemplate.queryForList(sql,map,String.class);
    }
}
