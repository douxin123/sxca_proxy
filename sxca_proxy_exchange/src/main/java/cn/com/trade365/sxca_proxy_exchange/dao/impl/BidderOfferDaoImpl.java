package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.store.Client;
import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.dao.BidderOfferDao;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 投标人投标数据持久层实现
 *
 * @author zhangjianbing
 */
@Component
public class BidderOfferDaoImpl extends BaseDaoImpl implements BidderOfferDao {
    @Autowired
    private Client fileClient;

    @Autowired
    private BeanConfig beanConfig;

    /**
     * @Description: 获取投标文件ID
     * @Param: applyId 供应商ID  mlbh 目录编号
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public Long queryFileId(String applyId, int wdlx) {
        String sql = "SELECT fd.path,fd.realname FROM  xm_tbwd  wd" +
                "  LEFT JOIN GG_FJGHWD fd  ON fd.id = wd.WD" +
                "     WHERE wd.BMGYSBH = ? AND wd.LX = ? AND wd.WDLX = 0";
        // 根据 MLBH 目录编号区分 文件类别
        List<Map<String, Object>> attachment = jdbcTemplate.queryForList(sql, new Object[]{applyId, wdlx});
        if (CollectionUtil.isEmpty(attachment) || attachment.get(0).get("path") == null || attachment.get(0).get("realname") == null) {
            return 0L;
        }
        return fileClient.store(
                new File(beanConfig.getFilePath() + attachment.get(0).get("path") + attachment.get(0).get("realname")), null);
    }

    /**
     * @Description: 获取投标人投标信息集合
     * @Param: sectionId 包序号
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public List<Map<String, Object>> queryBidderOfferList(String sectionId) {
        // 判断 xmfb.TBBJBZ  0是投标总价
        String sql = "SELECT bm.ID,bm.ZBXH,bm.BXH,bm.GYSMC,bm.TBSJ,bm.sjbzj,zb.TBJG,zb.BZ,zb.TBBJBZ,zb.QTTBJG,zb.QTZBJG,zb.ZBJG,zb.SM,zb.HBDW,zb.PM,tbnr.TBNRXH,zb.PBJG,zb.PFJG,zb.QTPBJG,tbnr.id gystbnrxh  FROM XM_BMGYS  bm " +
                "  LEFT  JOIN XM_ZBGYS zb ON zb.BXH = bm.BXH AND zb.gysjgbh = bm.gysjgbh AND zb.LX = bm.LX " +
                " LEFT JOIN ZB_GYSTBNR tbnr ON tbnr.BMGYSXH = bm.ID" +
                " WHERE  bm.bxh = ?";
        return jdbcTemplate.queryForList(sql, sectionId);
    }


    /**
     * @Description: 获取字段值
     * @Param: tbnrxh 投标内容序号 fieldName 字段名称
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public List<Map<String, Object>> queryBidFieldData(String tbnrxh,String gystbnrxh, String fieldName,String filedName2) {
        String sql = "SELECT tbsj.ZDZ,tbbg.ZDMC,tbbg.SJLX FROM  zb_tbsj  tbsj " +
                " LEFT JOIN ZB_TBBG tbbg ON  tbsj.TBBGXH = tbbg.ID " +
                " WHERE tbbg.TBNRXH = ? AND tbsj.GYSTBNRXH = ? AND  (tbbg.ZDMC LIKE ? or tbbg.ZDMC LIKE ?) AND tbbg.SFSY =1";
        return jdbcTemplate.queryForList(sql, new Object[]{tbnrxh,gystbnrxh, fieldName,filedName2});
    }

    /**
     * @Description: 获取评标结果信息
     * @Param: sectionId 包序号，bmgysId 报名供应商id
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    @Override
    public Map<String, Object> getEvalInfo(String sectionId, String bmgysId) {
        String sql = "SELECT PFJG,PBJG,HBDW FROM `xm_psjg` " +
                "WHERE BXH = ? AND BMGYSXH = ?";
        List<Map<String, Object>> evalInfos = jdbcTemplate.queryForList(sql, new Object[]{sectionId, bmgysId});
        if (evalInfos == null || evalInfos.size() == 0) {
            return null;
        }
        return evalInfos.get(0);
    }
}
