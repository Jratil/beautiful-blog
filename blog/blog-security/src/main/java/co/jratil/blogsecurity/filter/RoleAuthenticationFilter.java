package co.jratil.blogsecurity.filter;

import co.jratil.blogsecurity.constant.JwtConstant;
import co.jratil.blogsecurity.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 15:37
 */
public class RoleAuthenticationFilter extends BasicAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(RoleAuthenticationFilter.class);

    public RoleAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(JwtConstant.TOKEN_HEADER);

        // Token不存在或者token已过期
        if (token == null || !token.startsWith(JwtConstant.TOKEN_PREFIX) || JwtUtils.isExpire(token)) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(token));
        super.doFilterInternal(request, response, chain);
    }

    private Authentication getAuthentication(String token) {
        Integer id = JwtUtils.getIdByToken(token);
        log.debug("id={}", id);
        List<GrantedAuthority> authorities = JwtUtils.getRolesByToken(token);
        if (!StringUtils.isEmpty(id)) {
            return  new UsernamePasswordAuthenticationToken(id, null, authorities);
        }
        return null;
    }
}
