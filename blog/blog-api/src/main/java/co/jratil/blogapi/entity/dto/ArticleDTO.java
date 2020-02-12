package co.jratil.blogapi.entity.dto;

import co.jratil.blogapi.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = 2518198230132210040L;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 作者的昵称
     */
    private String authorName;

    /**
     * 文章标题
     */
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true)
    @Length(min = 1, max = 25, message = "标题字数不符合")
    private String articleTitle;

    /**
     * 文章副标题，在文章列表展示
     */
    @ApiModelProperty(name = "articleSubtitle", value = "文章副标题")
    @Length(max = 64, message = "副标题字数不符合")
    private String articleSubtitle;

    /**
     * 文章是否对他人可见，默认：0
     * 0：可见
     * 1：不可见
     */
    @ApiModelProperty(name = "visible", value = "是否可见")
    private Boolean articleVisible;


    /**
     * 作者的id
     */
    @ApiModelProperty(name = "authorId", value = "作者id", required = true)
    private Integer authorId;

    /**
     * 类目的id
     */
    @ApiModelProperty(name = "categoryId", value = "文章所处类目的id", required = true)
    @NotNull(message = "类目id不能为空")
    private Integer categoryId;

    /**
     * 类目的名称
     */
    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    /**
     * 文章内容
     */
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true)
    @Length(min = 1, max = 50000, message = "文章最少1个字，最长50000个字符")
    private String articleContent;


    /**
     * 文章的点赞（喜欢）数
     * 默认：0
     */
    @ApiModelProperty(value = "点赞数", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer articleLike;

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
     * 文章创建时间
     * 默认：当前时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createTime;

    /**
     * 文章修改时间
     * 默认：数据库更新时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdate;
}
