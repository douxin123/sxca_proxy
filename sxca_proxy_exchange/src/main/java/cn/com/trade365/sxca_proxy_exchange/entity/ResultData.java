package cn.com.trade365.sxca_proxy_exchange.entity;

import java.io.Serializable;

public class ResultData<T> implements Serializable {
    /**
     * 正确返回码
     */
    public static final Integer SUCCESS = 200;
    /**
     * 错误返回码
     */
    public static final Integer ERROR = 400;

    private Integer code;
    private String message;
    private T data;

    public static ResultData SUCCESS(){
        return new ResultData(SUCCESS, "SUCCESS");
    }

    public static ResultData ERROR(){
        return new ResultData(ERROR, "ERROR");
    }

    public ResultData() {
    }

    public ResultData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultData(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultData(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public ResultData setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultData setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}