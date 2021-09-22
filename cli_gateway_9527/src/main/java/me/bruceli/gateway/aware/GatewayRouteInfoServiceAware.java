package me.bruceli.gateway.aware;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import me.bruceli.gateway.entity.GatewayRouteInfoEntity;
import me.bruceli.gateway.mapper.GatewayRouteInfoMapper;
import me.bruceli.gateway.repository.RedisRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Slf4j
@Component
public class GatewayRouteInfoServiceAware implements ApplicationEventPublisherAware, CommandLineRunner {

    @Autowired
    private RedisRouteDefinitionRepository redisRouteDefinitionRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GatewayRouteInfoMapper gatewayRouteInfoMapper;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        this.publisher = applicationEventPublisher;
    }



    @Override
    public void run(String... args)
    {
        this.loadRouteConfig();
    }



    public String loadRouteConfig()
    {
        log.info("===网关配置信息===开始加载===");
        // 删除redis里面的路由配置信息
        redisTemplate.delete(RedisRouteDefinitionRepository.GATEWAY_ROUTES);

        // 从数据库拿到基本路由配置
        List<GatewayRouteInfoEntity> gatewayRouteList = gatewayRouteInfoMapper.selectList(null);
        gatewayRouteList.forEach(gatewayRoute -> {
            RouteDefinition definition = handleData(gatewayRoute);
            redisRouteDefinitionRepository.save(Mono.just(definition)).subscribe();
        });

        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        log.info("===网关配置信息===加载完成===");

        return "success";
    }

    /**
     * 从redis查询所有已经加载的路由
     *
     * @return
     */
    public List<GatewayRouteInfoEntity> queryAllRoutes()
    {
        List<GatewayRouteInfoEntity> gatewayRouteInfos = new ArrayList<GatewayRouteInfoEntity>();
        redisTemplate.opsForHash().values(RedisRouteDefinitionRepository.GATEWAY_ROUTES).stream()
                .forEach(routeDefinition -> {
                    RouteDefinition definition = JSON.parseObject(routeDefinition.toString(), RouteDefinition.class);
                    gatewayRouteInfos.add(convert2GatewayRouteInfo(definition));
                });
        return gatewayRouteInfos;
    }

    /**
     * 将redis中路由信息转换为返回给前端的路由信息
     *
     * @param obj
     *            redis中的路由
     * @return
     */
    private GatewayRouteInfoEntity convert2GatewayRouteInfo(Object obj)
    {
        RouteDefinition routeDefinition = (RouteDefinition) obj;
        GatewayRouteInfoEntity gatewayRouteInfoEntity = new GatewayRouteInfoEntity();
        gatewayRouteInfoEntity.setUri(routeDefinition.getUri().toString());
        gatewayRouteInfoEntity.setServiceId(routeDefinition.getId());
        List<PredicateDefinition> predicates = routeDefinition.getPredicates();
        // 只有一个
        if (CollectionUtils.isNotEmpty(predicates)) {
            String predicatesString = predicates.get(0).getArgs().get("pattern");
            gatewayRouteInfoEntity.setPredicates(predicatesString);
        }
        List<FilterDefinition> filters = routeDefinition.getFilters();
        if (CollectionUtils.isNotEmpty(filters)) {
            String filterString = filters.get(0).getArgs().get("_genkey_0");
            gatewayRouteInfoEntity.setFilters(filterString);
        }
        gatewayRouteInfoEntity.setRouteOrder(String.valueOf(routeDefinition.getOrder()));;
        return gatewayRouteInfoEntity;
    }

    public void addRoute(GatewayRouteInfoEntity gatewayRouteInfoEntity)
    {
        RouteDefinition definition = handleData(gatewayRouteInfoEntity);
        redisRouteDefinitionRepository.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public void updateRoute(GatewayRouteInfoEntity gatewayRouteInfoEntity)
    {
        RouteDefinition definition = handleData(gatewayRouteInfoEntity);
        try {
            this.redisRouteDefinitionRepository.delete(Mono.just(definition.getId()));
            redisRouteDefinitionRepository.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRoute(String routeId)
    {
        redisRouteDefinitionRepository.delete(Mono.just(routeId)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 路由数据转换公共方法
     *
     * @param gatewayRouteInfoEntity
     * @return
     */
    private RouteDefinition handleData(GatewayRouteInfoEntity gatewayRouteInfoEntity)
    {
        RouteDefinition definition = new RouteDefinition();
        /*
        // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
        Map<String, String> predicateParams = new HashMap<>(8);
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        predicateParams.put("pattern", gatewayRouteInfoEntity.getPredicates());
        predicate.setArgs(predicateParams);

        // 名称是固定的, 路径去前缀
        FilterDefinition filterDefinition = new FilterDefinition();
        Map<String, String> filterParams = new HashMap<>(8);
        filterDefinition.setName("StripPrefix");
        filterParams.put("_genkey_0", gatewayRouteInfoEntity.getFilters().toString());
        filterDefinition.setArgs(filterParams);
        */

        URI uri = null;
        if (gatewayRouteInfoEntity.getUri().startsWith("http")) {
            // http地址
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteInfoEntity.getUri()).build().toUri();
        }
        else {
            // 注册中心
            uri = UriComponentsBuilder.fromUriString("lb://" + gatewayRouteInfoEntity.getUri()).build().toUri();
        }

        definition.setId(gatewayRouteInfoEntity.getServiceId());
        definition.setPredicates(JSONUtil.toList(gatewayRouteInfoEntity.getPredicates(),PredicateDefinition.class));
        if(null!=gatewayRouteInfoEntity.getFilters() && !"".equals(gatewayRouteInfoEntity.getFilters())){
            definition.setFilters(JSONArray.parseArray(gatewayRouteInfoEntity.getFilters(),FilterDefinition.class));
        }
        definition.setUri(uri);
        definition.setOrder(Integer.parseInt(gatewayRouteInfoEntity.getRouteOrder()));

        return definition;
    }


}