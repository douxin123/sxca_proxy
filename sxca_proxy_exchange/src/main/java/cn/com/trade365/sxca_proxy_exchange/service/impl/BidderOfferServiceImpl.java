package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.dao.BidderOfferDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidBidderOfferDto;
import cn.com.trade365.sxca_proxy_exchange.service.BidderOfferService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.com.trade365.sxca_proxy_exchange.utils.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: JianBingZhang
 * @Date: 2018/11/8 15:49
 * @Version 1.0
 */
@Service("bidderOfferService")
public class BidderOfferServiceImpl implements BidderOfferService {

    @Autowired
    IdRelationService idRelationService;

    @Autowired
    RestComponent restComponent;

    @Autowired
    BidderOfferDao bidderOfferDao;

    @Override
    /**
     * @Description: 推送bidderOffer
     * @Param: [msgEvent]
     * @return: cn.com.trade365.platform.vo.BidBidderOffer
     * @Author: JianbingZhang
     * @Date:
     */
    public void pushBidderOfferData(MsgEvent msgEvent) throws Exception {
        Map<String, String> dataMap = msgEvent.getData();
        String sectionId = dataMap.get("tradeId");
        //根据id 获取分包信息 LEFT JOIN zb_tbsj zbsj ON zbsj.TBBGXH = tbbg.ID
        List<Map<String, Object>> infos = bidderOfferDao.queryBidderOfferList(sectionId);
        if (infos != null && infos.size() > 0) {
            for (Map<String, Object> item : infos) {
                BidBidderOfferDto bidderOffer = new BidBidderOfferDto();
                // 投标人id
                bidderOffer.setBidderId(idRelationService.getDataId(ObjectTypeEnum.BIDBIDDER, ConvertUtils.convert2str(item.get("ID"))));
                // 投标价格 待校准
                Long bidPrice = 0L;
                Long otherBidPrice = 0L;
                if (item.get("TBJG") == null && item.get("QTTBJG") == null) {
                    List<Map<String, Object>> prices = bidderOfferDao.queryBidFieldData(ConvertUtils.convert2str(item.get("TBNRXH")),ConvertUtils.convert2str(item.get("gystbnrxh")), "%投标总价%","%投标报价%");
                    if (prices != null && prices.size() > 0) {
                        if (StringUtils.equals(ConvertUtils.convert2str(prices.get(0).get("SJLX")), "6")) {
                            if(prices.get(0).get("ZDZ")!=null&&!StringUtils.isEmpty(ConvertUtils.convert2str(prices.get(0).get("ZDZ")))){
                                bidPrice = ConvertUtils.convert2Cent(prices.get(0).get("ZDZ"), "万元");
                            }
                        } else {
                            if(prices.get(0).get("ZDZ")!=null&&!StringUtils.isEmpty(ConvertUtils.convert2str(prices.get(0).get("ZDZ")))){
                                otherBidPrice = ConvertUtils.convert2Cent(prices.get(0).get("ZDZ"), "万元");
                            }
                        }
                    }
                } else {
                    if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 0) {
                        bidPrice = ConvertUtils.convert2Cent(item.get("TBJG"), item.get("HBDW"));
                    } else if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 1) {
                        bidPrice = ConvertUtils.convert2Cent(item.get("QTTBJG"), item.get("HBDW"));
                    } else {
                        String qtbj = ConvertUtils.convert2str(item.get("QTTBJG"));
                        bidPrice = ConvertUtils.convert2Long(new BigDecimal(qtbj).setScale(0, BigDecimal.ROUND_UP));
                    }
                }
                bidderOffer.setBidPrice(bidPrice);
                // 币种 item.get("BZ")
                bidderOffer.setCurrencyCode(item.get("BZ") == null ? "156" : ConvertUtils.convert2str(item.get("BZ")));
                // 工期,交货期,服务期
                String duration = "";
                List<Map<String, Object>> durations = bidderOfferDao.queryBidFieldData(ConvertUtils.convert2str(item.get("TBNRXH")),ConvertUtils.convert2str(item.get("gystbnrxh")), "%交货期%","%工期%");
                if (durations != null && durations.size() > 0) {
                    duration = ConvertUtils.convert2str(durations.get(0).get("ZDZ"));
                }
                bidderOffer.setDuration(duration);
                // 中标价格
                if (item.get("ZBJG") == null && item.get("QTZBJG") == null) {
                    bidderOffer.setWinBidPrice(0L);
                } else {
                    if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 0) {
                        bidderOffer.setWinBidPrice(ConvertUtils.convert2Cent(item.get("ZBJG"), item.get("HBDW")));
                    } else if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 1) {
                        bidderOffer.setWinBidPrice(ConvertUtils.convert2Cent(item.get("QTZBJG"), item.get("HBDW")));
                    } else {
                        bidderOffer.setWinBidPrice(ConvertUtils.convert2Long(item.get("QTZBJG")));
                    }
                }
                // 其他投标价 看投标报价金额描述
                bidderOffer.setOtherBidPrice(otherBidPrice);
                // 单位  item.get("HBDW") == null ? "万元" : ConvertUtils.convert2str(item.get("HBDW"))
                bidderOffer.setPriceUnit("-1");
                // 排名
                bidderOffer.setRank(ConvertUtils.convert2Int(item.get("PM")));
                // 分包id
                bidderOffer.setSectionId(idRelationService.getDataId(ObjectTypeEnum.SECTION, ConvertUtils.convert2str(item.get("BXH"))));
                // 项目id
                bidderOffer.setTenderProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, ConvertUtils.convert2str(item.get("ZBXH"))));
                // 创建时间
                bidderOffer.setCreateTime(DateUtil.getCurrTime());
                // 更新时间
                bidderOffer.setUpdateTime(DateUtil.getCurrTime());
                bidderOffer.setPlatformCode(msgEvent.getPlatformCode());
                bidderOffer.setSourceId(sectionId);
                // 其他类型中标价 比率或文字描述类型的报价   TBBJBZ  投标报价标志
                bidderOffer.setOtherWinbidPrice("");
                // 投标文件id  目录编号区分文件类型 待校准   wdlx 1招标，2资审
                String bmId = ConvertUtils.convert2str(item.get("ID"));
                bidderOffer.setBidFileId(bidderOfferDao.queryFileId(bmId, 1));
                // 资审文件id 待校准
                bidderOffer.setQualFileId(bidderOfferDao.queryFileId(bmId, 2));
                // 获取评审信息
                // 评分结果
                if (item.get("PFJG") == null) {
                    bidderOffer.setEvalResult("暂无");
                } else {
                    bidderOffer.setEvalResult(ConvertUtils.convert2str(item.get("PFJG")));
                }
                // 评标价格
                if (item.get("PBJG") == null && item.get("QTPBJG") == null) {
                    bidderOffer.setEvalPrice(0L);
                } else {
                    if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 0) {
                        bidderOffer.setEvalPrice(ConvertUtils.convert2Cent(item.get("PBJG"),item.get("HBDW")));
                    } else if (ConvertUtils.convert2Int(item.get("TBBJBZ")) == 1) {
                        bidderOffer.setEvalPrice(ConvertUtils.convert2Cent(item.get("QTPBJG"),item.get("HBDW")));
                    }else {
                        bidderOffer.setEvalPrice(ConvertUtils.convert2Long(item.get("QTPBJG")));
                    }
                }
