package me.bruceli.gateway.repository;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.bruceli.gateway.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/***
 * @project: xxxx
 * @description: 从redis中读取路由信息到webFlux中
 */
@Slf4j
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository
{

    public static final String GATEWAY_ROUTES = "gateway:routes";
    @Autowired
    private RedisTemplate redisTemplate;

    // 请注意，此方法很重要，从redis取路由信息的方法，官方核心包要用，核心路由功能都是从redis取的
    @Override
    public Flux<RouteDefinition> getRouteDefinitions()
    {
        log.info("从redis读取路由信息 begin >>>>>>>>>>>>>>>>>>>>>");
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        redisTemplate.opsForHash().values(GATEWAY_ROUTES).stream().forEach(routeDefinition -> {
            routeDefinitions.add(JSON.parseObject(routeDefinition.toString(), RouteDefinition.class));
        });
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route)
    {
        return route.flatMap(routeDefinition -> {
            redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(), JSON.toJSONString(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId)
    {
        return routeId.flatMap(id -> {
            if (redisTemplate.opsForHash().hasKey(GATEWAY_ROUTES, id)) {
                redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("路由文件没有找到: " + routeId)));
        });
    }

}
