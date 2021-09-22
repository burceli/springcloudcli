package me.bruceli.gateway.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.bruceli.gateway.entity.GatewayRouteInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GatewayRouteInfoMapper extends BaseMapper<GatewayRouteInfoEntity> {
}
