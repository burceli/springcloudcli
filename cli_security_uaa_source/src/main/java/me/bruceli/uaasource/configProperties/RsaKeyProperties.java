package me.bruceli.uaasource.configProperties;


import lombok.Data;
import me.bruceli.common.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;


@Data
@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RsaKeyProperties {

    private String pubKeyFile;

    private PublicKey publicKey;

    @PostConstruct
    public void createRsaKey() throws Exception {
        publicKey = RsaUtils.getPublicKey(pubKeyFile);
    }

}
