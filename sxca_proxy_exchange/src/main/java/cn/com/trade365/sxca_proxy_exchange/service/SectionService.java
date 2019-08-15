package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.platform.project.dto.PrjSectionDto;

public interface SectionService  extends BaseService {

	PrjSectionDto getSectionData(String sectionId) throws ExchangeException;

}
