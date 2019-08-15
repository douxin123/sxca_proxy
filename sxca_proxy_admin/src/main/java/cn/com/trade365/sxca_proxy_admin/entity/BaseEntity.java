/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package cn.com.trade365.sxca_proxy_admin.entity;



/**
 * 基础信息
 * @author lhl
 */
public class BaseEntity {

    transient private Integer offset = 0;
    transient private Integer limit = 5;
    transient private String  sort ;

    public String getSort() {
        return sort;
    }

    public BaseEntity setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public BaseEntity setOrder(String order) {
        this.order = order;
        return this;
    }

    transient private String  order;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
