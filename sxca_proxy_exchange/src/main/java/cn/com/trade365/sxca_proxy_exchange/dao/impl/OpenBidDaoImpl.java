package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.OpenBidDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 开标记录持久层实现
 *
 * @author zhangjianbing
 */
@Component
public class OpenBidDaoImpl extends BaseDaoImpl implements OpenBidDao {

    /**
     * @Description: 获取开标记录信息
     * @Param: sectionId
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public List<Map<String, Object>> queryOpenBidRecord(String sectionId) {
        String sql = "select fb.id as xmfbId,xm.id as zbxmId,gys.id as gysId,fb.KBDD,fb.KBSJ,fb.BMC,xm.XMMC,fb.BYWBH,gys.LXR,jg.JGMC,fb.lx from xm_xmfb fb " +
                " left join xm_zbxm xm on xm.id = fb.ZBXH" +
                " left join xm_bmgys gys on gys.BXH = fb.id" +
                " left join gg_jgbh jg on jg.JGBH  = gys.GYSJGBH" +
                "   where fb.id=? AND fb.lx = gys.lx ";
        return jdbcTemplate.queryForList(sql, sectionId);
    }
}
