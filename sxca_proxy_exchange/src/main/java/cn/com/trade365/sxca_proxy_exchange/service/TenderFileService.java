package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.PrjTenderFileDto;

public interface TenderFileService  extends BaseService {

	PrjTenderFileDto getTenderFileData(String tenderFileId)  throws Exception;

}
