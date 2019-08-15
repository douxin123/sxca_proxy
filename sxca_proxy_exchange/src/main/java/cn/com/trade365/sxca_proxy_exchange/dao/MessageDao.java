package cn.com.trade365.sxca_proxy_exchange.dao;


import cn.com.trade365.sxca_proxy_exchange.entity.MessageEntity;

import java.util.List;
import java.util.Map;

public interface MessageDao {


    int save(MessageEntity messageEntity);

    int update(MessageEntity messageEntity);

    int updateRetry(MessageEntity messageEntity);

    int findRetry(Long id);

    List<Map<String,Object>> getExceptionMessages();

    boolean updateRetryById(long id, int retry);
}
