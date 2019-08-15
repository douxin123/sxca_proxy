package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;


/**
 * 公共service  包含公用一些方法
 * @Author weichunjie
 */
@Service("commService")
public class CommonServiceImpl implements CommonService {

    @Autowired
    private NamedParameterJdbcTemplate dao;


}
