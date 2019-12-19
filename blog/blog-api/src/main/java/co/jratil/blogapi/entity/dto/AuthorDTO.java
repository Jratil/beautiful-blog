package co.jratil.blogapi.entity.dto;

import co.jratil.blogapi.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO implements Serializable {

    private static final long serialVersionUID = 6377861544235312213L;

    private Integer authorId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "name", value = "用户昵称")
    private String authorName;

    /**
     * 用户账号
     */
    @ApiModelProperty(name = "account", value = "用户账号--必须是邮箱")
    private String authorAccount;

    /**
     * 用户密码
     */
    @ApiModelProperty(name = "password", value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authorPassword;

    /**
     * 用户性别
     * 0：未知
     * 1：男
     * 2：女
     */
    @ApiModelProperty(name = "gender", value = "性别")
    private Integer authorGender;

    /**
     * 用户出生日期，默认为创建账号创建时间
     */
    @ApiModelProperty(name = "birthday", value = "生日(Date类型)")
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date authorBirthday;

    /**
     * 用户头像，存放头像图片的url
     */
    @ApiModelProperty(name = "avatar", value = "用户头像")
    private String authorAvatar;

    /**
     * 账号创建时间
     */
    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
}
