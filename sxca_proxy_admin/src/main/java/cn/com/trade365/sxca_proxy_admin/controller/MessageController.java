package cn.com.trade365.sxca_proxy_admin.controller;

import cn.com.trade365.sxca_proxy_admin.entity.MessageEntity;
import cn.com.trade365.sxca_proxy_admin.entity.ResultData;
import cn.com.trade365.sxca_proxy_admin.service.MessageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author :lhl
 * @create :2018-11-07 17:19
 */
@Controller
@RequestMapping("message")
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Value("${active.trade.message}")
    private String destination;


    @RequestMapping("exception/detail")
    @ResponseBody
    public ResultData queryById(HttpServletRequest request) {
        String strParentID = request.getParameter("id");
        if (!StringUtils.isEmpty(strParentID)) {
            return new ResultData(200, null).setData(messageService.findExceptionById(Integer.parseInt(strParentID)));
        }
        return new ResultData(400, "请传入id");
    }

    @RequestMapping("search")
    @ResponseBody
    public ModelMap query(MessageEntity messageEntity, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        String status = request.getParameter("status");
        String exception = request.getParameter("exception");
        String msgData = request.getParameter("msgData");
        log.info("接收到的: status={}; exception={}; msgData={} ", status, exception, msgData);
        if (!StringUtils.isEmpty(status)) {
            messageEntity.setStatus(Integer.parseInt(status));
        }
        messageEntity.setException(exception);
        messageEntity.setData(msgData);
        List<MessageEntity> list = messageService.findAll(messageEntity);
        model.put("pageInfo", new PageInfo<MessageEntity>(list));
        model.put("queryParam", messageEntity);
        return model;
    }

    @RequestMapping(value = "exception/retry")
    @ResponseBody
    public String retry(@RequestParam Long id) {
        if (null != id) {
            try {
            JSONObject result=new JSONObject();
            MessageEntity messageEntity = messageService.findById(id);
            String data = messageEntity.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            jsonObject.put("messageId", id);
            jmsMessagingTemplate.convertAndSend(destination, jsonObject.toJSONString());
            messageService.updateRetryById(id,messageEntity.getRetry());
            return HttpStatus.OK.toString();
            }catch (Exception e){
                log.error("重试失败 : ",e);
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.toString();
    }
}
