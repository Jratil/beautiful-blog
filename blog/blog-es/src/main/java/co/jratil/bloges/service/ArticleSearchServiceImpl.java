package co.jratil.bloges.service;

import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.service.ArticleSearchService;
import co.jratil.bloges.entity.ArticleEs;
import co.jratil.bloges.repository.ArticleSearchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-07-18 14:49
 */
@Service
@Component
public class ArticleSearchServiceImpl implements ArticleSearchService {

    @Autowired
    private ArticleSearchRepository articleSearchRepository;

    @Autowired
    ElasticsearchRestTemplate template;

    @Override
    public Page<Article> findByTitleOrContentAndVisible(String titleWord, String contentWord, Boolean visible, Pageable pageable) {

        Page<ArticleEs> articleSearchList =
                articleSearchRepository.findByArticleVisibleOrArticleTitleAndArticleContent(visible, titleWord, contentWord, pageable);

        return convertForList(articleSearchList);
    }

    @Override
    public void addArticleSearch(Article article) {
        articleSearchRepository.save(convertTo(article));
    }

    @Override
    public void updateById(Article article) {
        articleSearchRepository.save(convertTo(article));
    }

    @Override
    public void deleteById(Integer id) {
        articleSearchRepository.deleteById(id);
    }

    @Override
    public void deleteBatch(List<Integer> ids) {
        List<ArticleEs> list = new ArrayList<>();
        ids.forEach(id -> {
            ArticleEs o = new ArticleEs();
            o.setArticleId(id);
            list.add(o);
        });
        articleSearchRepository.deleteAll(list);
    }

    /**
     * 将 Article 转换成 ArticleSearch
     *
     * @param article 要转换的 Article
     * @return
     */
    private ArticleEs convertTo(Article article) {
        ArticleEs articleEs = new ArticleEs();
        BeanUtils.copyProperties(article, articleEs);
        return articleEs;
    }

    /**
     * 将 ArticleSearch 转换成 Article
     *
     * @param articleEs 要转换的 ArticleSearch
     * @return
     */
    private Article convertFor(ArticleEs articleEs) {
        Article article = new Article();
        BeanUtils.copyProperties(articleEs, article);
        return article;
    }

    /**
     * 将 Page<ArticleSearch> 转换成 Page<Article>, 方便其他地方接收
     *
     * @param searches 原始的 Page 对象
     * @return
     */
    private Page<Article> convertForList(Page<ArticleEs> searches) {
        List<Article> articles = new ArrayList<>();
        searches.getContent().forEach(item -> {
            Article article = convertFor(item);
            articles.add(article);
        });
        return new PageImpl<>(articles,
                searches.getPageable(), searches.getTotalElements());
    }
}
