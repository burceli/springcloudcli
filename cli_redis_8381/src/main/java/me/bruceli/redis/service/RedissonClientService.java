package me.bruceli.redis.service;

import org.redisson.api.*;

import java.util.concurrent.TimeUnit;

public interface RedissonClientService {

    String testLock(String i);

    String testLock2(String i);

    String testLock3(String i);
}
