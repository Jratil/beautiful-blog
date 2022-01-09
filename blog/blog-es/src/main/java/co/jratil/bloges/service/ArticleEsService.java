package co.jratil.bloges.service;

import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dto.ArticleArchiveDTO;
import co.jratil.bloges.entity.ArticleEsRequest;
import co.jratil.bloges.enums.IndexEnum;
import co.jratil.bloges.util.IndexUtils;
import co.jratil.bloges.wrapper.condition.ArticleConditionWrapper;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static co.jratil.blogcommon.util.FieldUtils.fieldName;
import static co.jratil.bloges.wrapper.result.ResultWrapper.wrapperList;

/**
 * @author wenjunjun9
 * @created 2021/12/4 21:32
 */
@Service
public class ArticleEsService {

    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    /**
     * 根据多个条件批量查找
     *
     * @param request 条件组合
     * @return 结果列表
     */
    @Nullable
    public List<Article> searchBatch(ArticleEsRequest request) {

        NativeSearchQuery query = new ArticleConditionWrapper()
                .all(request);

        SearchHits<Article> searchHits =
                esRestTemplate.search(query, Article.class, IndexUtils.indexName(IndexEnum.Article));

        return wrapperList(searchHits, Article.class);
    }

    public Article searchById(Integer articleId) {

        NativeSearchQuery query = new ArticleConditionWrapper().singleTerm(Article::getArticleId, articleId);

        SearchHits<Article> searchHits =
                esRestTemplate.search(query, Article.class, IndexUtils.indexName(IndexEnum.Article));

        List<Article> articleList = wrapperList(searchHits, Article.class);

        return CollectionUtils.isEmpty(articleList)
                ? null
                : articleList.get(0);
    }

    /**
     * 查询归档，按月归档
     *
     * @param authorId 作者id
     * @param onlyVisible 是否只查询可见的文章
     * @return 归档结果
     */
    public List<ArticleArchiveDTO> searchArchive(Integer authorId, boolean onlyVisible) {

        DateHistogramAggregationBuilder aggregationBuilder = AggregationBuilders
                .dateHistogram("archive")
                .calendarInterval(DateHistogramInterval.MONTH)
                .format("yyyy-MM")
                .field(fieldName(Article::getCreateTime));

        NativeSearchQuery query = new ArticleConditionWrapper(onlyVisible)
                .singleTerm(Article::getAuthorId, authorId)
                .setPageable(PageRequest.of(1, 1));
        query.addAggregation(aggregationBuilder);

        SearchHits<Article> searchHits = esRestTemplate.search(query, Article.class, IndexUtils.indexName(IndexEnum.Article));
        Aggregations aggregations = searchHits.getAggregations();

        if (aggregations != null) {

            ParsedDateHistogram parsedDateHistogram = aggregations.get("archive");

            if (parsedDateHistogram != null) {

                List<? extends Histogram.Bucket> buckets = parsedDateHistogram.getBuckets();

                return buckets.stream().map(bucket -> {
                    ArticleArchiveDTO dto = new ArticleArchiveDTO();
                    dto.setArchive(bucket.getKeyAsString());
                    dto.setCount((int) bucket.getDocCount());
                    return dto;
                }).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}
