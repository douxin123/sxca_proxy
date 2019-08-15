package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.EpAvoidUnitDao;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.EpAvoidUnitDto;
import cn.com.trade365.sxca_proxy_exchange.service.EpAvoidUnitService;
import cn.hutool.core.convert.Convert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回避单位 服务实现类
 *
 * @author 宋建华
 * @date 2018-11-14 10:28
 **/
@Service("epAvoidUnitService")
public class EpAvoidUnitServiceImpl implements EpAvoidUnitService {

    @Autowired
    EpAvoidUnitDao epAvoidUnitDao;
    @Autowired
    private IdRelationDao idRelationDao;

    @Override
    public Map<String, EpAvoidUnitDto> getEpAvoidUnit(MsgEvent msgEvent) throws ExchangeException{
        Map<String, EpAvoidUnitDto> map = new HashMap<>();
        Map<String, String> dataMap = msgEvent.getData();
        //tradeId = xm_xmfb.id
        Map<String, Object> xm_xmfbMap = epAvoidUnitDao.selectById("xm_xmfb", dataMap.get("tradeId"));
        Map<String, Object> xm_zbxmMap = epAvoidUnitDao.selectById("xm_zbxm", Convert.toStr(xm_xmfbMap.get("ZBXH")));

        List<Map<String, Object>> hbdwList = epAvoidUnitDao.selectHbdwList(dataMap.get("tradeId"));
        RelationEntity projectId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_PROJECT.getCode(), Convert.toStr(xm_xmfbMap.get("zbxh")));
        RelationEntity sectionId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.SECTION.getCode(), Convert.toStr(dataMap.get("tradeId")));

        for (Map<String, Object> hbdwMap : hbdwList) {
            EpAvoidUnitDto epAvoidUnit = new EpAvoidUnitDto();
            epAvoidUnit.setTenderProjectId(projectId.getDataId());
            epAvoidUnit.setSectionId(sectionId.getDataId());
            epAvoidUnit.setExtractType(getExtractType(Convert.toInt(xm_zbxmMap.get("lx"))));//申请分类
            epAvoidUnit.setAvoidUnitName(Convert.toStr(hbdwMap.get("dwmc")));//回避单位名称
            epAvoidUnit.setAvoidUnitOrgCode(StringUtils.isBlank(Convert.toStr(hbdwMap.get("ztdm")))?"无":Convert.toStr(hbdwMap.get("ztdm")));//回避单位组织机构代码
            epAvoidUnit.setPlatformCode(msgEvent.getPlatformCode());
            map.put(Convert.toStr(hbdwMap.get("id")), epAvoidUnit);
        }
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

}
