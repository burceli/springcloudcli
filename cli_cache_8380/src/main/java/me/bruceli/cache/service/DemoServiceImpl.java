package me.bruceli.cache.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "demoService")//抽取缓存的公共配置
public class DemoServiceImpl implements DemoService{

    @Override
    @Cacheable(value="SwitchCfgList", keyGenerator = "simpleKeyGenerator")
    public List<String> getAll(){
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        return list;
    }

    @Override
    @Cacheable(value="SwitchCfgList",keyGenerator = "simpleKeyGenerator")
    public String queryById(String id){
        return id;
    }

    @Override
    @CachePut(value="SwitchCfgList",key="#account.getName()")
    //@CacheEvict(value="accountCache",allEntries=true)删除所有
    public void add(String id){

    }

    @Override
    @CacheEvict(value="SwitchCfgList",key="#p0")
    public void delete(String id){

    }
}
