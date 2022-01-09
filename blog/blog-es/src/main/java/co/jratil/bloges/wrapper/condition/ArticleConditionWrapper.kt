package co.jratil.bloges.wrapper.condition

import cn.hutool.core.util.StrUtil
import co.jratil.blogapi.entity.dataobject.Article
import co.jratil.blogcommon.enums.ResponseEnum
import co.jratil.blogcommon.exception.GlobalException
import co.jratil.blogcommon.util.FieldUtils.method2Filed
import co.jratil.bloges.entity.ArticleEsRequest
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery
import org.springframework.data.elasticsearch.core.query.Query

/**
 * @author jun
 * @created 2021/12/12 16:01
 */
class ArticleConditionWrapper : ConditionWrapper {
    constructor()

    /**
     * 构建是否只查看开放的文章
     * true: 则构建条件，只允许查看开放的文章
     * false: 不构建条件，允许查看所有文章
     *
     * @param onlyVisible true: 只查看开放的文章，false: 查看所有文章
     */
    constructor(onlyVisible: Boolean) {
        if (onlyVisible) {
            super.boolQueryBuilder.must(
                QueryBuilders.termQuery(method2Filed(Article::getArticleVisible.name), true)
            )
        }
    }

    override fun <T> all(t: T): NativeSearchQuery {
        // 校验参数是否非法
        if (t !is ArticleEsRequest) {
            throw GlobalException(ResponseEnum.PARAM_ERROR)
        }
        val request = t as ArticleEsRequest

        // 根据文章id查询
        if (request.articleId != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Article::getArticleId.name), request.articleId
                )
            )
        }
        // 根据作者id查询
        if (request.authorId != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Article::getAuthorId.name), request.authorId
                )
            )
        }

        // 根据可见，在构造函数已经有了，自己选择
        if (request.articleVisible != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Article::getArticleVisible.name), request.articleContent
                )
            )
        }

        /// 根据文章标题模糊查询
        if (StrUtil.isNotEmpty(request.articleTitle)) {
            boolQueryBuilder.must(
                QueryBuilders.matchQuery(
                    method2Filed(Article::getArticleTitle.name), request.articleTitle
                )
            )
        }
        // 根据类目id查询
        if (request.categoryId != null) {
            boolQueryBuilder.must(
                QueryBuilders.termQuery(
                    method2Filed(Article::getCategoryId.name), request.categoryId
                )
            )
        }
        // 根据时间范围查询
        if (request.beginDate != null) {
            boolQueryBuilder.must(
                QueryBuilders.rangeQuery(
                    method2Filed(Article::getCreateTime.name)
                )
                    .gte(request.beginDate)
            )
        }
        // 根据时间范围查询
        if (request.endDate != null) {
            boolQueryBuilder.must(
                QueryBuilders.rangeQuery(
                    method2Filed(Article::getCreateTime.name)
                )
                    .gte(request.endDate)
            )
        }

        val query = NativeSearchQuery(boolQueryBuilder)
        query.setPageable<Query>(PageRequest.of(request.pageIndex, request.pageSize))
        return query
    }

}