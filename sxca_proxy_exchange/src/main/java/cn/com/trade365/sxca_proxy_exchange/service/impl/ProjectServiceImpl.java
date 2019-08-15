package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.ProjectDao;
import cn.com.trade365.platform.project.dto.PrjProjectDto;
import cn.com.trade365.sxca_proxy_exchange.service.ProjectService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service("projectMsgService")
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
	@Override
	public PrjProjectDto getProjectData(String projectId) {
		Map<String, Object> projectMap = projectDao.selectById("xm_xm", projectId);
		PrjProjectDto project = new PrjProjectDto();
		project.setCode(Convert.toStr(projectMap.get("xmbh")));
		project.setName(Convert.toStr(projectMap.get("xmmc")));
		project.setRegionCode(Convert.toStr(projectMap.get("ssdq")));
		project.setAddress(Convert.toStr(projectMap.get("xmdz")));
		project.setLegalPerson(Convert.toStr(projectMap.get("xmfr") == null ? "" : projectMap.get("xmfr")));
		project.setIndustriesType(Convert.toStr(projectMap.get("sshy"))); //TODO 项目行业分类
		project.setInvestProjectCode(Convert.toStr(projectMap.get("tzxmtydm") == null ? "" : projectMap.get("tzxmtydm")));
		project.setFundSource(Convert.toStr(projectMap.get("zjly")));
		project.setProjectScale(Convert.toStr(projectMap.get("xmgm")));
		project.setTenderer(Convert.toLong(projectDao.selectByParamColumn("data_id", "gg_jgbh", "jgbh", Convert.toStr(projectMap.get("zbrjgbh")))));
		project.setConnector(Convert.toLong(projectDao.selectById("data_id", "gg_czyb",  Convert.toStr(projectMap.get("lxrid")))));
		project.setLinkman(Convert.toStr(projectMap.get("lxr") == null ? "" : projectMap.get("lxr")));
		project.setContactInformation(Convert.toStr(projectMap.get("lxfs") == null ? "" : projectMap.get("lxfs")));
		project.setAgency(Convert.toLong(projectDao.selectByParamColumn("data_id", "gg_jgbh", "jgbh", Convert.toStr(projectMap.get("zbdljgbh")))));
		project.setProjectManager(Convert.toLong(projectDao.selectById("data_id", "gg_czyb",  Convert.toStr(projectMap.get("jdrid")))));
		project.setApprovalName(Convert.toStr(projectMap.get("xmpwmc") == null ? "" : projectMap.get("xmpwmc")));
		project.setApprovalNumber(Convert.toStr(projectMap.get("xmpzwh") == null ? "" : projectMap.get("xmpzwh")));
		project.setApprovalAuthority(Convert.toStr(projectMap.get("xmpzdw") == null ? "" : projectMap.get("xmpzdw")));
		project.setCreateTime(new Date());//TODO 日期转换  ConvertUtils.convert2Date(projectMap.get("jdsj"))
		project.setUpdateTime(new Date());//TODO 日期转换  ConvertUtils.convert2Date(projectMap.get("jdsj"))
		//以下数据暂无
		project.setAttachmentCode("附件关联标识号无");//Convert.toStr(projectMap.get("code")));//附件关联标识号
		project.setInvestmentAmount(new BigDecimal(0));//new BigDecimal(Convert.toStr(projectMap.get("code"))));//TODO  投资金额(金额单位：分)
		project.setCurrencyCode("156");//Convert.toStr(projectMap.get("code")));//金额币种
		project.setPriceUnit(-1);//ConvertUtils.convert2Int(projectMap.get("code")));//金额单位
		project.setSourceId(projectId);
		return project;
	}
}
