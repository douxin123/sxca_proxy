package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.sxca_proxy_exchange.entity.MessageEntity;

import java.util.List;
import java.util.Map;


public interface MessageService  extends BaseService {

    long insertMsg(MessageEntity messageEntity);
    int updateMsg(MessageEntity messageEntity);
    int updateRetry(MessageEntity messageEntity);
    int findAndIncreatmentRetry(Long id);

    /**
      * @Description: 获取某个时间段所有异常消息体的集合
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    List<Map<String,Object>> getExceptionMessages();

    /**
     * 根据id，更新重试次数
     * @param id
     * @return
     */
    Boolean updateRetryById(long id, int retry);
}
