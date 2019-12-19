package co.jratil.blogapi.exception;


import co.jratil.blogapi.response.ResponseEnum;

/**
 * @date 2019-08-13 23:15
 */
public class AuthorityException extends RuntimeException {

    private Integer code;
    private String message;

    public AuthorityException() {
    }

    public AuthorityException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public AuthorityException(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
