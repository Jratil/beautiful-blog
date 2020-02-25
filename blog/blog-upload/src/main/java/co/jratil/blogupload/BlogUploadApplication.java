package co.jratil.blogupload;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@EnableDubboConfiguration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BlogUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogUploadApplication.class, args);
    }

}
