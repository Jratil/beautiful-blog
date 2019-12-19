package co.jratil.blogsecurity.config;

import co.jratil.blogsecurity.exception.LoginAuthenticationEntryPoint;
import co.jratil.blogsecurity.exception.RoleAccessDeniedHandler;
import co.jratil.blogsecurity.filter.LoginAuthenticationFilter;
import co.jratil.blogsecurity.filter.RoleAuthenticationFilter;
import co.jratil.blogsecurity.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 17:41
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                // 拦截请求，创建了FilterSecurityInterceptor拦截器
                .authorizeRequests()
                // 放行的api
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/register").permitAll()
                // swagger start，放行 swagger 相关资源
                .antMatchers("/swagger").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                // swagger end
                // 全部拦截
                .anyRequest().authenticated()

                .and()
                // 添加自定义的过滤器
                .addFilter(new LoginAuthenticationFilter(authenticationManager()))
                .addFilter(new RoleAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 添加自定义的异常处理
                .exceptionHandling().authenticationEntryPoint(new LoginAuthenticationEntryPoint())
                .accessDeniedHandler(new RoleAccessDeniedHandler())
                .and()
                .headers()
                .frameOptions()
                .disable();
    }
}
