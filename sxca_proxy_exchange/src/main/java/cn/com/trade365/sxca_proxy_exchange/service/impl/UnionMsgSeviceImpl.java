package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.service.UnionMsgSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 联合体
 * @Author weichunjie
 */
@Service("unionMsgSevice")
public class UnionMsgSeviceImpl implements UnionMsgSevice {

    @Autowired
    private JdbcTemplate jdbcTemplate;

}
