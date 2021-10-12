package me.bruceli.uaa.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bruceli.common.vo.Result;
import me.bruceli.security.common.constant.AuthConstants;
import me.bruceli.security.common.utils.JwtUtils;
import me.bruceli.security.common.utils.RequestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class OAuthController {

    private TokenEndpoint tokenEndpoint;
    private RedisTemplate redisTemplate;
    private KeyPair keyPair;

    @PostMapping("/token")
    public Object postAccessToken(
            Principal principal,
            @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException {

        /**
         * 获取登录认证的客户端ID
         *
         * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
         * 方式一：client_id、client_secret放在请求路径中(注：当前版本已废弃)
         * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
         */
        String clientId = RequestUtils.getOAuth2ClientId();
        log.info("OAuth认证授权 客户端ID:{}，请求参数：{}", clientId, JSONUtil.toJsonStr(parameters));

        /**
         * knife4j接口文档测试使用
         *
         * 请求头自动填充，token必须原生返回，否则显示 undefined undefined
         * 账号/密码:  client_id/client_secret : client/123456
         */
        if (AuthConstants.TEST_CLIENT_ID.equals(clientId)) {
            return tokenEndpoint.postAccessToken(principal, parameters).getBody();
        }

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return Result.success(accessToken);
    }

    @DeleteMapping("/logout")
    public Result logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getStr(AuthConstants.JWT_JTI); // JWT唯一标识
        Long expireTime = payload.getLong(AuthConstants.JWT_EXP); // JWT过期时间戳(单位：秒)
        if (expireTime != null) {
            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else { // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
        return Result.success("注销成功");
    }

    @GetMapping("/public-key")
    public int getPublicKey() {
        return 1;
    }
}
