package co.jratil.blogapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "ResultVO", description = "返回的JSON格式")
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {

    @ApiModelProperty("响应的状态码")
    private Integer code;

    @ApiModelProperty("响应的结果说明")
    private String message;

    @ApiModelProperty("返回的数据")
    private T data;

    public ResponseVO() {
    }

    public ResponseVO(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseVO(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResponseVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
