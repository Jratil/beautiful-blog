package co.jratil.blogcontroller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("co.jratil.blogsecurity.mapper")
@SpringBootApplication(scanBasePackages = {"co.jratil.blogcontroller", "co.jratil.blogsecurity"})
public class BlogControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogControllerApplication.class, args);
    }
}
