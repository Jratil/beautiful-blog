package co.jratil.blogapi.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用于接收前端=>
 * 1、注册
 * 2、忘记密码
 * 的表单输入
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AuthorForm", description = "表单操作对象，登录/注册/修改密码")
public class AuthorForm implements Serializable {

    private static final long serialVersionUID = -7935390480768017742L;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @Pattern(regexp = "[a-zA-Z_0-9\\u4e00-\\u9fa5=+\\-*/()%]{2,15}", message = "昵称只能3-15位的字母数字中文_=+-*/()%组成", groups = {Register.class})
    private String authorName;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户邮箱")
    @Email(message = "邮箱格式不正确", groups = {Register.class, ForgetPassword.class, Login.class})
    private String authorAccount;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    @Pattern(regexp = "[a-zA-Z0-9._\\-]{6,15}", message = "密码只能6-15位的字母数字._-组成", groups = {Register.class, ForgetPassword.class, Login.class})
    private String authorPassword;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    @Pattern(regexp = "[0-9]{4}", message = "请输入4位数验证码", groups = {Register.class, ForgetPassword.class})
    private String verifyCode;

    public interface Register {
    }

    public interface ForgetPassword {
    }

    public interface Login{}
}
