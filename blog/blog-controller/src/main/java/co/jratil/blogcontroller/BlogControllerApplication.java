package co.jratil.blogcontroller;

import co.jratil.blogsecurity.config.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSecurity
@SpringBootApplication
public class BlogControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogControllerApplication.class, args);
    }
}
