package co.jratil.blogcontroller.controller.sms;

import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.service.MailService;
import co.jratil.blogcontroller.controller.AbstractController;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-27 18:41
 */
@Api(value = "MailController", tags = "3-验证码相关的接口")
@Slf4j
@RestController
@RequestMapping("/sms")
public class MailController extends AbstractController {

    @Reference
    private MailService mailService;

    @PostMapping("/send/{toMail}/{type}")
    public ResponseVO sendVerifyCode(@PathVariable("toMail") String toMail, @PathVariable("type")Integer type) {

        this.checkParam(toMail, "toMail", this.getClass());
        this.checkParam(type, "type", this.getClass());

        mailService.sendVerifyCode(toMail, type);

        return ResponseUtils.success();
    }
}
