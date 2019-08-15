package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.SectionDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.PrjSectionDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.SectionService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service("sectionMsgService")
public class SectionServiceImpl implements SectionService {
	
	@Autowired
	private SectionDao sectionDao;
	
	@Autowired
	private IdRelationService idRelationService;

	@Override
	public PrjSectionDto getSectionData(String sectionId) throws ExchangeException{
		Map<String, Object> sectionMap = sectionDao.selectById("xm_xmfb", sectionId);
		PrjSectionDto section = new PrjSectionDto();
		section.setTenderProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, Convert.toStr(sectionMap.get("zbxh"))));
		section.setSectionCode(Convert.toStr(sectionMap.get("bywbh")));
		section.setSectionName(Convert.toStr(sectionMap.get("bmc")));
		section.setSectionContent(Convert.toStr(sectionMap.get("bms")));
		Object classifyCode = sectionDao.selectByParamColumn("pmbh", "xm_pm", "id", Convert.toStr(sectionMap.get("bfl")));
		section.setSectionClassifyCode(Convert.toStr(classifyCode));
		section.setTenderMode(Convert.toInt(sectionMap.get("cgfs")));
		if(Convert.toInt(sectionMap.get("hbdw")) == 1) {
			section.setContractReckonPrice(Convert.toBigDecimal(sectionMap.get("bys")).multiply(new BigDecimal("100")));
		}else {
			section.setContractReckonPrice(Convert.toBigDecimal(sectionMap.get("bys")).multiply(new BigDecimal("1000000")));
		} 
		section.setCurrencyCode(Convert.toStr(sectionMap.get("bz") == null ? "156" : sectionMap.get("bz")));
		section.setPriceUnit(-1);//TODO 数据中心货币单位同意为：分， 暂不取内控中的值Convert.toInt(sectionMap.get("hbdw")));
		section.setSyndicatedFlag(Convert.toInt(sectionMap.get("sfyxlht")));
		section.setProjectType(Convert.toInt(sectionMap.get("lx")));
		section.setTenderOfferMark(Convert.toInt(sectionMap.get("tbbjbz")));
		section.setStatus(Convert.toInt(sectionMap.get("zt")));
		section.setSourceId(sectionId);
		section.setUpdateTime(new Date());
		section.setCreateTime(new Date());
		section.setBidQualification(Convert.toStr(sectionMap.get("zgyq") == null ? "" : sectionMap.get("zgyq")));
		return section;
	}
}