//                Map<String, Object> evalInfoMap = bidderOfferDao.getEvalInfo(sectionId, bmId);
//                if (evalInfoMap == null) {
//                    bidderOffer.setEvalResult("暂无");
//                    bidderOffer.setEvalPrice(0L);
//                } else {
//                    // 评分结果
//                    if (evalInfoMap.get("PFJG") == null) {
//                        bidderOffer.setEvalResult("暂无");
//                    } else {
//                        bidderOffer.setEvalResult(ConvertUtils.convert2str(evalInfoMap.get("PFJG")));
//                    }
//                    // 评标价格 单位：分
//                    String hbdw = "";
//                    if (StringUtils.equals(ConvertUtils.convert2str(evalInfoMap.get("HBDW")), "1")) {
//                        hbdw = "元";
//                    } else if (StringUtils.equals(ConvertUtils.convert2str(evalInfoMap.get("HBDW")), "2")) {
//                        hbdw = "万元";
//                    }
//                    if (evalInfoMap.get("PBJG") == null) {
//                        bidderOffer.setEvalPrice(0L);
//                    } else {
//                        bidderOffer.setEvalPrice(ConvertUtils.convert2Cent(evalInfoMap.get("PBJG"), hbdw));
//                    }
//                }
                // TODO 其他说明
                bidderOffer.setOtherExplanation("");
                // TODO 创建人id
                bidderOffer.setCreaterId(0L);
                // TODO 创建人姓名
                bidderOffer.setCreateName("");
                try {
                    //判断关系是否存在 存在则调用更新接口
                    ResultData<Long> reslut;
                    ResultData<Boolean> updateResult;
                    Long dataId;
                    if (idRelationService.isExitDataId(ObjectTypeEnum.BIDDEROFFER, ConvertUtils.convert2str(item.get("ID")))) {
                        dataId = idRelationService.getDataId(ObjectTypeEnum.BIDDEROFFER, ConvertUtils.convert2str(item.get("ID")));
                        updateResult = restComponent.put(Repositories.getUrl(Repositories.BIDBIDDEROFFER_SAVE_URL) + "/" + dataId, bidderOffer, new ParameterizedTypeReference<ResultData<Boolean>>() {
                        });
                        if (updateResult.getCode() != 200) {
                            throw new Exception(updateResult.getMessage());
                        }
                        log.info("投标人投标数据信息更新成功");
                    } else {
                        // 根据业务需求 调用数据中心接口 推送bidderOffer
                        reslut = restComponent.post(Repositories.getUrl(Repositories.BIDBIDDEROFFER_SAVE_URL), bidderOffer, new ParameterizedTypeReference<ResultData<Long>>() {
                        });
                        if (reslut.getCode() == 200) {
                            //关系表存储
                            RelationEntity relationEntity = new RelationEntity();
                            relationEntity.setCode(ObjectTypeEnum.BIDDEROFFER.getCode());
                            relationEntity.setTradeId(ConvertUtils.convert2str(item.get("ID")));
                            relationEntity.setData(JSONObject.toJSONString(dataMap));
                            //数据中心id
                            relationEntity.setDataId(reslut.getData());
                            idRelationService.relation(relationEntity);
                            log.info("投标人投标数据信息推送成功");
                        }
                    }
                } catch (Exception ex) {
                    throw new ExchangeException("调用失败", ex);
                }

            }
        }
    }
}

