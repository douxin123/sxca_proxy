package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.TenderProjectDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.PrjTenderProjectDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.TenderProjectService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tenderProjectMsgService")
public class TenderProjectServiceImpl implements TenderProjectService {

	@Autowired
	private TenderProjectDao tenderProjectDao;
	
	@Autowired
	private IdRelationService idRelationService;
	
	@Override
	public PrjTenderProjectDto getTenderProjectData(String tenderProjectId) throws ExchangeException{
		Map<String, Object> tenderProjectMap =  tenderProjectDao.selectById("xm_zbxm", tenderProjectId);
		Map<String, Object> extendMap = new HashMap<String, Object>();
		List<Map<String, Object>> extendList = tenderProjectDao.selectByParamColumn("xm_zbxmkz", "zbxh", tenderProjectId);
		//监管平台
		List<String> supervisePlatforms = tenderProjectDao.getSupervisePlatformsByXmid(tenderProjectId);
		if(CollectionUtil.isNotEmpty(extendList)) {
			extendMap = extendList.get(0);
		}
		PrjTenderProjectDto tenderProject = new PrjTenderProjectDto();
		if (StringUtils.isEmpty(tenderProjectMap.get("wtxmxh"))) {
			tenderProject.setMasterPrjId(-1l);
		} else {
			tenderProject.setMasterPrjId(idRelationService.getDataId(ObjectTypeEnum.PROJECT, Convert.toStr(tenderProjectMap.get("wtxmxh"))));
		}
		tenderProject.setCode(Convert.toStr(tenderProjectMap.get("ywbh")));
		tenderProject.setName(Convert.toStr(tenderProjectMap.get("xmmc")));
		Object classifyCode = tenderProjectDao.selectByParamColumn("pmbh", "xm_pm", "id", Convert.toStr(tenderProjectMap.get("xmfl")));
		tenderProject.setClassifyCode(Convert.toStr(classifyCode));
		tenderProject.setRegionCode(Convert.toStr(tenderProjectMap.get("ssdq") == null ? "" : tenderProjectMap.get("ssdq")));
		tenderProject.setTendererId(Convert.toLong(tenderProjectDao.selectByParamColumn("data_id", "gg_jgbh", "jgbh", Convert.toStr(tenderProjectMap.get("zbrjgbh")))));
		tenderProject.setTenderAgencyId(Convert.toLong(tenderProjectDao.selectByParamColumn("data_id", "gg_jgbh","jgbh", Convert.toStr(tenderProjectMap.get("dljgbh")))));
		tenderProject.setTenderMode(Convert.toInt(tenderProjectMap.get("cgfs")));//招标方式？？
		tenderProject.setTenderOrganizeForm(2);//招标组织形式 TODO 暂写2 委托招标
		tenderProject.setUpdateTime(new Date());
		tenderProject.setApplicableLaw(Convert.toInt(tenderProjectMap.get("syfl")));
		tenderProject.setItemCategory(Convert.toInt(tenderProjectMap.get("xmlmfl")));
		tenderProject.setLxr(Convert.toStr(tenderProjectMap.get("lxr") == null ? "" : tenderProjectMap.get("lxr")));
		tenderProject.setLxfs(Convert.toStr(tenderProjectMap.get("lxfs") == null ? "" : tenderProjectMap.get("lxfs")));
		if(tenderProjectMap.get("jdsj") != null) {
			tenderProject.setCreateTime(DateUtil.parse(Convert.toStr(tenderProjectMap.get("jdsj"))));//立项时间
		}else {
			tenderProject.setCreateTime(new Date());
		}
		//TODO 以下内容是内控中存在且是非必填项   -- 开始
		//TODO 招标内容与范围及招标方案说明
		tenderProject.setTenderContent(Convert.toStr(Convert.toStr(extendMap.get("xmnr") == null ? "" : extendMap.get("xmnr") )));
		tenderProject.setSuperviseDeptType(97);//Convert.toInt(extendMap.get("jgbm")));//TODO 监督部门代码类型
		//TODO 监督部门代码
		if(extendMap.get("jdbmdm") != null) {
			tenderProject.setSuperviseDeptCode(Convert.toStr(Convert.toStr(extendMap.get("jdbmdm"))));
		}else {
			tenderProject.setSuperviseDeptCode("无");
		}
		//TODO 监督部门名称
		if(extendMap.get("jdbmmc") != null) {
			tenderProject.setSuperviseDeptName(Convert.toStr(Convert.toStr(extendMap.get("jdbmmc"))));
		}else {
			tenderProject.setSuperviseDeptName("无");
		}
		tenderProject.setApproveDeptType(97);//TODO 审核部门代码类型
		//TODO 审核部门代码
		if(extendMap.get("shbmdm") != null) {
			tenderProject.setApproveDeptCode(Convert.toStr(Convert.toStr(extendMap.get("shbmdm"))));
		}else {
			tenderProject.setApproveDeptCode("无");
		}
		//TODO 审核部门名称
		if(extendMap.get("shbmmc") != null) {
			tenderProject.setApproveDeptName(Convert.toStr(Convert.toStr(extendMap.get("shbmmc"))));
		}else {
			tenderProject.setApproveDeptName("无");
		}
		if(null == tenderProjectMap.get("ljdz")) {
			tenderProject.setAttachmentCode("无");//附件存储 TODO
		}else {
			tenderProject.setAttachmentCode(Convert.toStr(tenderProjectMap.get("ljdz")));//附件存储 TODO
		}
		// -- 结束
		tenderProject.setDevelopOrgName("无");//Convert.toStr(tenderProjectMap.get("")));//TODO 建设单位
		// 公开类型：0--依法公开；1--授权使用；2--不公开,默认0
		tenderProject.setOpenType(new BigDecimal(0));
		tenderProject.setUserId(Convert.toLong(tenderProjectDao.selectById("data_id", "gg_czyb", Convert.toStr(tenderProjectMap.get("xmfzr")))));
		tenderProject.setSourceId(tenderProjectId);
		tenderProject.setProjectType(Convert.toInt(tenderProjectMap.get("lx")));
		// 招标项目来源标志 0-线下 1-线上 2-辅助开评标
		BigDecimal sourceSymbol=new BigDecimal("0");
		if(tenderProjectMap.get("lybz")!=null){
			sourceSymbol=Convert.toBigDecimal(tenderProjectMap.get("lybz"));
		}
		tenderProject.setSourceSymbol(sourceSymbol);
		// 监管平台名称与代码
		String platforms = "";
		if(supervisePlatforms!=null&&supervisePlatforms.size()>0){
			platforms= org.jsoup.helper.StringUtil.join(supervisePlatforms,";");
		}
		tenderProject.setSupervisePlatforms(platforms);
		return tenderProject;
	}
	
}
