package me.bruceli.uaa.config;


import me.bruceli.security.common.exception.AuthExceptionEntryPoint;
import me.bruceli.uaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {


    /**
     * 使用jwt或者redis
     */
    @Value("${access_token.store-jwt:true}")
    private boolean storeWithJwt;

    /**
     * jwt签名key，可随意指定<br>
     * 如配置文件里不设置的话，冒号后面的是默认值
     */
    @Value("${access_token.jwt-signing-key:jwt123}")
    private String jwtSigningKey;


    //数据库连接池对象
    @Autowired
    private DataSource dataSource;

    //认证业务对象
    @Autowired
    private UserService userService;

    //授权模式专用对象
    @Autowired
    private AuthenticationManager authenticationManager;

    //客户端信息来源
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }


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

    //授权信息保存策略
    @Bean
    public ApprovalStore approvalStore(){
        return new JdbcApprovalStore(dataSource);
    }

    //授权码模式数据来源
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    //指定客户端信息的数据库来源
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    //检查token的策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        //security.checkTokenAccess("isAuthenticated()");
        security.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }

    //令牌增强类
    @Bean
    public TokenEnhancer customJwtTokenEnhancer() {
        return new CustomJwtTokenEnhancer();
    }

    // 令牌(token)管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端详情服务
        // 因为是向客户端颁发令牌，所以需要知道是哪一个客户端
        services.setClientDetailsService(jdbcClientDetailsService());
        // 支持刷新令牌
        services.setSupportRefreshToken(true);
        // 令牌存储策略
        services.setTokenStore(tokenStore());

        //将令牌增强类放到增强链中
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customJwtTokenEnhancer(),accessTokenConverter()));
        services.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期24小时
        services.setAccessTokenValiditySeconds(3600);

        // 刷新令牌默认有效期3天
        services.setRefreshTokenValiditySeconds(7200);
        return  services;
    }



    //OAuth2的主配置信息
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                //注册令牌管理增强器
                .tokenServices(tokenService());
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
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter
                .getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userService);

        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        // 这里务必设置一个，否则多台认证中心的话，一旦使用jwt方式，access_token将解析错误
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);

        return jwtAccessTokenConverter;
    }


}
