package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;
import java.util.Map;

/**
 * 投标人投标数据持久层接口
 *
 * @author zhangjianbing
 */
public interface BidderOfferDao extends BaseDao {
    /**
     * @Description: 获取投标文件ID
     * @Param: applyId 报名供应商ID mlbh 目录编号
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    Long queryFileId(String applyId, int wdlx);

    /**
     * @Description: 获取投标人投标信息集合
     * @Param: sectionId 包序号
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    List<Map<String, Object>> queryBidderOfferList(String sectionId);

    /**
     * @Description: 获取字段值
     * @Param: tbnrxh 投标内容序号 fieldName 字段名称
     * @return:
     * @Author: JianbingZhang
     * @Date:
     */
    List<Map<String, Object>> queryBidFieldData(String tbnrxh, String gystbnrxh, String fieldName, String filedName2);

    /**
      * @Description: 获取评标结果信息
      * @Param:  sectionId 包序号，bmgysId 报名供应商id
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    Map<String, Object> getEvalInfo(String sectionId, String bmgysId);
}

