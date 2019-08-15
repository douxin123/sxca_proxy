package cn.com.trade365.sxca_proxy_exchange.dao;


import java.util.Map;

/**
 * 合同信息持久层接口
 * @author zhangjianbing
 * 
 */
public interface ContractDao extends BaseDao {
    /** 
      * @Description: 查询招标代理机构信息 
      * @Param:  
      * @return:  
      * @Author: JianbingZhang 
      * @Date: 
      */ 
    Map<String,Object> queryZbJg(String projectId);
    /** 
      * @Description: 根据id 查询合同信息 
      * @Param:  
      * @return:  
      * @Author: JianbingZhang 
      * @Date: 
      */ 
    Map<String,Object> queryById(String projectId);
}

