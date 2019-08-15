package cn.com.trade365.sxca_proxy_exchange.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * URL 业务常量
 * 
 * @Author weichunjie
 */
@Component
public class Repositories {
	/**
	 * 公共前缀
	 */
	public static String COMM_PREFIX;
	/**
	 * 格式 业务标识_操作_URL="url"
	 * user 推送RUL
	 */
	public static String USER_SAVE_URL = "user";

	/**
	 * project 项目 推送URL
	 */
	public static String PROJECT_SAVE_URL = "project";

	/**
	 * tenderProject 招标项目 推送URL
	 */
	public static String TENDER_PROJECT_SAVE_URL = "tenderProject";

	/**
	 * tenderFile 招标文件 推送URL
	 */
	public static String TENDER_FILE_SAVE_URL = "tenderFile";

	/**
	 * section 分包 推送URL
	 */
	public static String SECTION_SAVE_URL = "section";
	/**
	 * 机构信息保存
	 */
	public static  String ORG_SAVE_URL="organization";
	/**
	 * 用户机构关联信息
	 */
	public static  String USER_ORG_SAVE_URL="userOrg";
	/**
	 * BidBidder 推送URL
	 */
	public static  String BID_BIDDER_SAVE_URL="bidBidder";
	/**
	 * BidInvite 推送URL
	 */
	public static  String BID_INVITE_SAVE_URL="bidInvite";
	/**
	 * BidBidderInvite 推送URL
	 */
	public static  String BID_BIDDER_INVITE_SAVE_URL="bidBidderInvite";
	/**
	 * BidOpenbid 推送URL
	 */
	public static String BIDOPENBID_SAVE_URL = "openbid";
	/**
	 * BidBidderOffer 推送URL
	 */
	public static String BIDBIDDEROFFER_SAVE_URL = "bidderOffer";
	/**
	 * notice 推送URL
	 */
	public static String NOTICE_SAVE_URL = "notice";
	/**
	 * projectTenderClarify 推送URL
	 */
	public static String PROJECT_TENDER_CLARIFY_URL = "tenderClarify";
	/**
	 * ctContract 推送URL
	 */
	public static String CTCONTRACT_SAVE_URL = "ctContract";
	/**
	 * prjException 推送URL
	 */
	public static String PRJEXCEPTION_SAVE_URL = "prjException";
	/**
	 * ebEvaluationReport 推送URL
	 */
	public static String EB_EVALUATION_REPORT_SAVE_URL = "ebEvaluationReport";
	/**
	 * ebEvaluationExpert 推送URL
	 */
	public static String EB_EVALUATION_EXPERT_SAVE_URL = "ebEvaluationExpert";
	/**
	 * 质疑投诉
	 */
	public static  String EBDISSENT_SAVE_URL="ebDissent";
	/**
	 * epAvoidUnit推送URL
	 */
	public static String EP_AVOID_UNIT_SAVE_URL = "epAvoidUnit";
	/**
	 * epExtractApply推送URL
	 */
	public static String EP_EXTRACT_APPLY_SAVE_URL = "epExtractApply";

	/**
	 * 获取restUrl
	 * 
	 * @param repositoryUrl
	 *            Repositories对应的URL
	 * @return String
	 */
	public static String getUrl(String repositoryUrl) {
		return Repositories.COMM_PREFIX + repositoryUrl;
	}

	/**
	 * 公共前缀
	 * 
	 * @param prefix
	 */
	@Value("${rest.url}")
	public void setCOMM_PREFIX(String prefix) {
		this.COMM_PREFIX = prefix;
	}
}
