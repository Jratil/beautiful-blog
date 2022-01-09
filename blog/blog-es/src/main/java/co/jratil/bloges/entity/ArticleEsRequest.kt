package co.jratil.bloges.entity

import java.util.*

/**
 * @author jun
 * @created 2021/12/11 22:04
 */
data class ArticleEsRequest(
    var pageIndex: Int = 0,
    var pageSize: Int = 10,

    /**
     * 文章id
     */
    var articleId: Int? = null,

    /**
     * 文章标题
     */
    var articleTitle: String? = null,

    /**
     * 文章是否对他人可见，默认：0
     * 0：可见
     * 1：不可见
     */
    var articleVisible: Boolean = true,

    /**
     * 作者的id
     */
    var authorId: Int? = null,

    /**
     * 类目的id
     */
    var categoryId: Int? = null,

    /**
     * 文章内容
     */
    var articleContent: String? = null,

    /**
     * 时间范围
     */
    var beginDate: Date? = null,
    var endDate: Date? = null
)