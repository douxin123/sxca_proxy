package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.platform.project.dto.EbDissentDto;
import cn.com.trade365.sxca_proxy_exchange.service.EbDissentService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.com.trade365.store.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 投诉质疑
 */
@Service("ebDissentService")
public class EbDissentServiceImpl implements EbDissentService {
    @Autowired
    private Client fileClient;
    @Autowired
    private NamedParameterJdbcTemplate ebDissentDao;

    /**
     * 组装质疑对象
     * @param zzid 质疑ID
     * @return EbDissent
     */
    @Override
    public EbDissentDto getEbDissent(String zzid){
        EbDissentDto eb=new EbDissentDto();
        try {
            Map<String,Object> param=new HashMap<>();
            String sql="SELECT GYSMC,GYSBH,XMXH,ZYTSJD,YJLY,TJSJ,SLR,SLSJ,FQNR,HFSJ,(SELECT ztdm from gg_jgbh where JGBH=GYSBH) ztdm  FROM XM_ZYYY where SXLB=1 and id=:id";
            param.put("id",zzid);
            Map<String,Object> data=this.ebDissentDao.queryForMap(sql,param);
            if(data!=null){
                //组装对象
                eb.setTenderProjectId(0L);
                eb.setSection(0L);
                eb.setDissentPersonName(ConvertUtils.convert2str(data.get("GYSMC")));
                eb.setDissentPersionCode(ConvertUtils.convert2str(data.get("ztdm")));
                //1.	对招标文件异议2.对资格预审文件
                //3.对资格预审结果的异议4.对评标结果的异议9其他
                sql="select lx from xm_zbxm where id=:xmid";
                param.put("xmid",ConvertUtils.convert2str(data.get("XMXH")));
                int lx=this.ebDissentDao.queryForObject(sql,param,Integer.class);
                if(lx==1) {
                    eb.setDissentType(1);
                }
                if(lx==2){
                    eb.setDissentType(2);
                }
                eb.setDissentReason(ConvertUtils.convert2str(data.get("YJLY")));
                eb.setDissentContent(ConvertUtils.convert2str(data.get("YJLY")));
                eb.setSubmitTime(ConvertUtils.convert2Date(data.get("TJSJ")));
                eb.setAcceptPersion(ConvertUtils.convert2str(data.get("SLR")));
                eb.setAcceptTime(ConvertUtils.convert2Date(data.get("SLSJ")));
                eb.setHandleResult(ConvertUtils.convert2str(data.get("FQNR")));
                eb.setFeedbackTime(ConvertUtils.convert2Date(data.get("HFSJ")));
            }
        }catch (Exception e){
           log.info("JdbcTemplate数据查询异常",e);
           return  null;
        }
        return eb;
    }


    /**
     * 获取项目ID
     * @param zyid 质疑
     * @return String
     */
    @Override
    public  String getXMID(String zyid){
        try {
            Map<String,Object> param=new HashMap<>();
            String sql="select XMXH from xm_zyyy where id=:id";
            param.put("id",zyid);
            Map<String,Object> data=this.ebDissentDao.queryForMap(sql,param);
            if(data!=null){
                return  ConvertUtils.convert2str(data.get("XMXH"));
            }
        }catch (Exception e){

        }
        return  null;
    }

}
