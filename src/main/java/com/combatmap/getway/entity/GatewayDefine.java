package com.combatmap.getway.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "eif_gateway_define")
public class GatewayDefine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String routeId;
    /**
     * 跳转地址uri
     */
    private String uri;
    /**
     * 路由谓词
     */
    private String predicates;
    /**
     * 过滤器
     */
    private String filters;
    /**
     * 路由顺序
     */
    private int order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPredicates() {
        return this.predicates;
    }

    public void setPredicates(String predicates) {
        this.predicates = predicates;
    }

    public List<PredicateDefinition> getPredicateDefinition() {
        if (this.predicates != null) {
            return JSON.parseArray(this.predicates, PredicateDefinition.class);
        } else {
            return null;
        }
    }

    public String getFilters() {
        return filters;
    }

    public List<FilterDefinition> getFilterDefinition() {
        if (this.filters != null) {
            return JSON.parseArray(this.filters, FilterDefinition.class);
        } else {
            return null;
        }
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "GatewayDefine{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", predicates='" + predicates + '\'' +
                ", filters='" + filters + '\'' +
                '}';
    }
}
