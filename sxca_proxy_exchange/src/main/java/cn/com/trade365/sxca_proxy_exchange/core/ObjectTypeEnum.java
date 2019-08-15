package cn.com.trade365.sxca_proxy_exchange.core;

/**
 * 数据类型
 */
public enum  ObjectTypeEnum {

    PROJECT("project"),//跟踪项目
    TENDER_PROJECT("tenderProject"),//招标项目
    SECTION("section"),//标包
    TENDER_FILE("tenderFile"),//招标文件
    BIDBIDDER("bidBidder"),//
    BIDBIDDERATTACH("bidBidderAttach"),
    BIDBIDDERINVITE("bidBidderInvite"),
    BIDINVITE("bidInvite"),
    OPENBID("openBid"),//开标记录
    BIDDEROFFER("bidderOffer"),//投标人投标数据
    CTCONTRACT("ctContract"),//合同
    PRJEXCEPTION("prjException"),//异常情况报告
    PROJECTNOTICE("projectNotice"),
    USER("userInfo"),//用户信息
    ORG("orgInfo"),//机构
    UNORG("unOrg"),//联合体
    USERORG("userOrg"),//用户机构
	EB_EVALUATION_REPORT("ebEvaluationReport"),//评标报告
	EB_EVALUATION_EXPERT("ebEvaluationExpert"),//评标专家
    EB_DESSINT("ebDissent"),//质疑投诉
    EPAVOIDUNIT("epAvoidUnit"),//回避单位名单
    EPEXTRACTAPPLY("epExtractApply"),//专家抽取申请
    PROJECTETENDERCLARIFY("projectTenderClarify");//招标澄清文件


    ObjectTypeEnum(String code){
        this.code=code;
    }
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
