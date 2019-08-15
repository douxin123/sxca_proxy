package cn.com.trade365.sxca_proxy_exchange.entity;

/**
 * @author :lhl
 * @create :2018-11-06 16:46
 */
public class RelationEntity {

    private String code;

    private int id;

    private String tradeId;

    private long  dataId;

    private String data;

    @Override
    public String toString() {
        return "RelationEntity{" +
                "code='" + code + '\'' +
                ", id=" + id +
                ", tradeId='" + tradeId + '\'' +
                ", dataId='" + dataId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public RelationEntity(String code, int id, String tradeId, long dataId, String data) {
        this.code = code;
        this.id = id;
        this.tradeId = tradeId;
        this.dataId = dataId;
        this.data = data;
    }

    public RelationEntity(String code, String tradeId, long dataId, String data) {
        this.code = code;
        this.tradeId = tradeId;
        this.dataId = dataId;
        this.data = data;
    }

    public RelationEntity() {

    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
