package cn.com.trade365.sxca_proxy_exchange.core;

/**
 * @author :lhl
 * @create :2018-12-03 16:32
 */
public enum MessageStatusEnum {
    /**
     * 消息初始化状态
     */
    INIT(0),
    /**
     * 消息处理中
     */
    HANDLE(1),
    /**
     * 消息处理成功
     */
    SUCCESS(2),
    /**
     * 消息处理失败
     */
    FAIL(3);

    MessageStatusEnum(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
