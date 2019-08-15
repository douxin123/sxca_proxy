package cn.com.trade365.sxca_proxy_admin.dao;

import cn.com.trade365.sxca_proxy_admin.entity.MessageEntity;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author :lhl
 * @create :2018-11-13 13:44
 */
public class DynaticSqlProvider {

    public String selectWhitParamSql(MessageEntity messageEntity) {
        return new SQL() {
            {
                SELECT("*");
                FROM("ex_message");
                if (messageEntity.getStatus() != null) {
                    WHERE("status=#{status}");
                }
                if (messageEntity.getId() != null) {
                    WHERE("id=#{id}");
                }
                if (messageEntity.getException() != null) {
                    WHERE("exception like CONCAT('%',#{exception},'%')");
                }
                if (messageEntity.getData() != null) {
                    WHERE("msg_data like CONCAT('%',#{data},'%')");
                }
            }
        }.toString();
    }

}
