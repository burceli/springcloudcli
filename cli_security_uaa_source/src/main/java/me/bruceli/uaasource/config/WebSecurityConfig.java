package me.bruceli.uaasource.config;


import me.bruceli.common.constant.PermitAllUrl;
import me.bruceli.uaasource.configProperties.RsaKeyProperties;
import me.bruceli.uaasource.filter.JwtVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * spring security配置
 *
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    //SpringSecurity配置信息
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(PermitAllUrl.permitAllUrl("/swaggerList","/oauth/token","/oauth/user/token","/users-anon/**",
                        "/smsVerify","/thirdPartyLogin/**")).permitAll() // 放开权限的url
            .anyRequest()
            .authenticated()
            .and()
            .addFilter(new JwtVerifyFilter(super.authenticationManager(), rsaKeyProperties))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
