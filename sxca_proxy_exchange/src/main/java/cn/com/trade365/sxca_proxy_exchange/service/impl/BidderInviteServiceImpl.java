package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.BidInviteDao;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidBidderInviteDto;
import cn.com.trade365.sxca_proxy_exchange.service.BidderInviteService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-22 09:52
 **/
@Service("bidderInviteService")
public class BidderInviteServiceImpl implements BidderInviteService {

    @Autowired
    BidInviteDao bidInviteDao;

    @Autowired
    private IdRelationDao idRelationDao;

    @Override
    public BidBidderInviteDto getBidderInviteData(MsgEvent msgEvent)  throws ExchangeException {
        BidBidderInviteDto bidderInvite = new BidBidderInviteDto();
        Map<String, String> dataMap = msgEvent.getData();
        //tradeId = xm_tbyqxx.id
        String tradeId = dataMap.get("tradeId");
        Map<String, Object> tbyqxxMap = bidInviteDao.selectById("xm_tbyqxx", tradeId);
        RelationEntity projectId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_PROJECT.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("zbxh")));
        RelationEntity sectionId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.SECTION.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("bxh")));
        RelationEntity inviteId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.BIDINVITE.getCode(), tradeId);
        RelationEntity bidderId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.BIDBIDDER.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("bmgysbh")));

        bidderInvite.setInviteId(inviteId.getDataId());
        bidderInvite.setBidderId(bidderId.getDataId());
        bidderInvite.setTenderProjectId(projectId.getDataId());
        bidderInvite.setSectionId(sectionId.getDataId());
        bidderInvite.setInviteStatus(getInviteStatus(tbyqxxMap.get("sfjs")));
        bidderInvite.setSourceId(ConvertUtils.convert2str(tradeId));
        bidderInvite.setPlatformCode(msgEvent.getPlatformCode());

        bidderInvite.setCreateTime(new Date());
        bidderInvite.setUpdateTime(new Date());

        return bidderInvite;
    }

    /**
     * 映射邀请状态
     * @param sfjs
     * @return
     */
    private Integer getInviteStatus(Object sfjs) {
        if(sfjs == null) {
            return 0;//初始化
        }
        int status = Convert.toInt(sfjs);
        switch (status) {
            case 0 : return 2; //拒绝
            case 1 : return 1; //接受
            case 2 : return 3; //已撤销
            default: return 9;
        }

    }
}
