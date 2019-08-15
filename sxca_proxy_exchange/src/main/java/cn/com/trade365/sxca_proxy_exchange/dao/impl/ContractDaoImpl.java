package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.ContractDao;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 合同信息持久层实现
 *
 * @author zhangjianbing
 */
@Component
public class ContractDaoImpl extends BaseDaoImpl implements ContractDao {

    /**
     * @Description: 查询招标代理机构信息
     * @Param: projectId
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public Map<String, Object> queryZbJg(String projectId) {
        String sql = " SELECT jg.JGMC,xm.DLJGBH,jg.ZTDM,jg.DATA_ID  FROM xm_zbxm xm " +
                " LEFT JOIN gg_jgbh jg ON jg.JGBH = xm.DLJGBH " +
                " WHERE id = ? ";
        return jdbcTemplate.queryForMap(sql, projectId);
    }

    @Override
    public Map<String,Object> queryById(String cotractId){
        String sql = " SELECT * FROM bid_contract  " +
                " WHERE id = ? ";
        return jdbcTemplate.queryForMap(sql, cotractId);
    }
}
