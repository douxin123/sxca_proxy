package cn.com.trade365.sxca_proxy_admin.entity;

import java.io.Serializable;

/**
 * @author :lhl
 * @create :2018-11-06 14:19
 */
public class MessageEntity extends BaseEntity implements Serializable {


    private Long id;
    private String data;
    private Integer status;
    private String exception;

    public String getSmallException() {
        return smallException;
    }

    public void setSmallException(String smallException) {
        this.smallException = smallException;
    }

    private String smallException;
    private String acceptTime;
    private String finishTime;
    private Integer retry;


    public MessageEntity() {
    }

    public MessageEntity(Long id, String data, Integer status, String exception, String smallException, String acceptTime, String finishTime, Integer retry) {
        this.id = id;
        this.data = data;
        this.status = status;
        this.exception = exception;
        this.smallException = smallException;
        this.acceptTime = acceptTime;
        this.finishTime = finishTime;
        this.retry = retry;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", exception='" + exception + '\'' +
                ", smallException='" + smallException + '\'' +
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

    public String getData() {
        return data;
    }

    public MessageEntity setData(String data) {
        this.data = data;
        return this;
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

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }
}
