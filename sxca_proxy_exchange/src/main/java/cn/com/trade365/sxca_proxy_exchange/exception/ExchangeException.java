package cn.com.trade365.sxca_proxy_exchange.exception;

/**
 * 转换异常信息
 */
public class ExchangeException extends Exception{
    /**
     * 异常状态码
     */
    private String code;

    public ExchangeException(String message) {
        this(null,message,null);
    }

    public ExchangeException(String code,String message) {
        this(code,message,null);
    }

    public ExchangeException(String code,Throwable e) {
        this(code,null,e);
    }

    public ExchangeException(String code,String message, Throwable e) {
        super(message, e);
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
