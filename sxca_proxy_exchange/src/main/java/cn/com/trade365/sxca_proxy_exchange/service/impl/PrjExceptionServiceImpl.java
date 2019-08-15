package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.dao.PrjExceptionDao;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjExceptionDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.PrjExceptionService;
import cn.com.trade365.sxca_proxy_exchange.utils.ConvertUtils;
import cn.com.trade365.store.Client;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author: JianBingZhang
 * @Date: 2018/11/8 15:49
 * @Version 1.0
 */
@Service("prjExceptionService")
public class PrjExceptionServiceImpl implements PrjExceptionService {

    @Autowired
    private PrjExceptionDao prjExceptionDao;

    @Autowired
    IdRelationService idRelationService;

    @Autowired
    RestComponent restComponent;

    @Autowired
    Client fileClient;

    @Autowired
    BeanConfig beanConfig;


    @Override
    public PrjExceptionDto getExceptionData(MsgEvent msgEvent) throws ExchangeException{
        String sectionId = msgEvent.getData().get("sectionId");
        Map<String,Object> excMap = prjExceptionDao.queryPrjExcInfo(sectionId);
        PrjExceptionDto exception = new PrjExceptionDto();
        exception.setTenderProjectId(idRelationService.getDataId(ObjectTypeEnum.TENDER_PROJECT,ConvertUtils.convert2str(excMap.get("ZBXH"))));
        exception.setSectionId(idRelationService.getDataId(ObjectTypeEnum.SECTION,ConvertUtils.convert2str(excMap.get("BXH"))));
        exception.setExceptionName(ConvertUtils.convert2str(excMap.get("XMMC")) + ConvertUtils.convert2str(excMap.get("BYWBH")) + "异常报告");
        exception.setExceptionInfor(ConvertUtils.convert2str(excMap.get("YCMS")));
        exception.setApprovalResult(this.getStatusName(ConvertUtils.convert2str(excMap.get("ZT"))));
        exception.setApprovalTime(ConvertUtils.convert2Date(excMap.get("SHSJ")));
        // 数据来源id
        exception.setSourceId(sectionId);
        // 数据来源平台
        exception.setPlatformCode(msgEvent.getPlatformCode());
        // 附件关联标识号以项文逗号隔开
        // 上传文件doc
        if(excMap.get("FJ")!=null){
            List<Map<String, Object>> attachment =  prjExceptionDao.selectByParamColumn("gg_fjghwd","YWXH", ConvertUtils.convert2str(excMap.get("FJ")));
            if (CollectionUtil.isNotEmpty(attachment)) {
                Long attachmentDocId = fileClient.store(
                        new File(beanConfig.getFilePath() + attachment.get(0).get("path") + attachment.get(0).get("realname")), null);
                exception.setAttachmentCode(ConvertUtils.convert2str(attachmentDocId));
            }else{
                exception.setAttachmentCode("");
            }
        }
        return exception;
    }

    /**
      * @Description: 获取异常记录状态名称
      * @Param:
      * @return:
      * @Author: JianbingZhang
      * @Date:
      */
    public String getStatusName(String zt){
        String statusName = "";
        if(StringUtils.equals(zt,"-2")){
            statusName = "验证未通过";
        }else if(StringUtils.equals(zt,"-1")){
            statusName = "审批未通过";
        }else if(StringUtils.equals(zt,"0")){
            statusName = "编辑中";
        }else if(StringUtils.equals(zt,"1")){
            statusName = "审批中";
        }else if(StringUtils.equals(zt,"2")){
            statusName = "审批通过";
        }else if(StringUtils.equals(zt,"4")){
            statusName = "已生效";
        }
        return statusName;
    }
}

