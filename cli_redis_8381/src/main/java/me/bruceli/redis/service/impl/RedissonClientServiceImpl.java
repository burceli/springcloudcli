package me.bruceli.redis.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.bruceli.redis.annotation.DistributedLock;
import me.bruceli.redis.service.RedissonClientService;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedissonClientServiceImpl implements RedissonClientService {

    @Autowired
    private RedissonClient redissonClient;


    @Override
    @DistributedLock(argNum = 1, leaseTime = 10l, waitTime = 30l, tryLock = true, fairLock = false)
    public String testLock(String i) {
        try {
            log.info("i={}", i);
            log.info("thread sleep start ");
            Thread.sleep(3000);
            log.info("thread sleep end ");
            return Thread.currentThread().getName()+"--i="+i;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    @DistributedLock(argNum = 1, leaseTime = 3l, waitTime = 30l, tryLock = true, fairLock = false)
    public String testLock2(String i) {
        try {
            log.info("i={}", i);
            log.info("thread sleep start ");
            Thread.sleep(10000);
            log.info("thread sleep end ");
            return Thread.currentThread().getName()+"--i="+i;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    @DistributedLock(argNum = 1, leaseTime = 3l, waitTime = 10l, tryLock = true, fairLock = false)
    public String testLock3(String i) {
        try {
            log.info("i={}", i);
            log.info("thread sleep start ");
            Thread.sleep(30000);
            log.info("thread sleep end ");
            return Thread.currentThread().getName()+"--i="+i;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }


}
