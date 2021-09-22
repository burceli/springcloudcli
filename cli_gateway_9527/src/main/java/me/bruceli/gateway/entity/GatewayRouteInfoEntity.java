package me.bruceli.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("gateway_route_info")
public class GatewayRouteInfoEntity {
    private Integer id;
    private String serviceId;
    private String uri;
    private String predicates;
    private String filters;
    private String routeOrder;
    private String remarks;
    private String delFlag;
    private Date createDate;
    private Date updateDate;

}
