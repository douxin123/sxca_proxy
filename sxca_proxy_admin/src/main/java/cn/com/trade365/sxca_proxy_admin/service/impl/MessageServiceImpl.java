package cn.com.trade365.sxca_proxy_admin.service.impl;

import cn.com.trade365.sxca_proxy_admin.dao.MessageMapper;
import cn.com.trade365.sxca_proxy_admin.entity.MessageEntity;
import cn.com.trade365.sxca_proxy_admin.service.MessageService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author :lhl
 * @create :2018-11-07 17:24
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Boolean updateRetryById(long id,int retry) {
        return messageMapper.updateRetryById(id,retry+1);
    }

    @Override
    public MessageEntity findById(long id) {
        return messageMapper.findById(id);
    }

    @Override
    public String findExceptionById(long id) {
        return messageMapper.findExceptionById(id).getException();
    }

    private static final String ACCEPT_TIME = "acceptTime";
    private static final String COLUMN_ACCEPT_TIME = "accept_time";
    private static final String FINISH_TIME = "finishTime";
    private static final String COLUMN_FINISH_TIME = "finishTime";


    @Override
    public List<MessageEntity> findAll(MessageEntity messageEntity) {
        PageHelper.offsetPage(messageEntity.getOffset(), messageEntity.getLimit());
        StringBuilder sb = new StringBuilder();
        if (ACCEPT_TIME.equals(messageEntity.getSort())) {
            sb.append(COLUMN_ACCEPT_TIME);
        } else if (FINISH_TIME.equals(messageEntity.getSort())) {
            sb.append(COLUMN_FINISH_TIME);
        }
        if (!StringUtils.isEmpty(messageEntity.getOrder())) {
            sb.append("\t");
            sb.append(messageEntity.getOrder());
        }
        PageHelper.orderBy(sb.toString());
        List<MessageEntity> list = messageMapper.findAll(messageEntity);
        list.forEach(messageEntity1 -> {
            if (!StringUtils.isEmpty(messageEntity1.getException())) {
                messageEntity1.setSmallException(messageEntity1.getException().substring(0, 35));
            }
        });
        return list;
    }
}
