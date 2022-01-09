package co.jratil.bloges.wrapper.result

import lombok.extern.slf4j.Slf4j
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.elasticsearch.core.SearchHits

/**
 * @author wenjunjun9
 * @created 2021/12/11 23:36
 */
@Slf4j
object ResultWrapper {
    private val log: Logger = LoggerFactory.getLogger(ResultWrapper.javaClass)

    /**
     * es 查询结果转换成 list 返回
     *
     * @param searchHits  es 直接查询出来的结果
     * @param resultClass 转换后的实体类
     * @param <T>         eg: ArticleEs.class
     * @param <R>         eg: Article.class
     * @return
    </R></T> */
    @JvmStatic
    fun <T : Any, R : Any> wrapperList(searchHits: SearchHits<T>, resultClass: Class<R>): List<R>? {
        val searchHitList = searchHits.searchHits

        val collect = searchHitList?.map {
                obj: SearchHit<T> -> obj.content
        }

        return collect?.map { item: T ->
            val instance = resultClass.getDeclaredConstructor().newInstance()
            BeanUtils.copyProperties(item, instance)
            instance
        }
    }

    /**
     * 将 es 聚合结果转换
     * 其中
     *
     * @param searchHits 查询出来的结果
     * @param aggName 要查询的聚合的名称
     * @param resultClass 返回结果的 class
     *
     */
    @JvmStatic
    fun <T : Any> wrapperParsedDateHistogram(
        searchHits: SearchHits<Any>?,
        aggName: String,
        resultClass: Class<T>
    ): List<T>? {

        val aggregations = searchHits?.aggregations
        val parsedDateHistogram = aggregations?.get<ParsedDateHistogram>(aggName)
        val buckets = parsedDateHistogram?.buckets

        return buckets?.map { bucket: Histogram.Bucket? ->
            val dto = resultClass.getDeclaredConstructor().newInstance()
            val fields = resultClass.fields
            fields.forEach {
                if (it.name.equals(bucket?.keyAsString)) {
                    it.set(dto, bucket?.docCount)
                }
            }
            dto
        }
    }
}