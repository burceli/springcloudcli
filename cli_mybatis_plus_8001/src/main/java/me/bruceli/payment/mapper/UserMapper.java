package me.bruceli.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.bruceli.payment.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
