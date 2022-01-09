package co.jratil.blogsms.service;

import co.jratil.blogapi.constant.VerifyCodeConstant;
import co.jratil.blogapi.service.MailService;
import co.jratil.blogcommon.enums.ResponseEnum;
import co.jratil.blogcommon.exception.GlobalException;
import co.jratil.blogsms.constant.MailConstant;
import co.jratil.blogsms.utils.VerifyCodeUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-27 15:13
 */
@Slf4j
@Component
@Service(interfaceClass = MailService.class)
public class MailServiceImpl implements MailService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${mail.region_id}")
    private String RegionId;

    @Value("${mail.from.alias}")
    private String Alias;

    @Value("${mail.from.account_name}")
    private String AccountName;


    private static String AccessKeyId;
    private static String AccessKeySecret;

    static {
        Properties prop = new Properties();
        try {
            prop.load(MailServiceImpl.class.getResourceAsStream("/privateKey/SMSKey.properties"));
            AccessKeyId = prop.getProperty("AccessKeyId");
            AccessKeySecret = prop.getProperty("AccessKeySecret");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendVerifyCode(String toMail, Integer mailType) {
        String verifyCode = generateCode(toMail);

        IClientProfile profile = DefaultProfile.getProfile(RegionId, AccessKeyId, AccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();

        try {
            // 解析发送邮件模板
            ClassPathResource mailTemplate = new ClassPathResource(MailConstant.MailTemplate[mailType]);
            Scanner sc = new Scanner(mailTemplate.getInputStream());
            StringBuilder htmlBody = new StringBuilder();
            while (sc.hasNextLine()) {
                htmlBody.append(sc.next());
            }
            String mailContent = htmlBody.toString().replace("[verify_code]", verifyCode);

            request.setAccountName(AccountName);
            request.setFromAlias(Alias);
            request.setAddressType(1);
            request.setReplyToAddress(false);
            request.setToAddress(toMail);
            request.setSubject(MailConstant.MailTitle[mailType]);
            request.setHtmlBody(mailContent);
            // SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
            // 若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
            request.setSysMethod(MethodType.POST);

            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

            // 验证码发送完再设置时间，如果在之前设置，发送验证码需要时间，则会倒是验证码提前过期
            redisTemplate.opsForValue().set(VerifyCodeConstant.REDIS_VERIFY_PREFIX + toMail, verifyCode, VerifyCodeConstant.VERIFY_CODE_EXPIRE);

        } catch (ClientException e) {
            log.error("【邮件服务】发送邮件失败，错误码={}, message={}", e.getErrCode(), e.getErrMsg());
            throw new GlobalException(500, "【邮件服务】发送邮件失败，错误码：" + e.getErrCode());
        } catch (IOException e) {
            log.error("【邮件服务】解析邮件模板出错");
            throw new GlobalException(ResponseEnum.MAIL_SEND_ERROR);
        }
    }

    private String generateCode(String toMail) {
        String verifyCode = VerifyCodeUtil.generateCode();

        Object result = redisTemplate.opsForValue().get(VerifyCodeConstant.REDIS_VERIFY_PREFIX + toMail);
        if (result != null) {
            log.error("【发送邮件】发送验证码过于频繁，接收mail={}", toMail);
            throw new GlobalException(ResponseEnum.VERIFY_CODE_QUICKLY);
        }

        return verifyCode;
    }
}
