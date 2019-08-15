package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.PrjNoticeDto;

import java.util.Map;

/**
 * 内控与数据中心数据关联 公告相关
 */
public interface NoticeService  extends BaseService {

//	/**
//	 * 设置PrjNotice对象id映射
//	 *
//	 * @param prjNotice
//	 * @param tenderProjectIdStr
//	 * @param parentNoticeIdStr
//	 */
//	void setNoticeIdRelation(PrjNotice prjNotice, String tenderProjectIdStr, String parentNoticeIdStr, String createrId);

	/**
	 * 填充 prjNotice对象
	 * 
	 * @param prjNotice
	 * @param dataMap
	 */
	void setPrjNotice(PrjNoticeDto prjNotice, Map<String, String> dataMap) throws Exception;
}
