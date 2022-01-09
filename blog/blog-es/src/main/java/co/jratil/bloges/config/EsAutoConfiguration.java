package co.jratil.bloges.config;

import co.jratil.bloges.repository.ArticleSearchRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jun
 * @version 1.0
 * @created 2022/1/9 15:48
 */
@Configuration
@ComponentScan(basePackages = "co.jratil.bloges")
@MapperScan(basePackages = "co.jratil.bloges.repository")
public class EsAutoConfiguration {
}
