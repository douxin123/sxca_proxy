package cn.com.trade365.sxca_proxy_admin.dao;

import cn.com.trade365.sxca_proxy_admin.entity.MessageEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 根据传来的消息实体分页查询所有消息
     * @param messageEntity
     * @return
     */
    @SelectProvider(type = DynaticSqlProvider.class, method = "selectWhitParamSql")
    @Results(id = "messageResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "data", column = "msg_data"),
            @Result(property = "status", column = "status"),
            @Result(property = "exception", column = "exception"),
            @Result(property = "acceptTime", column = "accept_time"),
            @Result(property = "finishTime", column = "finish_time"),
            @Result(property = "retry", column = "retry"),
    })
    List<MessageEntity> findAll(MessageEntity messageEntity);

    /**
     * 根据id查询异常信息
     * @param id
     * @return
     */
    @Select("select exception from ex_message where id=#{id}")
    @ResultMap("messageResultMap")
    MessageEntity findExceptionById(long id);

    /**
     * 根据消息id查询消息实体类
     * @param id
     * @return
     */
    @Select("select * from ex_message where id=#{id}")
    @ResultMap("messageResultMap")
    MessageEntity findById(long id);

    /**
     * 根据id更新retry
     * @param id
     * @return
     */
    @Update("update ex_message set retry=#{retry} where id=#{id}")
    Boolean updateRetryById(@Param("id") long id, @Param("retry") int retry);
}
