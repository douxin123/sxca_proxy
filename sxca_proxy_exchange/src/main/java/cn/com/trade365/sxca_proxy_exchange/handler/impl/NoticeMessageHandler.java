package cn.com.trade365.sxca_proxy_exchange.handler.impl;

import cn.com.trade365.sxca_proxy_exchange.core.ObjectTypeEnum;
import cn.com.trade365.sxca_proxy_exchange.core.Repositories;
import cn.com.trade365.sxca_proxy_exchange.core.RestComponent;
import cn.com.trade365.sxca_proxy_exchange.entity.ResultData;
import cn.com.trade365.sxca_proxy_exchange.exception.ExchangeException;
import cn.com.trade365.sxca_proxy_exchange.handler.MessageHandler;
import cn.com.trade365.sxca_proxy_exchange.handler.MsgEvent;
import cn.com.trade365.platform.project.dto.PrjNoticeDto;
import cn.com.trade365.sxca_proxy_exchange.service.IdRelationService;
import cn.com.trade365.sxca_proxy_exchange.service.NoticeService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 公告公示同步
 */
@Component
public class NoticeMessageHandler implements MessageHandler {

	@Autowired
	IdRelationService idRelationService;

	@Autowired
	RestComponent restComponent;

	@Autowired
	NoticeService noticeService;
	
    private static Logger logger= LoggerFactory.getLogger(ProjectMessageHandler.class);

	@Override
	public void handleMessage(MsgEvent msgEvent) throws ExchangeException {
		Map<String, String> dataMap = msgEvent.getData();
		boolean isExit = idRelationService.isExitDataId(ObjectTypeEnum.PROJECTNOTICE, dataMap.get("tradeNoticeId"));
		if (isExit) {
            logger.warn("重复推送公告信息["+dataMap.get("tradeNoticeId")+"]");
		}
		try {
			PrjNoticeDto prjNotice = new PrjNoticeDto();
			prjNotice.setPlatformCode(msgEvent.getPlatformCode());
			prjNotice.setSourceId(dataMap.get("tradeNoticeId"));
			noticeService.setPrjNotice(prjNotice, dataMap);

			ResultData<Long> resultData = restComponent.post(Repositories.COMM_PREFIX + Repositories.NOTICE_SAVE_URL,
					prjNotice, new ParameterizedTypeReference<ResultData<Long>>() {
					});
			if (StringUtils.isBlank(String.valueOf(resultData.getData()))) {
				throw new ExchangeException("数据交换平台存储公告失败");
			}
			// 关系表存储
			idRelationService.relation(ObjectTypeEnum.PROJECTNOTICE, prjNotice.getSourceId(),
					resultData.getData(), JSON.toJSONString(prjNotice));
		} catch (Exception e) {
			throw new ExchangeException("调用失败", e);
		}
	}

	@Override
	public String getHandleName() {
		return MsgEvent.NOTICE_MESSAGE_EVENT;
	}

}
