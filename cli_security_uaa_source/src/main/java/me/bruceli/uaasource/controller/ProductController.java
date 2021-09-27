package me.bruceli.uaasource.controller;

import me.bruceli.common.domain.Payload;
import me.bruceli.common.utils.JwtUtils;
import me.bruceli.common.utils.RsaUtils;
import me.bruceli.security.common.entity.SysUser;
import me.bruceli.uaasource.configProperties.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
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
