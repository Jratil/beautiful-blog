package co.jratil.blogapi.response;

import co.jratil.blogapi.enums.ResponseEnum;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils<T> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> ResponseVO success(T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(ResponseEnum.SUCCESS.getCode());
        responseVO.setMessage(ResponseEnum.SUCCESS.getMessage());
        responseVO.setData(data);
        return responseVO;
    }

    public static ResponseVO success() {
        return success(null);
    }

    /**
     * 将结果写入response
     * @param data
     * @param response
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeResSuccess(T data, HttpServletResponse response) throws IOException {
        ResponseVO responseVO = success(data);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }

    public static void writeResSuccess(HttpServletResponse response) throws IOException {
        writeResSuccess(null, response);
    }

    public static ResponseVO error(Integer code, String message) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(code);
        responseVO.setMessage(message);
        return responseVO;
    }

    public static ResponseVO error(ResponseEnum responseEnum) {
        ResponseVO responseVO = new ResponseVO(responseEnum);
        return responseVO;
    }

    public static void writeResError(Integer code, String message, HttpServletResponse response) throws IOException {
        ResponseVO responseVO = error(code, message);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }

    public static void writeResError(ResponseEnum responseEnum, HttpServletResponse response) throws IOException {
        ResponseVO responseVO = error(responseEnum);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }
}
