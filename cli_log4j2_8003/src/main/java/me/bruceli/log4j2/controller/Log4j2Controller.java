package me.bruceli.log4j2.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Log4j2Controller {

    @GetMapping(value="/log/info/{name}")
    public String testInfo(@PathVariable("name") String name){
        log.info("testInfo start....");
        log.info("testInfo name={}",name);
        log.info("testInfo end....");
        return null;
    }
    @GetMapping(value="/log/error/{name}")
    public String testError(@PathVariable("name") String name){
        log.info("testInfo start....");
        log.info("testInfo name={}",name);
        log.error("testError name={}",name);
        log.info("testInfo end....");
        return null;
    }
}
