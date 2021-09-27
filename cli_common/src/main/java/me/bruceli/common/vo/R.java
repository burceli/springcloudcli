package me.bruceli.common.vo;

public class R<T> {
    private String message;
    private String code;
    private T data;

    private R() {
        this.code = RE.SUCCESS.getCode();
        this.message = RE.SUCCESS.getMessage();
    }

    private R(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(T data) {
        this.code = RE.SUCCESS.getCode();
        this.message = RE.SUCCESS.getMessage();
        this.data = data;
    }

    private R(RE re) {
        if (re == null) {
            this.code = RE.SUCCESS.getCode();
            this.message = RE.SUCCESS.getMessage();
        }
        this.code = re.getCode();
        this.message = re.getMessage();
    }

    private R(RE re, T data) {
        if (re == null) {
            this.code = RE.SUCCESS.getCode();
            this.message = RE.SUCCESS.getMessage();
            this.data = data;
        }
        this.code = re.getCode();
        this.message = re.getMessage();
        this.data = data;
    }


    public static <T> R<T> success() {
        return new R<T>();
    }

    public static <T> R<T> success(T data) {
        return new R<T>(data);
    }

    public static <T> R<T> success(RE re, T data) {
        return new R<T>(re, data);
    }

    public static <T> R<T> error() {
        return new R<T>(RE.ERROR);
    }

    public static <T> R<T> error(RE re) {
        return new R<T>(re);
    }

    public static <T> R<T> error(String code, String message) {
        return new R<T>(code, message);
    }


    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
