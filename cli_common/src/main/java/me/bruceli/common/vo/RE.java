package me.bruceli.common.vo;

public enum RE {
    SUCCESS("0","成功"),

    ERROR("1001","系统繁忙"),

    PARAMETER_ERROR("1002","参数错误"),

    REPEAT_SUBMIT("1003","请勿重复提交");

    private String code;
    private String message;

    private RE(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
