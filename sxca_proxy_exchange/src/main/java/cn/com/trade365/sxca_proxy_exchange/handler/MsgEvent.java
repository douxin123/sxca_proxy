package cn.com.trade365.sxca_proxy_exchange.handler;

import java.io.Serializable;
import java.util.Map;

/**
 * 事件
 */
public class MsgEvent implements Serializable {
	/**
	 * 投标人信息
	 */
	public final static String BIDDER_MESSAGE_EVENT = "BIDDER_MESSAGE_EVENT";
	/**
	 * 投标人投标中标信息
	 */
	public final static String BIDDER_OFFER_MESSAGE_EVENT = "BIDDER_OFFER_MESSAGE_EVENT";
	/**
	 * 邀请书信息
	 */
	public final static String INVITE_MESSAGE_EVENT = "INVITE_MESSAGE_EVENT";
	/**
	 * 投标人邀请信息
	 */
	public final static String BIDDER_INVITE_MESSAGE_EVENT = "BIDDER_INVITE_MESSAGE_EVENT";
	/**
	 * 开标记录消息
	 */
	public final static String OPENBID_MESSAGE_EVENT = "OPENBID_MESSAGE_EVENT";
	/**
	 * 企业信息
	 */
	public final static String ORG_MESSAGE_EVENT = "ORG_MESSAGE_EVENT";
	/**
	 * 项目信息
	 */
	public final static String PROJECT_MESSAGE_EVENT = "PROJECT_MESSAGE_EVENT";
	/**
	 * 标段信息
	 */
	public final static String SECTION_MESSAGE_EVENT = "SECTION_MESSAGE_EVENT";
	/**
	 * 招标项目信息
	 */
	public final static String TENDER_PROJECT_MESSAGE_EVENT = "TENDER_PROJECT_MESSAGE_EVENT";
	/**
	 * 招标文件信息
	 */
	public final static String TENDER_FILE_MESSAGE_EVENT = "TENDER_FILE_MESSAGE_EVENT";
	/**
	 * 联合体信息
	 */
	public final static String UNION_MESSAGE_EVENT = "UNION_MESSAGE_EVENT";
	/**
	 * 用户信息
	 */
	public final static String USER_MESSAGE_EVENT = "USER_MESSAGE_EVENT";
	/**
	 * 公告公示信息
	 */
	public final static String NOTICE_MESSAGE_EVENT = "NOTICE_MESSAGE_EVENT";
	/**
	 * 招标文件澄清
	 */
	public final static String PROJECT_TENDER_CLARIFY_EVENT="PROJECT_TENDER_CLARIFY_EVENT";
	/**
	 * 质疑投诉
	 */
	public final static String EB_DISSENT_EVENT = "EB_DISSENT_EVENT";
	/**
	 * 专家抽取申请
	 */
	public final static String EP_EXTRACT_APPLY_EVENT = "EP_EXTRACT_APPLY_EVENT";
	/**
	 * 异常情况报告信息
	 */
	public final static String EXCEPTION_MESSAGE_EVENT = "EXCEPTION_MESSAGE_EVENT";

	/**
	 * 合同信息
	 */
	public final static String CONTRACT_MESSAGE_EVENT = "CONTRACT_MESSAGE_EVENT";
	/**
	 * 评标专家
	 */
	public final static String EB_EVALUATION_EXPERT_MESSAGE_EVENT = "EB_EVALUATION_EXPERT_MESSAGE_EVENT";
	/**
	 * 评标报告
	 */
	public final static String EB_EVALUATION_REPORT_MESSAGE_EVENT = "EB_EVALUATION_REPORT_MESSAGE_EVENT";
	/**
	 * 来源平台
	 */
	private String platformCode;
	/**
	 * 事件类型
	 */
	private String event;
	/**
	 * 时间
	 */
	private long timestamp;
	/**
	 * 版本
	 */
	private long version;
	/**
	 * 数据
	 */
	private Map<String, String> data;
	/**
	 * 消息id
	 */
	private Long messageId;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
