package co.jratil.blogsecurity.exception;

import co.jratil.blogapi.response.ResponseEnum;
import co.jratil.blogapi.response.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 16:01
 */
public class RoleAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 用户权限不够时处理
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtils.writeResError(ResponseEnum.NOT_AUTHORITY, httpServletResponse);
    }
}
