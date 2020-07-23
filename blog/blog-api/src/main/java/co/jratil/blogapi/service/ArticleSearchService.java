package co.jratil.blogapi.service;

import co.jratil.blogapi.entity.dataobject.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-07-18 14:48
 */
public interface ArticleSearchService {

    Page<Article> findByTitleOrContentAndVisible(String titleWord, String contentWord, Boolean visible, Pageable pageable);

    void addArticleSearch(Article article);

    void updateById(Article article);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);
}
