package co.jratil.blogcommon.exception;


import co.jratil.blogcommon.enums.ResponseEnum;

public class EmailException extends RuntimeException {

    Integer code;

    public EmailException() {
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
