package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.platform.project.dto.PrjTenderFileDto;
import cn.com.trade365.store.Client;
import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.TenderFileDao;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.TenderFileService;
import cn.com.trade365.sxca_proxy_exchange.utils.FileUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tenderFileService")
public class TenderFileServiceImpl implements TenderFileService {
	
	@Autowired
	private TenderFileDao tenderFileDao;
	
	@Autowired
	private IdRelationService idRelationService;
	
	@Autowired
	private Client fileClient;

	@Autowired
	private BeanConfig beanConfig;
	
	@Override
	public PrjTenderFileDto getTenderFileData(String tenderFileSectionId) throws Exception{
		//查询内控文件分包表的数据
		Map<String, Object> fileSectionMap = tenderFileDao.selectById("xm_wjfb", tenderFileSectionId);
		String tenderFileId = Convert.toStr(fileSectionMap.get("wjxh"));
		//查询招标文件数据
   		Map<String, Object> tenderFileMap =  tenderFileDao.selectById("xm_cgwj", tenderFileId);
		Integer projectType = Convert.toInt(tenderFileMap.get("lx"));
		//费用类别(1-标书费,2-邮寄费 3-图纸押金 4-中标服务费 5-投标保证金)
		Map<String, Object> marginMap =  tenderFileDao.querySectionInfo(projectType, Convert.toStr(fileSectionMap.get("bxh")), 5);
		Map<String, Object> docMap =  tenderFileDao.querySectionInfo(projectType, Convert.toStr(fileSectionMap.get("bxh")), 1);
		PrjTenderFileDto tenderFile = new PrjTenderFileDto();
		tenderFile.setTenderProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, Convert.toStr(tenderFileMap.get("zbxh"))));
		tenderFile.setSectionId(idRelationService.getDataId(ObjectTypeEnum.SECTION, Convert.toStr(fileSectionMap.get("bxh"))));
		tenderFile.setProjectType(projectType);
		tenderFile.setDocCode("无");//TODO Convert.toStr(tenderFileMap.get("zbwj")));
		tenderFile.setDocName(Convert.toStr(tenderFileMap.get("wjmc")));
		DateTime fbsj = DateUtil.parse(Convert.toStr(tenderFileMap.get("fbsj")));
		tenderFile.setSubmitTime(fbsj == null ? new Date() : fbsj);
		tenderFile.setQualifications(Convert.toStr(marginMap.get("zgyq") == null ? "无" : marginMap.get("zgyq")));//TODO Convert.toStr(tenderFileMap.get("")));
		tenderFile.setValidTime("90天");//TODO 申请有效期 - 暂时写90天
		tenderFile.setDocSubmitMethod("线上");//TODO 申请文件递交方法 - 暂写线上 
		tenderFile.setDocSubmitEndTime(DateUtil.parse(Convert.toStr(tenderFileMap.get("jssj"))));//申请文件递交截止时间或上传时间
		tenderFile.setOpenTime(DateUtil.parse(Convert.toStr(tenderFileMap.get("kbsj"))));//文件开启时间或开标时间
		tenderFile.setEvaluationMethod("暂无，交易存在，返回内控");//Convert.toStr(tenderFileMap.get()));//TODO 评审办法 - 交易存在，返回内控
		tenderFile.setDocOpenMethod("线上");//TODO 文件开启方式  默认：线上
		if(Convert.toInt(marginMap.get("sfsq")) == 0) {//是否收取(0-收取 1-不收取)
			if(Convert.toInt(marginMap.get("jsfs")) == 2) {//计算方式(0-不收取  1-浮动费率 （发改价格[2011]534号）2-定额   3-比例)
				tenderFile.setMarginAmount(Convert.toLong(Convert.toFloat(marginMap.get("je")) * 100));// 转换成分
			}else {
				//TODO   1-浮动费率   3-比例  暂时写0
				tenderFile.setMarginAmount(0L);
			}
		}else {
			//如果不收取
			tenderFile.setMarginAmount(0L);
		}
		tenderFile.setMarginCurrency(156);//TODO 人民币暂写156，投标保证金币种代码
		tenderFile.setMarginPayType(Convert.toStr(tenderFileMap.get("bzjjnfs")));//投标保证金缴纳方式
		Object qualType = tenderFileDao.selectByParamColumn("zgscfs", "xm_zbxm", "id" ,Convert.toStr(fileSectionMap.get("zbxh")));
		tenderFile.setQualType(Convert.toInt(qualType));//资格审查方式
		tenderFile.setDocPrice(Convert.toLong(Convert.toFloat(docMap.get("je")) * 100));//单位转换成：分
		tenderFile.setDocPriceCurrency(156);//TODO 人民币暂写156，投标保证金币种代码
		tenderFile.setBidOpenPlace(Convert.toStr(marginMap.get("kbdd")));//开标地点  xm_xmfb.KBDD
		tenderFile.setIsFictitious(Convert.toBigDecimal(tenderFileMap.get("sfxn")));
		// 上传附件
		List<Map<String, Object>> list = tenderFileDao.selectByParamColumn("gg_fjghwd", "ywxh", Convert.toStr(tenderFileMap.get("ljdz")));
		if (CollectionUtil.isNotEmpty(list)) {
			String  attachmentCode = "";
			for (Map<String, Object> map : list) {
				attachmentCode += fileClient.store(
						new File(beanConfig.getFilePath() + map.get("path") + map.get("realname")), null) + ",";
			}
			tenderFile.setAttachmentCode(attachmentCode.substring(0, attachmentCode.length() - 1));
		}else {
			tenderFile.setAttachmentCode("无");
		}
		// 上传文件doc
		list = tenderFileDao.selectByParamColumn("gg_fjghwd", "ywxh", Convert.toStr(tenderFileMap.get("zbwj")));
		if (CollectionUtil.isNotEmpty(list)) {
			Long attachmentCode = fileClient.store(
						new File(beanConfig.getFilePath() + list.get(0).get("path") + list.get(0).get("realname")), null);
			tenderFile.setTenderFileContentId(attachmentCode);
			tenderFile.setTenderFileContentHtml(FileUtil.getDocFileContent(beanConfig.getFilePath() + list.get(0).get("path"),
					Convert.toStr(list.get(0).get("realname"))));
		}
		return tenderFile;
	}
	
}
