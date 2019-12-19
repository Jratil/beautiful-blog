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
@TableName("t_article")
public class Article implements Serializable {

    private static final long serialVersionUID = -4349660067778454895L;

    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
    private Integer articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章副标题，在文章列表展示
     */
    private String articleSubtitle;

    /**
     * 文章是否对他人可见，默认：0
     * 0：可见
     * 1：不可见
     */
    private Boolean articleVisible;

    /**
     * 文章的点赞（喜欢）数
     * 默认：0
     */
    private Integer articleLike;

    /**
     * 作者的id
     */
    private Integer authorId;

    /**
     * 类目的id
     */
    private Integer categoryId;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章创建时间
     * 默认：当前时间
     */
    private Date createTime;

    /**
     * 文章修改时间
     * 默认：数据库更新时间
     */
    private Date lastUpdate;

}