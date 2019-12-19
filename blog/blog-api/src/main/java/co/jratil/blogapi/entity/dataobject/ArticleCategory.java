package co.jratil.blogapi.entity.dataobject;

import co.jratil.blogapi.utils.serializer.Date2LongSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@TableName("t_article_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategory implements Serializable {

    private static final long serialVersionUID = 329736111792845096L;

    /**
     * 类目id
     * 0：默认自动生成的名为“默认”的类目
     * 其他新建类目id在 0 上自增
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 类目的名称
     */
    private String categoryName;

    private Boolean categoryVisible;

    /**
     * 作者的id
     */
    private Integer authorId;

    /**
     * 类目创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
}