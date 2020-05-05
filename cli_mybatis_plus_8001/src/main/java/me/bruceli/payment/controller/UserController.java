package me.bruceli.payment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.bruceli.payment.entity.User;
import me.bruceli.payment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    public UserService userService;

    @GetMapping(value="/user/selectById/{id}")
    public User selectById(@PathVariable("id") long id){
        return userService.selectById(id);
    }

    @GetMapping(value="/user/insert/{name}")
    public User insert(@PathVariable("name") String name){
        return userService.insert(name);
    }

    @GetMapping(value="/user/updateById/{id}")
    public User insert(@PathVariable("id") long id){
        return userService.updateById(id);
    }

    @GetMapping(value="/user/deleteById/{id}")
    public Integer deleteById(@PathVariable("id") long id){
        return userService.deleteById(id);
    }

    @GetMapping(value="/user/selectList")
    public List<User> selectById(){
        return userService.selectList();
    }

    @GetMapping(value="/user/mplock/{id}")
    public User mplock(@PathVariable("id") long id){
        return userService.mplock(id);
    }

    @GetMapping(value="/user/selectPage/{currentPage}/{pageSize}")
    public Page<User> selectPage(@PathVariable("currentPage") int currentPage, @PathVariable("pageSize") int pageSize){
        return userService.selectPage(currentPage, pageSize);
    }
}
