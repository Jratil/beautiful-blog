package co.jratil.blogapi.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-01-01 18:25
 */
@ApiModel(value = "CommentDTO", description = "添加评论使用")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = -4260795352606391422L;

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer commentId;

    /**
     * 评论人userId
     */
    @ApiModelProperty(value = "评论者id", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer authorId;

    @ApiModelProperty(value = "评论者名称", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String authorName;

    /**
     * 评论的文章id
     */
    @ApiModelProperty(value = "评论的文章id")
    @NotNull(message = "文章id为空")
    private Integer articleId;

    /**
     * 父评论id
     */
    @ApiModelProperty(value = "父评论id，就是如果是回复别人的评论，则那个最上层的评论id")
    private Integer parentCommentId;

    /**
     * 父评论的用户id
     */
    @ApiModelProperty(value = "父评论的用户id，就是如果是回复别人的评论，则那个发表最上层评论的用户id", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer parentCommentAuthorId;

    /**
     * 被回复的评论id
     */
    @ApiModelProperty(value = "回复的评论id")
    private Integer replyCommentId;

    /**
     * 被回复的评论用户id
     */
    @ApiModelProperty(value = "回复的评论的用户id", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer replyCommentAuthorId;

    @ApiModelProperty(value = "回复的评论的用户名字", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String replyCommentAuthorName;

    /**
     * 评论等级[ 1 一级评论 ，2 二级评论]
     */
    @ApiModelProperty(value = "评论的层级，回复文章：1，回复其他评论：2")
    @NotNull(message = "评论层级为空，请选择['1', '2']")
    private Integer commentLevel;

    /**
     * 评论的内容
     */
    @ApiModelProperty(value = "回复的内容，长度：1-200")
    @Length(min = 1, max = 200, message = "评论内容不能为空，且不能超过200字")
    private String content;

    /**
     * 状态 (1 有效，0 逻辑删除)
     */
    @ApiModelProperty(value = "评论的删除状态", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean deleteStatus;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "评论的点赞数", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer praiseNum;

    /**
     * 返回自己是否点赞了
     */
    @ApiModelProperty(value = "是否点赞了", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean hasLike;

    @ApiModelProperty(value = "用户头像", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, defaultValue = "")
    private String authorAvatar;

    /**
     * 置顶状态[ 1 置顶，0 不置顶 默认 ]
     */
    @ApiModelProperty(value = "评论的置顶状态", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean topStatus;

    @ApiModelProperty(value = "一级评论的子评论数量", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer childCommentTotal = 0;

    @ApiModelProperty(value = "文章显示时默认显示的两条子评论", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CommentDTO> childCommentList = new ArrayList<>();

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "评论的发表时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createTime;
}
