package top.latke.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 事件推送 Aware： 动态更新路由网关 Service
 */
@Slf4j
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    /**
     * 写路由定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;
    /**
     * 获取路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;
    /**
     * 时间发布
     */
    private ApplicationEventPublisher applicationEventPublisher;

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        //完成事件推送句柄初始化
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 增加路由定义
     * @param routeDefinition
     * @return
     */
    public String addRouteDefinition(RouteDefinition routeDefinition) {
        log.info("gateway add route: [{}]", routeDefinition);
        //保存路由配置并发布
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        //发布事件通知给Gateway，同步新增的路由定义
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    /**
     * 更新路由定义
     * @param routeDefinitions
     * @return
     */
    public String updateList(List<RouteDefinition> routeDefinitions) {
        log.info("gateway update route: [{}]", routeDefinitions);
        // 先拿到当前 Gateway 中存储的路由定义
        List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
            // 清除掉之前所有旧的路由定义
            routeDefinitionsExits.forEach(rd -> {
                log.info("delete route definition:[{}]",rd);
                deleteById(rd.getId());
            });
        }

        // 把更新的路由定义同步到 Gateway 中
        routeDefinitions.forEach(routeDefinition -> updateByRouteDefinition(routeDefinition));
        return "success";
    }

    /**
     * 根据路由 id 删除路由配置
     * @param id
     * @return
     */
    private String deleteById(String id) {
        try {
            log.info("gateway delete route id: [{}]", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            //发布事件通知给Gateway，更新路由定义
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        }catch (Exception ex){
            log.error("gateway delete route fail:[{}]",ex.getMessage(),ex);
            return "delete fail";
        }
    }

    /**
     * 更新路由配置： 删除 + 新增
     * @param routeDefinition
     * @return
     */
    private String updateByRouteDefinition(RouteDefinition routeDefinition) {
        try {
            log.info("gateway update route: [{}]", routeDefinition);
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
            //发布事件通知给Gateway，更新路由定义
//            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));

            //新增
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return "update success";
        }catch (Exception ex){
            log.error("gateway update route fail，route id:[{}]",ex.getMessage(),ex);
            return "update fail";
        }
    }

}
