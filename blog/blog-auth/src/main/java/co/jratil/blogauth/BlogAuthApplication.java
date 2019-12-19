package co.jratil.blogauth;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = {"co.jratil.blogauth.mapper", "co.jratil.blogsecurity"})
@EnableCaching
@EnableDubboConfiguration
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"co.jratil.blogauth", "co.jratil.blogsecurity"})
public class BlogAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAuthApplication.class, args);
    }

}
