package com.combatmap.getway.dao;

import com.combatmap.getway.entity.GatewayDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GatewayDefineRepository extends JpaRepository<GatewayDefine, String> {

    void deleteByRouteId(String id);

    boolean existsByRouteId(String routeId);
}

