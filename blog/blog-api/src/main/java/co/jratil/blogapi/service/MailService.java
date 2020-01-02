package co.jratil.blogapi.service;

public interface MailService {

    /**
     * 发送验证码，标题和内容已经在配置文件定义，只要到时候生成验证码发送
     * @param toMail 接收邮箱
     */
    void sendVerifyCode(String toMail, Integer mailType);
}
