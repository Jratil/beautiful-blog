package co.jratil.blogupload.config;

import co.jratil.blogupload.service.UploadServiceImpl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-30 17:19
 */
@Configuration
public class QiniuConfig {

    private static String AccessKey;

    private static String SecretKey;

    static {
        try {
            Properties prop = new Properties();
            prop.load(UploadServiceImpl.class.getResourceAsStream("/privateKey/qiniuKey.properties"));
            AccessKey = prop.getProperty("qiniu.access-key");
            SecretKey = prop.getProperty("qiniu.secret-key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public com.qiniu.storage.Configuration configuration() {
        return new com.qiniu.storage.Configuration(Region.huanan());
    }

    @Bean
    public Auth auth() {
        return Auth.create(AccessKey, SecretKey);
    }

    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(configuration());
    }
}
