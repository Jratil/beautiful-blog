package co.jratil.blogredis;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableDubboConfiguration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BlogRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogRedisApplication.class, args);
    }

}
