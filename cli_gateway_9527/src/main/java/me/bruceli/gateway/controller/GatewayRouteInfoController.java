package me.bruceli.gateway.controller;

import me.bruceli.common.vo.R;
import me.bruceli.gateway.aware.GatewayRouteInfoServiceAware;
import me.bruceli.gateway.entity.GatewayRouteInfoEntity;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mygateway")
public class GatewayRouteInfoController {


    @Autowired
    private GatewayRouteInfoServiceAware gatewayRouteInfoServiceAware;

    /**
     * 刷新路由配置
     *
     * @return
     */
    @GetMapping("/refresh")
    public R refresh()
    {
        this.gatewayRouteInfoServiceAware.loadRouteConfig();
        return R.success();
    }


    /**
     * 从redis查询所有已经加载的路由
     * @return
     */
    @GetMapping("/routes")
    public R<List<GatewayRouteInfoEntity>> routes()
    {
        List<GatewayRouteInfoEntity> gatewayRouteInfoEntityList = gatewayRouteInfoServiceAware.queryAllRoutes();

        return R.success(gatewayRouteInfoEntityList);
    }

}

