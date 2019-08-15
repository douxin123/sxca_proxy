package cn.com.trade365.sxca_proxy_exchange.entity;

import java.util.Date;

/**
 * @author :lhl
 * @create :2018-11-15 11:10
 */
public class UnstructuredDocumentEntity {


    private String id;
    private String path;
    private String realName;
    private String showName;
    private String businessTableName;
    private String businessField;
    private Date uploadTime;
    private String businessId;
    private String uploadPerson;
    private int status;
    private int category;
    private String Remarks;
    private String hash;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UnstructuredDocumentEntity{");
        sb.append("id='").append(id).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append(", showName='").append(showName).append('\'');
        sb.append(", businessTableName='").append(businessTableName).append('\'');
        sb.append(", businessField='").append(businessField).append('\'');
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", businessId='").append(businessId).append('\'');
        sb.append(", uploadPerson='").append(uploadPerson).append('\'');
        sb.append(", status=").append(status);
        sb.append(", category=").append(category);
        sb.append(", Remarks='").append(Remarks).append('\'');
        sb.append(", hash='").append(hash).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public UnstructuredDocumentEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return path;
    }

    public UnstructuredDocumentEntity setPath(String path) {
        this.path = path;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public UnstructuredDocumentEntity setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getShowName() {
        return showName;
    }

    public UnstructuredDocumentEntity setShowName(String showName) {
        this.showName = showName;
        return this;
    }

    public String getBusinessTableName() {
        return businessTableName;
    }

    public UnstructuredDocumentEntity setBusinessTableName(String businessTableName) {
        this.businessTableName = businessTableName;
        return this;
    }

    public String getBusinessField() {
        return businessField;
    }

    public UnstructuredDocumentEntity setBusinessField(String businessField) {
        this.businessField = businessField;
        return this;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public UnstructuredDocumentEntity setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public String getBusinessId() {
        return businessId;
    }

    public UnstructuredDocumentEntity setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    public String getUploadPerson() {
        return uploadPerson;
    }

    public UnstructuredDocumentEntity setUploadPerson(String uploadPerson) {
        this.uploadPerson = uploadPerson;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public UnstructuredDocumentEntity setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getCategory() {
        return category;
    }

    public UnstructuredDocumentEntity setCategory(int category) {
        this.category = category;
        return this;
    }

    public String getRemarks() {
        return Remarks;
    }

    public UnstructuredDocumentEntity setRemarks(String remarks) {
        Remarks = remarks;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public UnstructuredDocumentEntity setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public UnstructuredDocumentEntity(String id, String path, String realName, String showName, String businessTableName, String businessField, Date uploadTime, String businessId, String uploadPerson, int status, int category, String remarks, String hash) {

        this.id = id;
        this.path = path;
        this.realName = realName;
        this.showName = showName;
        this.businessTableName = businessTableName;
        this.businessField = businessField;
        this.uploadTime = uploadTime;
        this.businessId = businessId;
        this.uploadPerson = uploadPerson;
        this.status = status;
        this.category = category;
        Remarks = remarks;
        this.hash = hash;
    }

    public UnstructuredDocumentEntity() {

    }
}
