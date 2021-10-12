package me.bruceli.uaasource.controller;


import me.bruceli.uaasource.configProperties.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uaasource")
public class ProductController {

    @Autowired
    RsaKeyProperties rsaKeyProperties;
    @GetMapping("/all")
    public String findAll(){
        return "产品列表查询成功！";
    }

    @GetMapping("/who")
    public Authentication who(@RequestParam String access_token){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

}
