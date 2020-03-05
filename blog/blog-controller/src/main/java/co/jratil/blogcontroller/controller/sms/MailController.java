package co.jratil.blogcontroller.controller.sms;

import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.service.MailService;
import co.jratil.blogcontroller.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-27 18:41
 */
@Api(value = "MailController", tags = "8-验证码相关的接口")
@Slf4j
@RestController
@RequestMapping("/sms")
public class MailController extends AbstractController {

    @Reference
    private MailService mailService;

    @ApiOperation(value = "发送验证码", notes = "发送验证码到邮箱", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toMail", value = "接受验证码的邮箱", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "发送的验证码类型， 0：注册，1：找回密码", paramType = "path")
    })
    @PostMapping("/send/{toMail}/{type}")
    public ResponseVO sendVerifyCode(@PathVariable("toMail") String toMail, @PathVariable("type")Integer type) {

        this.checkParam(toMail, "toMail", this.getClass());
        this.checkParam(type, "type", this.getClass());

        mailService.sendVerifyCode(toMail, type);

        return ResponseUtils.success();
    }
}
