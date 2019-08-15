package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.PrjExceptionDao;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 招标异常情况信息持久层接口
 *
 * @author zhangjianbing
 */
@Component
public class PrjExceptionDaoImpl extends BaseDaoImpl implements PrjExceptionDao {

    @Override
    /**
     * @Description: 获取招标项目异常信息
     * @Param: sectionId
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    public Map<String, Object> queryPrjExcInfo(String sectionId) {
        String sql = "  SELECT ycfb.ZBXH,ycfb.BXH,xm.XMMC,fb.`BYWBH` ,yc.`YCMS`,yc.ZT,yc.`SHSJ`,yc.FJ FROM xm_ycfb ycfb" +
                "   LEFT JOIN xm_yc  yc   ON yc.ID = ycfb.YCXH" +
                "   LEFT JOIN xm_zbxm xm  ON xm.id = ycfb.ZBXH" +
                "   LEFT JOIN XM_XMFB fb   ON fb.id = ycfb.BXH" +
                "    WHERE ycfb.BXH = ?";
        return jdbcTemplate.queryForMap(sql, sectionId);
    }
}
