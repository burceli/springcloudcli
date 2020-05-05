package me.bruceli.payment.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.bruceli.payment.mapper.UserMapper;
import me.bruceli.payment.entity.User;
import me.bruceli.payment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserMapper userMapper;

    @Override
    public User selectById(long id){
        User user=userMapper.selectById(id);
        return user;
    }

    @Override
    public User insert(String name){
        User user=new User();
        user.setName(name);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateById(long id){
        User user=new User();
        user.setId(id);
        user.setName("lisi");
        userMapper.updateById(user);
        return user;
    }

    @Override
    public int deleteById(long id){
        return userMapper.deleteById(id);
    }

    @Override
    public List<User> selectList(){
        return userMapper.selectList(null);
    }

    @Override
    public User mplock(long id) {
        User user=this.selectById(id);
        user.setName("lisi2");
        userMapper.updateById(user);
        return user;
    }


    @Override
    public Page<User> selectPage(int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        userMapper.selectPage(page, null);
        return page;
    }
}
