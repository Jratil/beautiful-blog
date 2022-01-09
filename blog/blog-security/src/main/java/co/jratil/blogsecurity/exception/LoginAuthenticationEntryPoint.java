package co.jratil.blogsecurity.exception;

import co.jratil.blogcommon.enums.ResponseEnum;
import co.jratil.blogcommon.response.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 15:58
 */
public class LoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 用户没有登录时，处理
     * @param httpServletRequest
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseUtils.writeResError(ResponseEnum.NOT_LOGIN, response);
    }
}
