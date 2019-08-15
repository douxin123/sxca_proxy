package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.EpExtractApplyDao;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EpExtractApplyDto;
import cn.com.trade365.sxca_proxy_exchange.service.EpExtractApplyService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家抽取申请 服务实现类
 *
 * @author 宋建华
 * @date 2018-11-14 11:25
 **/
@Service("epExtractApplyService")
public class EpExtractApplyServiceImpl implements EpExtractApplyService {

    @Autowired
    EpExtractApplyDao epExtractApplyDao;
    @Autowired
    private IdRelationDao idRelationDao;

    @Override
    public Map<String, EpExtractApplyDto> getEpExtractApplyData(MsgEvent msgEvent) throws ExchangeException{
        Map<String, EpExtractApplyDto> map = new HashMap<>();
        Map<String, String> dataMap = msgEvent.getData();
        //tradeId = xm_xmfb.id
        Map<String, Object> xm_xmfbMap = epExtractApplyDao.selectById("xm_xmfb", dataMap.get("tradeId"));
        Map<String, Object> xm_zbxmMap = epExtractApplyDao.selectById("xm_zbxm", Convert.toStr(xm_xmfbMap.get("zbxh")));

        Map<String, Object> zjsqMap = epExtractApplyDao.selectZjsqList(dataMap.get("tradeId"));
        RelationEntity projectId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_PROJECT.getCode(), Convert.toStr(xm_xmfbMap.get("zbxh")));
        RelationEntity sectionId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.SECTION.getCode(), Convert.toStr(dataMap.get("tradeId")));
        EpExtractApplyDto epExtractApply = new EpExtractApplyDto();
        epExtractApply.setTenderProjectId(projectId.getDataId());
        epExtractApply.setSectionId(sectionId.getDataId());
        epExtractApply.setBidType(Convert.toInt(xm_xmfbMap.get("lx")));
        epExtractApply.setExtractType(getExtractType(Convert.toInt(xm_zbxmMap.get("lx"))));//申请分类
        epExtractApply.setEvaluationStartTime(ConvertUtils.convert2Date(zjsqMap.get("pbkssj")));//评标开始时间(预估)
        epExtractApply.setEvaluationEndTime(ConvertUtils.convert2Date(zjsqMap.get("pbjssj")));//评标结束时间（预估）
        epExtractApply.setExpertTotalNum(Convert.toInt(zjsqMap.get("zjrs")));//专家总数
        if(zjsqMap.get("kzsz2")==null){
            epExtractApply.setExpertOwnerNum(0);
        }else{
            epExtractApply.setExpertOwnerNum(Convert.toInt(zjsqMap.get("kzsz2")));//业主专家人数
        }
        epExtractApply.setExtractOptions(getExtractOptions(zjsqMap.get("id")));//专家抽取条件
        epExtractApply.setPlatformCode(msgEvent.getPlatformCode());
        map.put(Convert.toStr(dataMap.get("tradeId"))+"_"+Convert.toStr(xm_xmfbMap.get("lx")), epExtractApply);

        return map;
    }

    /**
     * 获取申请分类
     *
     * @param lx xm_zbxm.lx
     * @return
     */
    private Integer getExtractType(Integer lx) {
        if (lx == null) {
            return null;
        }
        switch (lx) {
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 9;
        }
    }

    /**
     * 获取专家抽取条件
     *
     * @param zjsqId zb_zjsq.id
     * @return
     */
    private String getExtractOptions(Object zjsqId) {
        List<Map<String, Object>> cqtjList = epExtractApplyDao.selectCqtj(zjsqId);
        StringBuilder extractOptions = new StringBuilder();
        for (Map<String, Object> cqtjMap : cqtjList) {
            if (cqtjMap.get("zjfl") != null) {
                switch (Convert.toInt(cqtjMap.get("zjfl"))) {
                    case 0:
                        extractOptions.append("专家分类：商务类；");
                        break;
                    case 1:
                        extractOptions.append("专家分类：技术类；");
                        break;
                    case 3:
                        extractOptions.append("专家分类：法律类；");
                        break;
                    default:
                }
            }
            if (cqtjMap.get("xcsl") != null) {
                extractOptions.append("需抽数量：").append(cqtjMap.get("xcsl")).append("；");
            }
            if (cqtjMap.get("cqbs") != null) {
                extractOptions.append("抽取倍数：").append(cqtjMap.get("cqbs")).append("倍；");
            }
            if (cqtjMap.get("ssdq") != null) {
                List<String> list = Arrays.asList(Convert.toStr(cqtjMap.get("ssdq")).split(","));
                List<String> regionNameList = epExtractApplyDao.selectRegionName(list);
                if(regionNameList!=null&&regionNameList.size()>0){
                    extractOptions.append("所属地区：");
                    for (String regionName : regionNameList) {
                        extractOptions.append(regionName).append(",");
                    }
                    extractOptions.deleteCharAt(extractOptions.lastIndexOf(",")).append("；");
                }
            }
            if (cqtjMap.get("pbzy") != null) {
                List<String> list = Arrays.asList(Convert.toStr(cqtjMap.get("pbzy")).split(","));
                List<String> pbzyNameList = epExtractApplyDao.selectPbzyName(list);
                if(pbzyNameList!=null&&pbzyNameList.size()>0) {
                    extractOptions.append("评标专业：");
                    for (String pbzyName : pbzyNameList) {
                        extractOptions.append(pbzyName).append(",");
                    }
                    extractOptions.deleteCharAt(extractOptions.lastIndexOf(",")).append("；");
                }
            }
            extractOptions.append("\r\n");
        }
        return extractOptions.toString();
    }


}
