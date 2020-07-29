package com.combatmap.getway.service.impl;

import com.combatmap.getway.dao.GatewayDefineRepository;
import com.combatmap.getway.entity.GatewayDefine;
import com.combatmap.getway.service.GatewayDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class GatewayDefineServiceImpl implements GatewayDefineService {
    @Autowired
    GatewayDefineRepository gatewayDefineRepository;

    @Autowired
    private GatewayDefineService gatewayDefineService;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public List<GatewayDefine> findAll() {
        return gatewayDefineRepository.findAll();
    }

    @Override
    public String loadRouteDefinition() {
        try {
            List<GatewayDefine> gatewayDefineServiceAll = gatewayDefineService.findAll();
            if (gatewayDefineServiceAll == null) {
                return "none route defined";
            }
            for (GatewayDefine gatewayDefine : gatewayDefineServiceAll) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayDefine.getRouteId());
                definition.setUri(new URI(gatewayDefine.getUri()));
                List<PredicateDefinition> predicateDefinitions = gatewayDefine.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = gatewayDefine.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                routeDefinitionWriter.save(Mono.just(definition)).subscribe();
                this.publisher.publishEvent(new RefreshRoutesEvent(this));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    @Override
    public GatewayDefine save(GatewayDefine gatewayDefine) {
        boolean exists = gatewayDefineRepository.existsByRouteId(gatewayDefine.getRouteId());
        if (!exists) {
            gatewayDefineRepository.save(gatewayDefine);
            return gatewayDefine;
        }

        return new GatewayDefine();
    }

    @Override
    public void deleteByRouteId(String id) {
        gatewayDefineRepository.deleteByRouteId(id);
    }

}