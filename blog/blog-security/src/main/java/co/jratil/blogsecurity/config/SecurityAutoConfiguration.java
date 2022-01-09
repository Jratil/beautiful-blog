package co.jratil.blogsecurity.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @author jun
 * @version 1.0
 * @created 2022/1/8 19:56
 */
@Configuration
@ComponentScan(basePackages = "co.jratil.blogsecurity")
@MapperScan(basePackages = "co.jratil.blogsecurity.mapper")
public class SecurityAutoConfiguration {


}
