package me.bruceli.cache.controller;

import me.bruceli.cache.service.DemoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private DemoService demoService;

    @GetMapping("/getAll")
    public List<String> getAll(){
        return demoService.getAll();
    }

    @GetMapping("/${id}")
    public String queryById(@PathVariable String id){
        return demoService.queryById(id);
    }

    @PostMapping("/${id}")
    public void add(String id){
        demoService.add(id);
    }

    @DeleteMapping("/${id}")
    public void delete(String id){
        demoService.delete(id);
    }
}
