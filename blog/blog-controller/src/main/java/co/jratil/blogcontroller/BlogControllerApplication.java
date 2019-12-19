package co.jratil.blogcontroller;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@MapperScan("co.jratil.blogsecurity.mapper")
@EnableDubboConfiguration
@SpringBootApplication(scanBasePackages = {"co.jratil.blogcontroller", "co.jratil.blogsecurity"})
public class BlogControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogControllerApplication.class, args);
    }
}
