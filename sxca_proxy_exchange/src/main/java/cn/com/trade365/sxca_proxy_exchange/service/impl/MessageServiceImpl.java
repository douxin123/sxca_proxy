package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.MessageDao;
import cn.com.trade365.sxca_proxy_exchange.entity.MessageEntity;
import cn.com.trade365.sxca_proxy_exchange.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author :lhl
 * @create :2018-11-06 13:49
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public int  updateMsg(MessageEntity messageEntity) {
       return messageDao.update(messageEntity);
    }

    @Override
    public int updateRetry(MessageEntity messageEntity) {
        return messageDao.updateRetry(messageEntity);
    }

    @Override
    public long insertMsg(MessageEntity messageEntity) {
        long id = messageDao.save( messageEntity);
        messageEntity.setId(id);
        return id;
    }

    @Override
    public int findAndIncreatmentRetry(Long id) {
        return messageDao.findRetry(id)+1;
    }

    @Override
    public List<Map<String,Object>> getExceptionMessages(){
        return messageDao.getExceptionMessages();
    }

    @Override
    public Boolean updateRetryById(long id,int retry) {
        return messageDao.updateRetryById(id,retry);
    }
}
