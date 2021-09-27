package me.bruceli.uaasource.config;

import me.bruceli.common.constant.PermitAllUrl;

import me.bruceli.security.common.exception.Auth2ResponseExceptionTranslator;
import me.bruceli.security.common.exception.AuthExceptionEntryPoint;
import me.bruceli.security.common.exception.MyAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class OauthSourceConfig extends ResourceServerConfigurerAdapter {

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
     * 指定当前资源的id和存储方案
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 定义异常转换类生效
        AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint)
                .setExceptionTranslator(new Auth2ResponseExceptionTranslator());
        resources.resourceId("product_api").tokenStore(tokenStore()).authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                //指定不同请求方式访问资源所需要的权限，一般查询是read，其余是write。
                .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(PermitAllUrl.permitAllUrl("/zip/**","/thumbFile/**","/test/**")).permitAll() // 放开权限的url
                .anyRequest().authenticated()
                .and()
                //异常的时候处理 返回SC_UNAUTHORIZED --> 401状态码未授权异常
                .exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint())
                .accessDeniedHandler(new MyAccessDeniedHandler())
                .and()
                .headers().addHeaderWriter((request, response) -> {
            response.addHeader("Access-Control-Allow-Origin", "*");//允许跨域
            if (request.getMethod().equals("OPTIONS")) {//如果是跨域的预检请求，则原封不动向下传达请求头信息
                response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            }
        });

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
