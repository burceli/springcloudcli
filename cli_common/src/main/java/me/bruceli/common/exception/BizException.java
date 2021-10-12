package me.bruceli.common.exception;

import lombok.Getter;
import me.bruceli.common.vo.IResultCode;

@Getter
public class BizException extends RuntimeException {

    public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BizException(String message){
        super(message);
    }

    public BizException(String message, Throwable cause){
        super(message, cause);
    }

    public BizException(Throwable cause){
        super(cause);
    }
}
