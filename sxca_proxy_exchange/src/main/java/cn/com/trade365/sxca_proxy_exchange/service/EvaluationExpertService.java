package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.EbEvaluationExpertDto;

import java.util.List;

/**
 * 内控与数据中心数据关联 评标专家
 */
public interface EvaluationExpertService extends BaseService {

    /**
     * 拼接评标专家对象
     * @param evaluationExpertlist
     */
    void setEvaluationExpert(List<EbEvaluationExpertDto> evaluationExpertlist, String evaluationExpertGroupId) throws ExchangeException;
}
