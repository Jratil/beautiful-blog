package co.jratil.blogapi.exception;

import co.jratil.blogapi.enums.ResponseEnum;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-11-16 22:34
 */
public class GlobalException extends RuntimeException {

    private Integer code;
    private String message;

    public GlobalException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(String message) {
        this.code = 500;
        this.message = message;
    }

    public GlobalException(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
