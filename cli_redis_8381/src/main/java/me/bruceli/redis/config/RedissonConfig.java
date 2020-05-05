package me.bruceli.redis.config;

import me.bruceli.redis.lock.DistributedLockTemplate;
import me.bruceli.redis.lock.SingleDistributedLockTemplate;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

@Configuration
public class RedissonConfig {

    @Autowired
    private Environment env;

    @Bean
    public RedissonClient redisson() throws Exception {

        String[] profiles = env.getActiveProfiles();
        String profile = "test";
        if(profiles.length > 0) {
            profile = profiles[0];
        }
        String fileName="redis"+File.separator+"redission-config-cluster-"+profile+".yml";
        return Redisson.create(Config.fromYAML(new ClassPathResource(fileName).getInputStream()));

    }

    @Bean
    DistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) {
        return new SingleDistributedLockTemplate(redissonClient);
    }

}
