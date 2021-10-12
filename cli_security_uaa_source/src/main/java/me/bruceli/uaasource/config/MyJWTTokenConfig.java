package me.bruceli.uaasource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class MyJWTTokenConfig {

    @Value("${access_token.jwt-signing-key:jwt123}")
    private String jwtSigningKey;


    /**
     * 令牌存储
     */
    @Bean
    public TokenStore tokenStore() {
        /*if (storeWithJwt) {
            return new JwtTokenStore(accessTokenConverter());
        }
        return new RedisTokenStore(redisConnectionFactory);*/
        return new JwtTokenStore(accessTokenConverter());
    }



    /**
     * Jwt资源令牌转换器<br>
     * 参数access_token.store-jwt为true时用到
     *
     * @return accessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 这里务必设置一个，否则多台认证中心的话，一旦使用jwt方式，access_token将解析错误
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);

        return jwtAccessTokenConverter;
    }
}
