package cn.com.trade365.sxca_proxy_exchange.dao;

import java.util.List;
import java.util.Map;

/**
 * 专家抽取申请
 */
public interface EpExtractApplyDao extends BaseDao {

    /**
     * 获取zb_zjsq表信息
     *
     * @param bxh xm_xmfb.id
     * @return
     */
    Map<String, Object> selectZjsqList(String bxh);

    /**
     * 获取zb_cqtj表信息
     * @param zjsqbh
     * @return
     */
    List<Map<String, Object>> selectCqtj(Object zjsqbh);

    /**
     * 查询区域名称
     * @param list 区域代码集合
     * @return
     */
    List<String> selectRegionName(List<String> list);

    /**
     * 查询评标专业名称
     * @param list jc_pbzy.id 集合
     * @return
     */
    List<String> selectPbzyName(List<String> list);
}
