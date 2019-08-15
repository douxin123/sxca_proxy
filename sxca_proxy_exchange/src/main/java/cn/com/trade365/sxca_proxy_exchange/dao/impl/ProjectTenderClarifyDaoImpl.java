package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.ProjectTenderClarifyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author :lhl
 * @create :2018-11-14 15:46
 */
@Component
public class ProjectTenderClarifyDaoImpl implements ProjectTenderClarifyDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *  获取变更澄清链接地址
     * @param id 澄清变更id
     * @return xm_fjghwd 的 ywxh
     */
    @Override
    public String getAttachmentId(String id) {
        String sql = "select ljdz from xm_bgcq  where id=?";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(sql, id);
        return stringObjectMap == null ? null : stringObjectMap.get("ljdz").toString();
    }

    /**
     *
     * @param id 变更澄清id
     * @return
     */
    @Override
    public List getProjectTenderClarifyMap(String id) {
        String sql = "SELECT\n" +
                "\ta.SFXGSJ isPostpone,\n" +
                "\ta.qcsj clarifyTime,\n" +
                "\ta.CQWJSM clarifyContent,\n" +
                "\tc.kbsj preBidOpenTime,\n" +
                "\ta.XKBSJ bidOpenTime,\n" +
                "\tf.id tenderFileId ,\n" +
                "\tfb.KBDD preOpenAddress,\n" +
                "\ta.XKBDD openAddress\n" +
                "FROM\n" +
                "\txm_bgcq a\n" +
                "\tLEFT JOIN xm_bgcq_wj b ON a.id = b.bgcqbh\n" +
                "\tJOIN xm_wjfb f ON f.wjxh = b.CGWJBH \n" +
                "\tJOIN xm_xmfb fb on fb.id=f.BXH\n" +
                "\tjoin xm_cgwj c on c.id=b.CGWJBH \n" +
                "WHERE\n" +
                "\ta.id  =?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[]{id});
        return list;
    }
}
