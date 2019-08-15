package cn.com.trade365.sxca_proxy_exchange.service;

import cn.com.trade365.sxca_proxy_exchange.core.BaseService;
import cn.com.trade365.platform.project.dto.EbDissentDto;

public interface EbDissentService  extends BaseService {

    /**
     * 组装质疑对象
     * @param zzid 质疑ID
     * @return EbDissent
     */
    public EbDissentDto getEbDissent(String zzid);

    /**
     * 获取项目ID
     * @param zyid 质疑
     * @return String
     */
    public  String getXMID(String zyid);
}
