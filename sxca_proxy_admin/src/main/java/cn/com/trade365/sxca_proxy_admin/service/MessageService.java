package cn.com.trade365.sxca_proxy_admin.service;

import cn.com.trade365.sxca_proxy_admin.entity.MessageEntity;

import java.util.List;

/**
 * @author lhl
 */
public interface MessageService {

    /**
     *
     * @param messageEntity
     * @return
     */
    List<MessageEntity> findAll(MessageEntity messageEntity);

    /**
     *
     * @param id
     * @return
     */
    String  findExceptionById(long id);

    /**
     *
     * @param id
     * @return
     */
    MessageEntity  findById(long id);

    /**
     * 根据id，更新重试次数
     * @param id
     * @return
     */
    Boolean updateRetryById(long id, int retry);
}
