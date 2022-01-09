package co.jratil.bloges.wrapper.condition

import co.jratil.blogcommon.util.FieldUtils
import co.jratil.blogcommon.func.IGetter
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery

/**
 * @author jun
 * @created 2021/12/11 16:19
 */
abstract class ConditionWrapper {
    protected var boolQueryBuilder = QueryBuilders.boolQuery()

    /**
     * 构建单个字段条件 精确 查询
     *
     * @param fn    Article::getArticleId
     * @param value value
     * @return Query
     */
    fun <T> singleTerm(fn: IGetter<T>, value: Any): NativeSearchQuery {
        boolQueryBuilder.must(QueryBuilders.termQuery(FieldUtils.fieldName(fn), value))
        return NativeSearchQuery(boolQueryBuilder)
    }

    /**
     * 构建单个字段条件 模糊 匹配
     *
     * @param fn    Article::getArticleId
     * @param value value
     * @return Query
     */
    fun <T> singleMatch(fn: IGetter<T>, value: Any): NativeSearchQuery {
        boolQueryBuilder.must(QueryBuilders.matchQuery(FieldUtils.fieldName(fn), value))
        return NativeSearchQuery(boolQueryBuilder)
    }

    /**
     * 构建所有查询条件
     *
     * @param t
     * @return Query
     */
    abstract fun <T> all(t: T): NativeSearchQuery
}