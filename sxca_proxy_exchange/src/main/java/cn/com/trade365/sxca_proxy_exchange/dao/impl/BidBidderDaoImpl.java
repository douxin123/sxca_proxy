package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.BidBidderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-12 11:34
 **/
@Component
public class BidBidderDaoImpl extends BaseDaoImpl implements BidBidderDao {
    private static final Logger log = LoggerFactory.getLogger(BidBidderDaoImpl.class);

    @Override
    public List<Map<String, Object>> selectBmgysList(String bxh) {
        String sql = "select * from xm_bmgys where bxh = ?";
        return jdbcTemplate.queryForList(sql, bxh);
    }

    @Override
    public Date getBidDocDownloadTime(String sectionId, String orgId) {
        String sql = "select DATE_FORMAT(xzsj,'%Y-%m-%d %H:%i:%s') from xm_wjxzjl where BXH = ? and GYSJGBH = ? order by xzsj asc limit 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{sectionId, orgId}, Date.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Integer getPassedApplicant(String sectionId, String orgId) {
        String sql = "select SCJG from xm_zsjg where BXH = ? and GYSJGBH = ? limit 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{sectionId, orgId}, Integer.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getZbgys(String bxh, String orgId) {
        String sql = "select * from xm_zbgys where BXH = ? and GYSJGBH = ?  limit 1";
        try {
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql,  new Object[]{bxh,orgId});
            if(resultList!=null&&resultList.size()>0){
                return resultList.get(0);
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
