package co.jratil.blogsms.constant;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-27 18:05
 */
public class MailConstant {

    public static final Integer REGISTER = 0;
    public static final Integer FORGET_PASSWORD = 1;

    public static final String[] MailTemplate = {"static/mail_register.html", "static/mail_forget_password.html"};
    public static final String[] MailTitle = {"【不错博客】注册邮箱验证", "【不错博客】忘记密码邮箱验证"};

}
