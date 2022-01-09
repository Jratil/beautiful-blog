package co.jratil.bloges.repository;

import co.jratil.bloges.entity.ArticleEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-07-18 14:36
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<ArticleEs, Integer> {

    //@Query("\"bool\" : {\"must\": [{\"query_string\": {\"query\": \"?0\",\"fields\": [\"articleVisible\"]}}],\"should\": [{\"query_string\" : {\"query\" : \"?1\", \"fields\" : [ \"articleTitle\" ]}},{\"query_string\" : {\"query\" : \"?2\", \"fields\" : [\"articleContent\"]}}]}")
    Page<ArticleEs> findByArticleVisibleOrArticleTitleAndArticleContent(Boolean visible, String titleWord, String contentWord, Pageable pageable);
}
