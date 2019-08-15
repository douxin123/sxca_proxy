package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.BidBidderDao;
import cn.com.trade365.sxca_proxy_exchange.dao.IdRelationDao;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.BidBidderDto;
import cn.com.trade365.sxca_proxy_exchange.service.BidderService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋建华
 * @date 2018-11-09 10:33
 **/
@Service("bidderService")
public class BidderServiceImpl implements BidderService {

    @Autowired
    BidBidderDao bidBidderDao;
    @Autowired
    private IdRelationDao idRelationDao;

    @Override
    public Map<String, BidBidderDto> getBidderData(MsgEvent msgEvent) throws ExchangeException {
        Map<String, BidBidderDto> map = new HashMap<>();
        Map<String, String> dataMap = msgEvent.getData();

        //tradeId = xm_xmfb.id
        Map<String, Object> xm_xmfbMap = bidBidderDao.selectById("xm_xmfb", dataMap.get("tradeId"));
        Map<String, Object> xm_zbxmMap = bidBidderDao.selectById("xm_zbxm", ConvertUtils.convert2str(xm_xmfbMap.get("ZBXH")));
        List<Map<String, Object>> xm_bmgysList = bidBidderDao.selectBmgysList(dataMap.get("tradeId"));

        RelationEntity projectId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_PROJECT.getCode(), ConvertUtils.convert2str(xm_xmfbMap.get("ZBXH")));
        RelationEntity sectionId = idRelationDao.getRelationByTradeId(ObjectTypeEnum.SECTION.getCode(), ConvertUtils.convert2str(dataMap.get("tradeId")));
        if (xm_bmgysList != null && !xm_bmgysList.isEmpty()) {
            for (Map<String, Object> bmgysMap : xm_bmgysList) {
                BidBidderDto bidBidder = new BidBidderDto();
                bidBidder.setProjectId(projectId.getDataId());
                bidBidder.setSectionId(sectionId.getDataId());
                bidBidder.setBidMode(ConvertUtils.convert2Int(xm_xmfbMap.get("cgfs")));//投标方式 1公开 2邀请
                bidBidder.setIsUnion(ConvertUtils.convert2Int(bmgysMap.get("sflht")));//是否联合体
                bidBidder.setUnionId(0);//TODO 联合体id
                Object orgId = bidBidderDao.selectByParamColumn("data_id", "gg_jgbh", "jgbh", Convert.toStr(bmgysMap.get("gysjgbh")));
                bidBidder.setOrganizationId(Convert.toLong(orgId));//企业id
                bidBidder.setOrganizationName(ConvertUtils.convert2str(bmgysMap.get("gysmc")));//企业名称
                bidBidder.setCreaterId(0L);//TODO 创建人id
                bidBidder.setCreaterName(ConvertUtils.convert2str(xm_zbxmMap.get("fzrmc")));//创建人名称
                bidBidder.setRespondTime(ConvertUtils.convert2Date(bmgysMap.get("bmsj")));//响应时间
                bidBidder.setBidReviewSign(ConvertUtils.convert2Int(bmgysMap.get("shbz")));//购标审核标志
                Map<String,Object> zbgys = bidBidderDao.getZbgys(ConvertUtils.convert2str(dataMap.get("tradeId")),Convert.toStr(bmgysMap.get("gysjgbh")));
                if(zbgys == null){
                    //投标人状态 0初始化 1已拒绝 2废除 3参与 4预中标 5已中标
                    bidBidder.setStatus(getStatus(ConvertUtils.convert2Int(bmgysMap.get("zt")),true));
                }else{
                    bidBidder.setStatus(getStatus(ConvertUtils.convert2Int(zbgys.get("zt")),false));
                }
                bidBidder.setExplanation("");//TODO 说明
                bidBidder.setSourceId(ConvertUtils.convert2str(bmgysMap.get("id")));
                bidBidder.setPlatformCode(msgEvent.getPlatformCode());
                bidBidder.setCreateTime(new Date());//TODO
                bidBidder.setUpdateTime(new Date());//TODO
                //新加字段
                //获取是否通过资格审查
                bidBidder.setPassedApplicant(0);
                if(ConvertUtils.convert2Int(xm_xmfbMap.get("LX"))==2){//如果是资审项目则查询投标人通过状态
                    bidBidder.setPassedApplicant(getPassedApplicant(ConvertUtils.convert2str(bmgysMap.get("bxh")),ConvertUtils.convert2str(bmgysMap.get("gysjgbh"))));
                }
                bidBidder.setIsFloatRate(0);//默认0否
                //投标文件上传时间
                bidBidder.setCheckinTime(ConvertUtils.convert2Date(bmgysMap.get("tbsj")));
                //保证金递交时间
                bidBidder.setMarginReceiveTime(ConvertUtils.convert2Date(bmgysMap.get("bzjdjsj")));
                //保证金缴纳方式
                bidBidder.setMarginPayForm(getMarginPayForm(ConvertUtils.convert2Int(bmgysMap.get("bzjjnfs"))));
                //获取下载时间
                bidBidder.setBidDocDownloadTime(new Date());

                map.put(ConvertUtils.convert2str(bmgysMap.get("id")), bidBidder);
            }
        }
        return map;

    }

    /**
     * 获取招标文件下载时间
     * @param sectionId
     * @param orgId
     * @return
     */
    private Date getBidDocDownloadTime(String sectionId,String orgId){
        return bidBidderDao.getBidDocDownloadTime(sectionId,orgId);
    }

    /**
     * 保证金递交方式
     * @param jnfs
     * @return
     */
    private Integer getMarginPayForm(Integer jnfs){
        if(jnfs==0){
            return 1;//资金
        }else if(jnfs==2){
            return 2;//投标保函
        }else if(jnfs==1){
            return 3;//投标保险
        }
        return 9;//其他
    }

    /**
     * 是否通过资格预审
     * @param sectionId
     * @param orgId
     * @return 2 不通过 1 通过
     */
    private Integer getPassedApplicant(String sectionId,String orgId){
        Integer passed=bidBidderDao.getPassedApplicant(sectionId,orgId);
        if(passed!=null&&passed==1){
            return 1;
        }
        return 2;//
    }

    /**
     * 获取投标人状态
     *
     * @param zt xm_bmgys.zt
     * @return
     */
    private Integer getStatus(Integer zt, boolean isZbgys) {
        if (zt == null) {
            return null;
        }
        //投标人状态 0初始化 1已拒绝 2废除 3参与 4预中标 5已中标
        switch (zt) {
            case -1:
                if(isZbgys) {
                    return 1; //无效
                }
                return 1; //审查未通过
            case -2:
                return 1; //投标无效
            case -3:
                return 2; //供应商主动退出
            case 1:
                return 5;//中标
            case 0:
                return 4;//候选
            case 45:
                return 3; //购标申请
            case 46:
                return 3; //购标支付
            case 47:
                return 3; //标书下载
            case 48:
                return 3; //投标应答
            case 49:
                return 3; //已投标
            case 50:
                return 3; //已解密
            case 51:
                return 3; //已签字确认
            case 65:
                return 3; //评审应答
            case 75:
                return 5; //中标
            case 76:
                return 3; //未中标
            case 98:
                return 3; //异常结束
            case 99:
                return 3; //结束
            default:
                return 0;
        }
    }

}
