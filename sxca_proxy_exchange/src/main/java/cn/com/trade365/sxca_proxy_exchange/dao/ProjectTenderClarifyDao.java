package cn.com.trade365.sxca_proxy_exchange.dao;


import java.util.List;

public interface ProjectTenderClarifyDao {

    List getProjectTenderClarifyMap(String id) throws  Exception;
    String  getAttachmentId(String id) throws  Exception;
}
