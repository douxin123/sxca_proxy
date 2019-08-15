package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.BidderOfferDao;
import cn.com.trade365.sxca_proxy_exchange.dao.OpenBidDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidOpenbidDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.OpenbidService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: JianBingZhang
 * @Date: 2018/11/8 15:49
 * @Version 1.0
 */
@Service("openbidService")
public class OpenbidServiceImpl implements OpenbidService {

    @Autowired
    private OpenBidDao openbidDao;

    @Autowired
    private IdRelationService idRelationService;

    @Autowired
    private BidderOfferDao bidderOfferDao;

    @Override
    /**
     * @Description: 封装openbid参数
     * @Param: [dataMap]
     * @return: cn.com.trade365.platform.entity.BidOpenbid
     * @Author: JianbingZhang
     * @Date:
     */
    public BidOpenbidDto getOpenBidData(MsgEvent msgEvent) throws ExchangeException {
        //根据id 获取分包信息
        String sectionId = msgEvent.getData().get("tradeId");
        List<Map<String, Object>> infos = openbidDao.queryOpenBidRecord(sectionId);
        Map<String, Object> infoMap = new HashMap<>();
        if (infos != null && infos.size() > 0) {
            infoMap = infos.get(0);
        }
        BidOpenbidDto openbid = new BidOpenbidDto();
        //开标参与人   gg_jgbh.JGMC + xm_bmgys.LXR    多个参与人 需要遍历 数据记录
        StringBuilder actorString = new StringBuilder();
        for (int i = 0; i < infos.size(); i++) {
            if (i > 0) {
                actorString.append(",");
            }
            actorString.append(ConvertUtils.convert2str(infos.get(i).get("JGMC")) + ConvertUtils.convert2str(infos.get(i).get("LXR")));
        }
        openbid.setBidOpeningActorId(ConvertUtils.convert2str(actorString));
        //开标地点
        openbid.setBidOpeningAddress(ConvertUtils.convert2str(infoMap.get("KBDD")));
        //开标记录编号
        openbid.setRecordCode(ConvertUtils.convert2str(infoMap.get("BYWBH")));
        //开标记录名称
        openbid.setRecordName(ConvertUtils.convert2str(infoMap.get("XMMC")) + ConvertUtils.convert2str(infoMap.get("BMC")) + "开标记录");
        //开标时间
        openbid.setBidOpeningTime(ConvertUtils.convert2Date(((BigDecimal) infoMap.get("KBSJ")).longValue()));
        //项目id
        openbid.setProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, infoMap.get("zbxmId").toString()));
        //相关包编号
        openbid.setSectionId(idRelationService.getDataId(ObjectTypeEnum.SECTION, infoMap.get("xmfbId").toString()));
        //插入时间
        openbid.setCreateTime(DateUtil.date());
        //更新时间
        openbid.setUpdateTime(DateUtil.date());
        //版本号
        openbid.setPlatformCode(msgEvent.getPlatformCode());
        //类型
        openbid.setType(ConvertUtils.convert2Int(infoMap.get("lx")));
        openbid.setSourceId(sectionId);
        // 开标记录内容 待校准
        String openContent = "";
        List<Map<String, Object>> offers = bidderOfferDao.queryBidderOfferList(sectionId);
        if (offers != null && offers.size() > 0) {
            for (Map<String, Object> offer : offers) {
                String gycMc = ConvertUtils.convert2str(offer.get("GYSMC"));
                String bidPrice = "暂无";
                List<Map<String, Object>> prices = bidderOfferDao.queryBidFieldData(ConvertUtils.convert2str(offer.get("TBNRXH")), ConvertUtils.convert2str(offer.get("gystbnrxh")),"%投标总价%","%投标报价%");
                if (prices != null && prices.size() > 0) {
                    bidPrice = ConvertUtils.convert2str(prices.get(0).get("ZDZ"));
                }
                String jhq = "暂无";
                List<Map<String, Object>> durs = bidderOfferDao.queryBidFieldData(ConvertUtils.convert2str(offer.get("TBNRXH")), ConvertUtils.convert2str(offer.get("gystbnrxh")),"%交货期%","%工期%");
                if (durs != null && durs.size() > 0) {
                    jhq = ConvertUtils.convert2str(durs.get(0).get("ZDZ"));
                }
                String bzj = "暂无";
                if (offer.get("sjbzj") != null) {
                    bzj = ConvertUtils.convert2str(offer.get("sjbzj"));
                }
                String tbsj = "暂无";
                if (offer.get("TBSJ") != null) {
                    tbsj = ConvertUtils.convert2str(offer.get("TBSJ"));
                }
                openContent += "投标人：" + gycMc + "，报价：" + bidPrice + "万元，工期：" + jhq
                        + "，投标保证金额：" + bzj + "元，投标文件上传时间：" + tbsj + "  ";
            }
        }
        openbid.setBidOpeningContent(openContent.toString());
        //TODO 附件关联标识号
        openbid.setAttachmentCode(0L);
        return openbid;
    }
}
