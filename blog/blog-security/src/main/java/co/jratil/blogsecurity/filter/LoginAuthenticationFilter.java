package co.jratil.blogsecurity.filter;

import co.jratil.blogcommon.enums.ResponseEnum;
import co.jratil.blogcommon.exception.GlobalException;
import co.jratil.blogcommon.response.ResponseUtils;
import co.jratil.blogsecurity.constant.JwtConstant;
import co.jratil.blogsecurity.entity.LoginEntity;
import co.jratil.blogsecurity.entity.UserDetailsImpl;
import co.jratil.blogsecurity.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 14:39
 */
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private boolean rememberMe = false;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl(JwtConstant.LOGIN_PATH);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 从request获取登录信息
            LoginEntity loginEntity = objectMapper.readValue(request.getInputStream(), LoginEntity.class);
            if (loginEntity == null) {
                throw new GlobalException("用户名或密码不正确！");
            }

            log.info(loginEntity.toString());
            rememberMe = loginEntity.isRememberMe();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    loginEntity.getAuthorAccount(), loginEntity.getAuthorPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        List<String> roles = userDetails.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = JwtUtils.createToken(userDetails.getAuthorId(), roles, rememberMe);
        // 把 token 设置到cookie中
        // response.setHeader(JwtConstant.TOKEN_HEADER, token);
        Cookie cookie = new Cookie(JwtConstant.TOKEN_HEADER, token);
        cookie.setMaxAge((int) JwtConstant.EXPIRATION);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setContentType(JwtConstant.CHARACTER_UTF_8);
        ResponseUtils.writeResSuccess(response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtils.writeResError(ResponseEnum.LOGIN_ERROR, response);
    }
}
