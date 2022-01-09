package co.jratil.bloges.entity;

import com.google.gson.internal.$Gson$Types;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-07-18 14:38
 */
@Data
@Document(indexName = "article", shards = 1, replicas = 0)
public class ArticleEs implements Serializable {

    private static final long serialVersionUID = -5250856767979341350L;

    /**
     * 文章id
     */
    @Id
    private Integer articleId;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String articleTitle;

    /**
     * 文章副标题，在文章列表展示
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String articleSubtitle;

    /**
     * 文章是否可见，不分词
     * 0：可见
     * 1：不可见
     */
    @Field(type = FieldType.Boolean)
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
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String articleContent;

    /**
     * 文章创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 文章修改时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdate;
}
