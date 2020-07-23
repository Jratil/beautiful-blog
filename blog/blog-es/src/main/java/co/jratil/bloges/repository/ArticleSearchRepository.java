package co.jratil.bloges.repository;

import co.jratil.bloges.entity.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-07-18 14:36
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<ArticleSearch, Integer> {

    //@Query("\"bool\" : {\"must\": [{\"query_string\": {\"query\": \"?0\",\"fields\": [\"articleVisible\"]}}],\"should\": [{\"query_string\" : {\"query\" : \"?1\", \"fields\" : [ \"articleTitle\" ]}},{\"query_string\" : {\"query\" : \"?2\", \"fields\" : [\"articleContent\"]}}]}")
    Page<ArticleSearch> findByArticleVisibleOrArticleTitleAndArticleContent(Boolean visible, String titleWord, String contentWord, Pageable pageable);
}
