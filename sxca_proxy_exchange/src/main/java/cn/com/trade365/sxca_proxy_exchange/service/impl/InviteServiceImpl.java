package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.BidInviteDao;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidInviteDto;
import cn.com.trade365.sxca_proxy_exchange.service.InviteService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.com.trade365.store.Client;
import cn.hutool.core.convert.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-09 10:33
 **/
@Service("inviteService")
public class InviteServiceImpl implements InviteService {
    private static final Logger log = LoggerFactory.getLogger(InviteServiceImpl.class);

    @Autowired
    BidInviteDao bidInviteDao;
    @Autowired
    private IdRelationDao idRelationDao;

    @Autowired
    Client fileClient;

    @Override
    public BidInviteDto getBidInviteData(MsgEvent msgEvent) throws ExchangeException{
        BidInviteDto bidInvite = new BidInviteDto();
        Map<String, String> dataMap = msgEvent.getData();
        //tradeId = xm_tbyqxx.id
        String tradeId = dataMap.get("tradeId");

        Map<String, Object> tbyqxxMap = bidInviteDao.selectById("xm_tbyqxx", tradeId);
        Map<String, Object> xm_zbxmMap = bidInviteDao.selectById("xm_zbxm", ConvertUtils.convert2str(tbyqxxMap.get("ZBXH")));
        RelationEntity projectId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_PROJECT.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("zbxh")));
        RelationEntity sectionId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.SECTION.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("bxh")));
        bidInvite.setTenderProjectId(projectId.getDataId());
        bidInvite.setSectionId(sectionId.getDataId());

        bidInvite.setInviteIssueTime(ConvertUtils.convert2Date(tbyqxxMap.get("fcsj")));//投标邀请发出时间
        bidInvite.setInviteEndTime(ConvertUtils.convert2Date(tbyqxxMap.get("jzsj")));//投标邀请回复截止时间
        try {
            String filePath = bidInviteDao.getFilePath(tbyqxxMap.get("yqhnr"));
            Long attachmentId = fileClient.store(new File(filePath), "tag 可有可无");
            bidInvite.setAttachmentCode(attachmentId);// 附件id
        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("邀请书附件获取失败。", e);
            bidInvite.setAttachmentCode(1000000L);//TODO 附件id默认值
        }
        bidInvite.setInviteReason(ConvertUtils.convert2str(tbyqxxMap.get("yqsmnr")));//邀请内容

        RelationEntity tenderId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.BIDBIDDER.getCode(), ConvertUtils.convert2str(tbyqxxMap.get("bmgysbh")));
        if (tenderId != null) {
            bidInvite.setTenderId(ConvertUtils.convert2str(tenderId.getDataId()));//投标邀请人id
        }
        Map<String, Object> cgwjMap = bidInviteDao.getCgwjMap(tradeId);
        bidInvite.setTenderDocgetStartTime(ConvertUtils.convert2Date(cgwjMap.get("qssj")));//招标文件获取开始时间
        bidInvite.setTenderDocDeadLine(ConvertUtils.convert2Date(cgwjMap.get("jssj")));//招标文件获取截止时间
        bidInvite.setBidDocReferEndTime(ConvertUtils.convert2Date(cgwjMap.get("kbsj")));//投标文件递交截止时间
        bidInvite.setTenderDocGetMethod(cgwjMap.get("hqfs") == null ? "在线下载" : ConvertUtils.convert2str(cgwjMap.get("hqfs")));//招标文件获取方法
        bidInvite.setBidDocReferMethod(getBidDocReferMethod(Convert.toInt(cgwjMap.get("lybz"))));//投标文件递交方法
        Map<String, Object> czyMap = bidInviteDao.selectById("gg_czyb", ConvertUtils.convert2str(xm_zbxmMap.get("xmfzr")));
        bidInvite.setCreaterId(ConvertUtils.convert2Long(czyMap.get("data_id")));// 创建人id
        bidInvite.setCreateName(ConvertUtils.convert2str(xm_zbxmMap.get("fzrmc")));
        bidInvite.setCreateTime(ConvertUtils.convert2Date(tbyqxxMap.get("fcsj")));
        bidInvite.setUpdateTime(ConvertUtils.convert2Date(tbyqxxMap.get("fcsj")));
        bidInvite.setSourceId(ConvertUtils.convert2str(tradeId));
        bidInvite.setPlatformCode(msgEvent.getPlatformCode());
        return bidInvite;
    }

    /**
     * 获取 投标文件递交方法
     *
     * @param lybz xm_cgwj.lybz
     * @return
     */
    private String getBidDocReferMethod(Integer lybz) {
        if (lybz == null) {
            return null;
        }
        switch (lybz) {
            case 0:
                return "线下";
            case 1:
                return "线上";
            case 2:
                return "辅助开评标";
            default:
                return "";
        }
    }

}
