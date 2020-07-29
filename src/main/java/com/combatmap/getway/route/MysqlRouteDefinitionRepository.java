package com.combatmap.getway.route;

import com.alibaba.fastjson.JSON;
import com.combatmap.getway.entity.GatewayDefine;
import com.combatmap.getway.service.GatewayDefineService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {
    private static final Logger LOGGER = LogManager.getLogger(MysqlRouteDefinitionRepository.class);

    @Autowired
    private GatewayDefineService gatewayDefineService;


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            List<GatewayDefine> gatewayDefineList = gatewayDefineService.findAll();
            Map<String, RouteDefinition> routes = new LinkedHashMap<>();
            for (GatewayDefine gatewayDefine : gatewayDefineList) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayDefine.getRouteId());
                definition.setUri(new URI(gatewayDefine.getUri()));
                definition.setOrder(definition.getOrder());
                List<PredicateDefinition> predicateDefinitions = gatewayDefine.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = gatewayDefine.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                routes.put(definition.getId(), definition);

            }
            return Flux.fromIterable(routes.values());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Flux.empty();
        }
    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            try {
                GatewayDefine gatewayDefine = new GatewayDefine();
                gatewayDefine.setRouteId(r.getId());
                gatewayDefine.setUri(r.getUri().toString());
                gatewayDefine.setPredicates(JSON.toJSONString(r.getPredicates()));
                gatewayDefine.setFilters(JSON.toJSONString(r.getFilters()));
                gatewayDefineService.save(gatewayDefine);
                return Mono.empty();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition save error: " + r.getId())));
            }

        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            try {
                gatewayDefineService.deleteByRouteId(id);
                return Mono.empty();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition delete error: " + routeId)));
            }
        });
    }
}
