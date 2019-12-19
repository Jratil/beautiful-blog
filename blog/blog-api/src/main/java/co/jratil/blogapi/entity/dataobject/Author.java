package co.jratil.blogapi.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_author")
public class Author implements Serializable {

    private static final long serialVersionUID = 1692928029247361088L;

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer authorId;

    /**
     * 用户昵称
     */
    private String authorName;

    /**
     * 用户账号
     */
    private String authorAccount;

    /**
     * 用户密码
     */
    private String authorPassword;

    /**
     * 用户性别，默认：0
     * 0：未知
     * 1：男
     * 2：女
     */
    private Integer authorGender;

    /**
     * 用户出生日期
     * 默认：当前时间
     */
    private Date authorBirthday;

    /**
     * 用户头像，存放头像图片的url
     * 默认："null"（字符串）
     */
    private String authorAvatar;

    /**
     * 用户的权限，默认：0
     * 0：普通用户
     * 1：管理员
     */
    private Boolean authorLock;

    /**
     * 账号创建时间
     * 默认：当前时间
     */
    private Date createTime;
}