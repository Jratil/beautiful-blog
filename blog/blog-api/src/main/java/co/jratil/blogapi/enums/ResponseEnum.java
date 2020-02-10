package co.jratil.blogapi.enums;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel
@Getter
public enum ResponseEnum {

    SUCCESS(0, "成功"),
    // 登录类code
    LOGIN_ERROR(1, "登录失败，账号或密码错误"),
    PASSWORD_ERROR(2, "登录失败，账号或密码错误"),
    REGISTER_ERROR(3,"注册失败"),
    UPDATE_ERROR(4, "更新失败"),
    AUTHOR_EXIST(5, "用户或昵称已经存在"),
    AUTHOR_NOT_EXIST(6, "用户不存在"),
    // 权限验证类code
    NOT_LOGIN(10, "没有登录"),
    NOT_AUTHORITY(12, "没有权限"),
    ROLE_NOT_EXIST(13, "用户角色不存在"),
    ROLE_EXIST(14, "用户角色已存在"),
    // 文章类code
    CATEGORY_NOT_EXIST(100, "类目不存在"),
    CATEGORY_EXIST(101, "类目已存在"),
    ARTICLE_NOT_EXIST(102, "文章不存在"),
    AUTHOR_NOT_ARTICLE(103, "用户没有文章"),
    ADD_ARTICLE_ERROR(104, "添加文章出错"),
    // 评论类
    COMMENT_NOT_EXIST(150, "评论不存在"),
    // 其他类code
    // 邮件类
    VERIFY_CODE_QUICKLY(1000, "验证码发送次数过快，请2分钟后再试"),
    MAIL_SEND_ERROR(1001, "发送邮件出错"),
    // 参数验证类
    PARAM_ERROR(1100, "参数错误"),
    VERIFY_NOT_EQUAL(1101, "验证码错误"),
    FILE_EMPTY(2000, "文件为空"),
    SERVER_FILE_DELETE_ERROR(2001, "删除服务器文件出错"),
    PICTURE_TOO_BIG(2002, "上传图片不能超过2M"),
    NOT_PICTURE(2003,"上传的文件不是图片");

    Integer code;

    String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
