package cn.com.trade365.sxca_proxy_exchange.dao.impl;

import cn.com.trade365.sxca_proxy_exchange.dao.MessageDao;
import cn.com.trade365.sxca_proxy_exchange.entity.MessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


@Component
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(MessageDaoImpl.class);

    @Override
    public int update(MessageEntity messageEntity) {

        String sql = "update ex_message set status=?,finish_time=?,`exception`=? where id=?";
        return jdbcTemplate.update(sql, messageEntity.getStatus(), messageEntity.getFinishTime(), messageEntity.getException(), messageEntity.getId());
    }

    @Override
    public int updateRetry(MessageEntity messageEntity) {
        String sql = "update ex_message set status=0,`exception`=null, retry=? where id=?";
        return jdbcTemplate.update(sql, messageEntity.getRetry(), messageEntity.getId());

    }

    @Override
    public int findRetry(Long id) {
        String sql = "select retry from ex_message where id=?";
        Object retry = jdbcTemplate.queryForMap(sql, id).get("retry");
        return retry == null ? 0 : Integer.parseInt(retry.toString());
    }

    @Override
    public int save(MessageEntity messageEntity) {
        String sql = "insert into ex_message(msg_data,status,accept_time) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, messageEntity.getMsgData());
                ps.setInt(2, messageEntity.getStatus());
                ps.setString(3, messageEntity.getAcceptTime().toString());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }
    
    /** 
      * @Description: 获取 近一天内，处理失败，重试次数不大于5的消息集合
      * @Param:  
      * @return:  
      * @Author: JianbingZhang 
      * @Date: 
      */ 
    @Override
    public List<Map<String,Object>> getExceptionMessages(){
        String sql = " SELECT * FROM `ex_message`  " +
                "WHERE STATUS=3 AND retry<=5 AND  TO_DAYS(accept_time) = TO_DAYS(NOW()) ORDER BY  accept_time ASC  ";
        return jdbcTemplate.queryForList(sql);
    }

    /**
      * @Description: 消息重发，重试次数+1
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    @Override
    public boolean updateRetryById(long id,int retry){
        String sql = "update ex_message set retry=? where id=?";
        return jdbcTemplate.update(sql, retry+1, id)>0;
    }
}
