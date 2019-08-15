package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.PrjTenderProjectDto;

public interface TenderProjectService  extends BaseService {

	PrjTenderProjectDto getTenderProjectData(String tenderProjectId) throws ExchangeException;

}
