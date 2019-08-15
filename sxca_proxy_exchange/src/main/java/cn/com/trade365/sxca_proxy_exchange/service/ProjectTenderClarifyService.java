package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.platform.project.dto.PrjTenderClarifyDto;

import java.util.List;


public interface ProjectTenderClarifyService {


    List<PrjTenderClarifyDto> getProjectTenderClarify(String id) throws  Exception;
}
