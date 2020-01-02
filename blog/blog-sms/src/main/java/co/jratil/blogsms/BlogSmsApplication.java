package co.jratil.blogsms;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;

@EnableDubboConfiguration
@SpringBootApplication(exclude ={ DataSourceAutoConfiguration.class})
public class BlogSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSmsApplication.class, args);
    }

}
