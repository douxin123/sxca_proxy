package cn.com.trade365.sxca_proxy_exchange.entity;


import java.util.Date;

/**
 * @author :lhl
 * @create :2018-11-06 14:19
 */
public class MessageEntity {


    private Long id;
    private String msgData;
    private Integer status;

    private String exception;
    private Date acceptTime;
    private Date finishTime;
    private Integer retry;


    public MessageEntity() {
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msgData='" + msgData + '\'' +
                ", status=" + status +
                ", exception='" + exception + '\'' +
                ", acceptTime='" + acceptTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", retry=" + retry +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public MessageEntity(Long id, String msgData, Integer status, String exception, Date acceptTime, Date finishTime, Integer retry) {
        this.id = id;
        this.msgData = msgData;
        this.status = status;
        this.exception = exception;
        this.acceptTime = acceptTime;
        this.finishTime = finishTime;
        this.retry = retry;
    }

    public Date getAcceptTime() {

        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }
}
