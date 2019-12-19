package co.jratil.blogsecurity.entity;

import co.jratil.blogapi.entity.dto.AuthorForm;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 18:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity implements Serializable {

    private static final long serialVersionUID = -164385733095094972L;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户邮箱")
    @JsonProperty("account")
    @Email(message = "邮箱格式不正确", groups = {AuthorForm.Register.class, AuthorForm.ForgetPassword.class, AuthorForm.Login.class})
    private String authorAccount;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    @JsonProperty("password")
    @Pattern(regexp = "[a-zA-Z0-9._\\-]{6,15}", message = "密码只能6-15位的字母数字._-组成", groups = {AuthorForm.Register.class, AuthorForm.ForgetPassword.class, AuthorForm.Login.class})
    private String authorPassword;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    @JsonProperty("verify-code")
    @Pattern(regexp = "[0-9]{4}", message = "请输入4位数验证码", groups = {AuthorForm.Register.class, AuthorForm.ForgetPassword.class})
    private String verifyCode;

    /**
     * 记住我
     */
    @ApiModelProperty("记住我")
    @JsonProperty("remember-me")
    private boolean rememberMe;
}