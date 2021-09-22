package me.bruceli.security.mapper;


import java.util.List;


import me.bruceli.security.entity.PermissionEntity;
import org.apache.ibatis.annotations.Select;


public interface PermissionMapper {

    @Select(" select * from sys_permission ")
    List<PermissionEntity> findAllPermission();

}
