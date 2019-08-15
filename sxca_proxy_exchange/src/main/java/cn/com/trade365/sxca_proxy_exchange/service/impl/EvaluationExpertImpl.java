package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.dao.EvaluationExpertDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.EbEvaluationExpertDto;
import cn.com.trade365.sxca_proxy_exchange.service.EvaluationExpertService;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 内控与数据中心数据关联 评标专家
 *
 * @author admin
 * @since 2018-11-09
 */
@Service
public class EvaluationExpertImpl implements EvaluationExpertService {

	@Autowired
	EvaluationExpertDao evaluationExpertDao;

	@Autowired
	IdRelationService idRelationService;

	@Override
	public void setEvaluationExpert(List<EbEvaluationExpertDto> evaluationExpertList, String evaluationExpertGroupId)
			throws ExchangeException {
		List<Map<String, Object>> evaluationExpertMapList = evaluationExpertDao.selectByParamColumn("zb_pwhcy", "sqxh",
				evaluationExpertGroupId);
		if (CollectionUtil.isEmpty(evaluationExpertMapList)) {
			throw new ExchangeException("调用失败，未查询到相关专家信息，专家组id=" + evaluationExpertGroupId);
		}
		List<Map<String, Object>> tradeSectionIdList = evaluationExpertDao.selectByParamColumn("zb_zjsq_fb", "ZJSQBH",
				evaluationExpertGroupId);

		for (Map<String, Object> map : tradeSectionIdList) {
			String tradeSecitonId = Convert.toStr(map.get("bxh"));
			String tradeManagerId = Convert.toStr(map.get("zzbh"));
			String tradeProjectId = Convert.toStr(map.get("zbxh"));

			for (Map<String, Object> evaluationExpertMap : evaluationExpertMapList) {
				Long dataSectionId = idRelationService.getDataId(ObjectTypeEnum.SECTION, tradeSecitonId);
				Long dataProjectId = idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT, tradeProjectId);

				EbEvaluationExpertDto ebEvaluationExpert = new EbEvaluationExpertDto();
				// 招标项目编号
				ebEvaluationExpert.setTenderProjectId(dataProjectId);
				// 包id
				ebEvaluationExpert.setSectionId(dataSectionId);
				// 专家类别
				ebEvaluationExpert.setExpertType(getExpertType(Convert.toInt(evaluationExpertMap.get("rylx"))));
				// 专家姓名
				ebEvaluationExpert.setExpertName(Convert.toStr(evaluationExpertMap.get("zjxm")));
				// 身份证件号
				ebEvaluationExpert.setIdCard(Convert.toStr(evaluationExpertMap.get("zjhm")));
				// 身份证件类型
				ebEvaluationExpert.setIdCardType(01);
				// 签到时间
				if (StringUtils.isNotBlank(Convert.toStr(evaluationExpertMap.get("qdsj")))) {
					ebEvaluationExpert
							.setExpertCheckInTime(DateUtil.parse(Convert.toStr(evaluationExpertMap.get("qdsj"))));
				} else {
					ebEvaluationExpert.setExpertCheckInTime(new Date());
				}
				// 是否是组长
				String temp = Convert.toStr(evaluationExpertMap.get("id"));
				if (null != tradeManagerId && null != temp && tradeManagerId.equals(temp)) {
					ebEvaluationExpert.setIsChairMan(1);
				} else {
					ebEvaluationExpert.setIsChairMan(0);
				}

				ebEvaluationExpert.setSourceId(Convert.toStr(evaluationExpertMap.get("id")));

				evaluationExpertList.add(ebEvaluationExpert);
			}
		}

	}

	/**
	 * rylx=2值为1[在库专家]，=1值为2采购人代表
	 * 
	 * @param type
	 * @return
	 */
	private Integer getExpertType(int type) {
		return type == 1 ? 2 : 1;
	}

}
