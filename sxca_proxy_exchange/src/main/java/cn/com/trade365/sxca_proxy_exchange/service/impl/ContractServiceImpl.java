package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.dao.ContractDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.CtContractDto;
import cn.com.trade365.sxca_proxy_exchange.service.ContractService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author: JianBingZhang
 * @Date: 2018/11/8 15:49
 * @Version 1.0
 */
@Service("contractService")
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Autowired
    IdRelationService idRelationService;

    @Autowired
    RestComponent restComponent;

    @Override
    public CtContractDto getContractData(MsgEvent msgEvent) throws ExchangeException{
        String contractId = msgEvent.getData().get("contractId");
        Map<String,Object> conMap = contractDao.queryById(contractId);
        CtContractDto contract = new CtContractDto();
        contract.setTenderProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, ConvertUtils.convert2str(conMap.get("tenderProjectCode"))));
        contract.setSectionId(idRelationService.getDataId(ObjectTypeEnum.SECTION,ConvertUtils.convert2str(conMap.get("bidSectionCode"))));
        contract.setBidderCode(ConvertUtils.convert2str(conMap.get("bidderCode")));
        contract.setBidderName(ConvertUtils.convert2str(conMap.get("bidderName")));
        contract.setContractContent(ConvertUtils.convert2str(conMap.get("contractContent")));
        contract.setContractName(ConvertUtils.convert2str(conMap.get("contractName")));
        contract.setContractPrice(ConvertUtils.convert2Cent(conMap.get("contractPrice"),"元"));
        contract.setContractSignTime(ConvertUtils.convert2Date(conMap.get("contractSignTime")));
        contract.setFinishTime(ConvertUtils.convert2Date(conMap.get("finishTime")));
        contract.setPriceCurrency(ConvertUtils.convert2str(conMap.get("priceCurrency")));
        // 金额单位  默认 -1分 0元 1万元
        contract.setPriceUnit(-1);
        // 查询代理机构ZTDM 主体代码
        Map<String,Object>  tender = contractDao.queryZbJg(ConvertUtils.convert2str(conMap.get("tenderProjectCode")));
        contract.setTenderCode(ConvertUtils.convert2str(tender.get("ZTDM")));
        contract.setTendererName(ConvertUtils.convert2str(tender.get("JGMC")));
        contract.setPlatformCode(msgEvent.getPlatformCode());
        contract.setSourceId(ConvertUtils.convert2str(conMap.get("id")));
        contract.setTenderId(ConvertUtils.convert2str(tender.get("DATA_ID")));
        // 查询供应商机构 DATA_ID
        Map<String,Object>  bidder = contractDao.selectMapByParamColumn("gg_jgbh","ZTDM",ConvertUtils.convert2str(conMap.get("bidderCode")));
        contract.setBidderId(ConvertUtils.convert2str(bidder.get("DATA_ID")));

        // 实际履约期限
        if(conMap.get("actuallyPeriod") != null){
            contract.setActuallyPeriod(ConvertUtils.convert2Int(conMap.get("actuallyPeriod")));
        }else{
            contract.setActuallyPeriod(0);
        }
        // 合同期限
        if(conMap.get("contractLimitTime") != null){
            contract.setLimiteTime(ConvertUtils.convert2str(conMap.get("contractLimitTime")));
        }else{
            contract.setLimiteTime("");
        }
        // 履约变更内容
        if(conMap.get("performChangeContent") !=null){
            contract.setPerformChangeContent(ConvertUtils.convert2str(conMap.get("performChangeContent")));
        }else{
            contract.setPerformChangeContent("");
        }
        // 履约信息
        if(conMap.get("performInfo") != null){
            contract.setPerformInfo(ConvertUtils.convert2str(conMap.get("performInfo")));
        }else{
            contract.setPerformInfo("");
        }
        // 递交时间
        if(conMap.get("contractSubmitTime")!= null){
            contract.setSubmitTime(ConvertUtils.convert2Date(conMap.get("contractSubmitTime")));
        }else{
            contract.setSubmitTime(new Date());
        }
        // 质量要求
        if(conMap.get("qualityRequire")!=null){
            contract.setQualityRequire(ConvertUtils.convert2str(conMap.get("qualityRequire")));
        }else{
            contract.setQualityRequire("");
        }
        // 合同结算金额
        if(conMap.get("contractSettlePrice")!=null){
            contract.setSettlePrice(ConvertUtils.convert2Cent(conMap.get("contractSettlePrice"),"元"));
        }else{
            contract.setSettlePrice(0L);
        }
        // 附件关联标识号
        contract.setAttachmentCode("");
        return contract;
    }
}

