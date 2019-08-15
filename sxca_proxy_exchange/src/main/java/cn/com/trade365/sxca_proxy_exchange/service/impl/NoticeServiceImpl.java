package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.NoticeDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.PrjNoticeDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.NoticeService;
import cn.com.trade365.store.Client;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 内控与数据中心数据关联 公告相关
 * 
 */
@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	NoticeDao noticeDao;

	@Autowired
	IdRelationService idRelationService;

	@Autowired
	Client fileClient;

	@Autowired
	BeanConfig beanConfig;

	/**
	 * 设置PrjNotice对象id映射
	 *
	 * @param prjNotice
	 * @param tenderProjectIdStr
	 * @param parentNoticeIdStr
	 */
	public void setNoticeIdRelation(PrjNoticeDto prjNotice, String tenderProjectIdStr, String parentNoticeIdStr,
			String createrId, String[] sectionIdArray) throws ExchangeException {
		// 处理 tender_project_id
		Object tradeTenderProjectId = noticeDao.selectById("id", "xm_zbxm", tenderProjectIdStr);
		Long tenderProjectId = idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT,
				Convert.toStr(tradeTenderProjectId));
		prjNotice.setTenderProjectId(tenderProjectId);

		// 处理 原公告id
		if (StringUtils.isNotBlank(parentNoticeIdStr)) {
			Object tradeParentNoticeId = noticeDao.selectById("id", "xm_gg", parentNoticeIdStr);
			Long parentNoticeId = idRelationService.getDataId(ObjectTypeEnum.PROJECTNOTICE,
					Convert.toStr(tradeParentNoticeId));
			prjNotice.setParentNoticeId(parentNoticeId);
		} else {
			prjNotice.setParentNoticeId(0L);
		}

		// 处理 公告creater
		Object tradeCreaterId = noticeDao.selectByParamColumn("data_id", "gg_czyb", "id", createrId);
		prjNotice.setBulletinDutyId(Convert.toLong(tradeCreaterId));

		// 处理公告分包id转换
		List<Long> sectionIdList = idRelationService.getDataIdList(ObjectTypeEnum.SECTION,
				Arrays.asList(sectionIdArray));
		prjNotice.setBidSectionCodes(StringUtils.join(sectionIdList, ","));
	}

	/**
	 * 填充 prjNotice对象
	 *
	 * @param prjNotice
	 * @param dataMap
	 * @throws Exception
	 */
	@Override
	public void setPrjNotice(PrjNoticeDto prjNotice, Map<String, String> dataMap) throws Exception {
		String tradeNoticeId = prjNotice.getSourceId();
		Map<String, Object> tradeNoticeMap = noticeDao.selectById("xm_gg", tradeNoticeId);
		if (CollectionUtil.isEmpty(tradeNoticeMap)) {
			throw new ExchangeException("调用失败，未查询到相关公告信息，公告id=" + tradeNoticeId);
		}

		// 招标文件从分包里查
		String sectionIds = Convert.toStr(tradeNoticeMap.get("xgbbh"));
		String[] sectionIdArray = sectionIds.split(",");

		// 处理id映射
		setNoticeIdRelation(prjNotice, Convert.toStr(tradeNoticeMap.get("zbxh")),
				Convert.toStr(tradeNoticeMap.get("yggxh")), Convert.toStr(tradeNoticeMap.get("creater")),
				sectionIdArray);

		if (Convert.toInt(tradeNoticeMap.get("gglx")) == 6) {
			// 公告类型
			prjNotice.setBulletinType(3);
			// 公告性质
			prjNotice.setBulletinNature(2);
		} else if (Convert.toInt(tradeNoticeMap.get("gglx")) == 7) {
			// 公告类型
			prjNotice.setBulletinType(4);
			// 公告性质
			prjNotice.setBulletinNature(2);
		} else if (Convert.toInt(tradeNoticeMap.get("gglx")) == 9) {
			if (Convert.toInt(tradeNoticeMap.get("lx")) == 2) {
				// 公告类型
				prjNotice.setBulletinType(2);
			} else {
				// 公告类型
				prjNotice.setBulletinType(1);
			}
			// 公告性质
			prjNotice.setBulletinNature(2);
		} else {
			// 公告类型
			if (Convert.toInt(tradeNoticeMap.get("lx")) == 2) {
				prjNotice.setBulletinType(2);
			} else {
				prjNotice.setBulletinType(Convert.toInt(tradeNoticeMap.get("gglx")));
			}
			// 公告性质
			prjNotice.setBulletinNature(1);
		}
		// 招标文件获取方法
		prjNotice.setTenderDocGetMethod("线上");
		// 投标文件递交方法
		prjNotice.setBidDocReferMethod("线上");
		// 公告名称
		prjNotice.setBulletinName(Convert.toStr(tradeNoticeMap.get("ggmc")));
		// 公告发布时间
		String bulletinIssueTime = Convert.toStr(tradeNoticeMap.get("fbsj"));
		if (StringUtils.isNotBlank(bulletinIssueTime)){
			if (bulletinIssueTime.length() == 12) {
				bulletinIssueTime += "00";
			}
			prjNotice.setBulletinIssueTime(DateUtil.parse(bulletinIssueTime));
		}else{
			prjNotice.setBulletinIssueTime(DateUtil.date());
		}

		// 公告结束时间
		prjNotice.setBulletinEndTime(DateUtil.parse(Convert.toStr(tradeNoticeMap.get("ggjssj"))));
		// 发布媒体
		List<String> tradeMediaNameList = noticeDao.getMediaName(tradeNoticeId);
		prjNotice.setBulletinMedia(StringUtils.join(tradeMediaNameList, ","));
		// 起草时间
		prjNotice.setCreateTime(DateUtil.parse(Convert.toStr(tradeNoticeMap.get("qcsj"))));
		Object projectId = noticeDao.selectByParamColumn("zbxh", "xm_wjfb", "bxh", sectionIdArray[0]);
		// 公开招标 邀请招标
		Object tradeTenderMode = noticeDao.selectByParamColumn("cgfs", "xm_zbxm", "id", Convert.toStr(projectId));
		prjNotice.setTenderMode(Convert.toInt(tradeTenderMode));
		List<Map<String, Object>> tradeDocumentMapList = noticeDao.getTenderFile(Convert.toStr(projectId),
				Convert.toInt(tradeNoticeMap.get("lx")));

		if (CollectionUtil.isNotEmpty(tradeDocumentMapList)) {
			Map<String, Object> tradeDocumentMap = tradeDocumentMapList.get(0);
			// 招标文件获取开始时间
			prjNotice.setTenderDocGetStartTime(DateUtil.parse(Convert.toStr(tradeDocumentMap.get("qssj"))));
			// 招标文件获取截止时间
			prjNotice.setTenderDocDeadLine(DateUtil.parse(Convert.toStr(tradeDocumentMap.get("jssj"))));
			// 投标文件递交截止时间
			prjNotice.setBidDocReferEndTime(DateUtil.parse(Convert.toStr(tradeDocumentMap.get("kbsj"))));
		}
		// 上传文件doc
		Map<String, Object> attachment = noticeDao.selectById("gg_fjghwd", Convert.toStr(tradeNoticeMap.get("ggnr")));
		if (CollectionUtil.isNotEmpty(attachment)) {
			File docFile = new File(beanConfig.getFilePath() + attachment.get("path") + attachment.get("realname"));
			Long attachmentDocId = fileClient.store(docFile, null);
			prjNotice.setBulletinContentId(attachmentDocId);
			// html
			String htmPath = docFile.getPath().replace(".docx",".htm").replace(".doc",".htm");
			String htmlContent = cn.hutool.core.io.FileUtil.readString(new File(htmPath), "GBK");
			if (htmlContent.indexOf("charset=utf-8") != -1) {
                htmlContent = cn.hutool.core.io.FileUtil.readString(new File(htmPath), "UTF-8");
            } else {
				htmlContent = new String(htmlContent.getBytes(),"UTF-8");
			}
			log.info("----------------------------------html路径" + htmPath);
			log.info("----------------------------------html内容" + htmlContent);
			prjNotice.setBullentinContentHtml(htmlContent);
		}
		// 上传文件pdf
		attachment = noticeDao.selectById("gg_fjghwd", Convert.toStr(tradeNoticeMap.get("ggpdfwj")));
		if (CollectionUtil.isNotEmpty(attachment)) {
			Long attachmentPdfId = fileClient.store(
					new File(beanConfig.getFilePath() + attachment.get("path") + attachment.get("realname")), null);
			prjNotice.setAttachmentId(attachmentPdfId);
		}
	}

}
