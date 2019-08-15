package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-09 15:09
 **/
public interface BidBidderDao extends BaseDao {
    /**
     * 获取报名供应商集合
     * @param bxh xm_xmfb.id
     * @return
     */
    List<Map<String, Object>> selectBmgysList(String bxh);

    /**
     * 获取第一次招标文件下载时间
     * @param sectionId
     * @param orgId
     * @return
     */
    Date getBidDocDownloadTime(String sectionId, String orgId);

    /**
     * 获取资审项目供应商是否通过资审
     * @param sectionId
     * @param orgId
     * @return
     */
    Integer getPassedApplicant(String sectionId, String orgId);

    /**
      * @Description:  获取中标供应商列表
      * @Param:  bxh bmId
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    Map<String, Object> getZbgys(String bxh, String orgId);

}
