package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.PrjProjectDto;

public interface ProjectService  extends BaseService {

	PrjProjectDto getProjectData(String projectId);

}
