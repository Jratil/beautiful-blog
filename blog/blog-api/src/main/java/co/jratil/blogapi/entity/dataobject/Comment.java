package co.jratil.blogapi.entity.dataobject;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_comment
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_comment")
public class Comment implements Serializable {

    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 评论人userId
     */
    private Integer authorId;

    /**
     * 评论的文章id
     */
    private Integer articleId;

    /**
     * 父评论id
     */
    private Integer parentCommentId;

    /**
     * 父评论的用户id
     */
    private Integer parentCommentAuthorId;

    /**
     * 被回复的评论id
     */
    private Integer replyCommentId;

    /**
     * 被回复的评论用户id
     */
    private Integer replyCommentAuthorId;

    /**
     * 评论等级[ 1 一级评论 默认 ，2 二级评论]
     */
    private Integer commentLevel;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 状态 (1 有效，0 逻辑删除)
     */
    private Boolean deleteStatus;

    /**
     * 点赞数
     */
    private Integer praiseNum;

    /**
     * 置顶状态[ 1 置顶，0 不置顶 默认 ]
     */
    private Boolean topStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}