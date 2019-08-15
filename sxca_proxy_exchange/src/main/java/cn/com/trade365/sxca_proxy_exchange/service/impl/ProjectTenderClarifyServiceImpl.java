package cn.com.trade365.sxca_proxy_exchange.service.impl;

import cn.com.trade365.sxca_proxy_exchange.config.BeanConfig;
import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.dao.*;
import cn.com.trade365.sxca_proxy_exchange.entity.RelationEntity;
import cn.com.trade365.platform.project.dto.PrjTenderClarifyDto;
import cn.com.trade365.sxca_proxy_exchange.service.ProjectTenderClarifyService;
import cn.com.trade365.sxca_proxy_exchange.utils.InitPrjClarify;
import cn.com.trade365.store.Client;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author :lhl
 * @create :2018-11-14 15:45
 */
@Service
public class ProjectTenderClarifyServiceImpl implements ProjectTenderClarifyService {


    @Autowired
    private ProjectTenderClarifyDao projectTenderClarifyDao;

    @Autowired
    private UnstructuredDocumentDao unstructuredDocumentDao;
    @Autowired
    private Client fileStoreClient;
    @Autowired
    RestComponent restComponent;
    @Autowired
    IdRelationDao idRelationDao;
    @Autowired
    ProcurementDocumentDao procurementDocumentDao;
    @Autowired
    BeanConfig beanConfig;

    /**
     * @param id 内控澄清id
     * @return
     * @throws Exception
     */
    @Override
    public List<PrjTenderClarifyDto> getProjectTenderClarify(String id) throws Exception {
        //获取变更澄清链接地址
        String attachmentId = projectTenderClarifyDao.getAttachmentId(id);
        //根据变更澄清链接地址查询非结构化文档表
        List<Map<String, Object>> list = unstructuredDocumentDao.queryList(attachmentId);

        StringBuilder ids = new StringBuilder();
        //根据查询的文件信息上传oss,并生成ids字符串
        if (list.size() > 0) {
            list.forEach(entity -> {
                ids.append(fileStoreClient.store(new File(beanConfig.getFilePath() + entity.get("path") + "/" + entity.get("realName")), null));
                ids.append(",");
            });
        }
        String idList = "";
        //将ids截取掉最后的,
        if (ids.length() != 0) {
            idList = ids.toString().substring(0, ids.length() - 1);
        }
        //根据id查询变更澄清需要推送的数据
        List<Map<String, Object>> maps = projectTenderClarifyDao.getProjectTenderClarifyMap(id);
        List<PrjTenderClarifyDto> prjTenderClarifyDtos = new ArrayList<>();
        //循环过滤别名结尾时Time的数据,将14位long值转成DateTime
        for (Map<String, Object> map : maps
                ) {
            map.forEach((k, v) -> {
                if (k != null && v != null && k.contains("Time")) {
                    DateTime parse = DateUtil.parse(v.toString(), "yyyyMMddHHmmss");
                    map.put(k, parse);
                }
            });


            map.put("submitTime", map.get("clarifyTime"));
            if (ObjectUtil.isNull(map.get("openAddress"))) {
                map.put("openAddress", map.get("preOpenAddress"));
            }
            Object bidOpenTime = map.get("bidOpenTime");
            if (ObjectUtil.isNull(bidOpenTime)) {
                Object preBidOpenTime = map.get("preBidOpenTime");
                map.put("bidOpenTime", preBidOpenTime);
            }
            String tenderFileId = map.get("tenderFileId").toString();
            //根据文件分包查询关系表数据dataId
            RelationEntity relationEntity = idRelationDao.getRelationByTradeId(ObjectTypeEnum.TENDER_FILE.getCode(), tenderFileId);
            //查询结果不是空则将map中的tenderFileId 的值进行替换
            if (ObjectUtil.isNotNull(relationEntity)) {
                map.put("tenderFileId", relationEntity.getDataId());
            } else {
                throw new RuntimeException("招标文件不存在");
            }
            //map转换对象
            PrjTenderClarifyDto prjTenderClarifyDto = JSON.toJavaObject(new JSONObject(map), PrjTenderClarifyDto.class);
            //设置来源id时文件分包的id
            prjTenderClarifyDto.setSourceId(tenderFileId);
            //设置附件id列表
            prjTenderClarifyDto.setAttachmentCode(idList);
            //如果isPostpone和charifyContent是空,进行初始化
            InitPrjClarify.initPrjClarifyField(prjTenderClarifyDto);
            //加到list数组中
            prjTenderClarifyDtos.add(prjTenderClarifyDto);
        }
        return prjTenderClarifyDtos;

    }


}