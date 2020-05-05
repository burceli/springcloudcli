package me.bruceli.payment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.bruceli.payment.entity.User;

import java.util.List;


public interface UserService {

    User selectById(long id);

    User insert(String name);

    User updateById(long id);

    int deleteById(long id);

    List<User> selectList();

    User mplock(long id);

    Page<User> selectPage(int currentPage, int pageSize);
}
