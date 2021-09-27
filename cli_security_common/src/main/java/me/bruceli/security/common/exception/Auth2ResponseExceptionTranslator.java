package me.bruceli.security.common.exception;


import me.bruceli.common.vo.R;
import me.bruceli.common.vo.RE;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * Created by liugh on 2019/6/6.
 */
public class Auth2ResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        Throwable throwable = e.getCause();
        if (throwable instanceof InvalidTokenException) {
            return new ResponseEntity(R.error(RE.TOKEN_ERROR), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(R.error(RE.PERMIT_ERROR), HttpStatus.FORBIDDEN);
    }
}
