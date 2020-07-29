package com.combatmap.getway.service;

import com.combatmap.getway.entity.GatewayDefine;

import java.util.List;

public interface GatewayDefineService {
    List<GatewayDefine> findAll();

    String loadRouteDefinition();

    GatewayDefine save(GatewayDefine gatewayDefine);

    void deleteByRouteId(String id);


}