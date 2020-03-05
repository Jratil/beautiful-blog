package co.jratil.blogsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude ={ DataSourceAutoConfiguration.class})
public class BlogSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSmsApplication.class, args);
    }

}
