package co.jratil.bloges.service;

import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.bloges.entity.ArticleSearch;
import co.jratil.bloges.repository.ArticleSearchRepository;
import co.jratil.blogapi.service.ArticleSearchService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

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

    @Autowired
    private ElasticsearchOperations operations;

    @Override
    public Page<Article> findByTitleOrContentAndVisible(String titleWord, String contentWord, Boolean visible, Pageable pageable) {

        Page<ArticleSearch> articleSearchList =
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
        List<ArticleSearch> list = new ArrayList<>();
        ids.forEach(id -> {
            ArticleSearch o = new ArticleSearch();
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
    private ArticleSearch convertTo(Article article) {
        ArticleSearch articleSearch = new ArticleSearch();
        BeanUtils.copyProperties(article, articleSearch);
        return articleSearch;
    }

    /**
     * 将 ArticleSearch 转换成 Article
     *
     * @param articleSearch 要转换的 ArticleSearch
     * @return
     */
    private Article convertFor(ArticleSearch articleSearch) {
        Article article = new Article();
        BeanUtils.copyProperties(articleSearch, article);
        return article;
    }

    /**
     * 将 Page<ArticleSearch> 转换成 Page<Article>, 方便其他地方接收
     *
     * @param searches 原始的 Page 对象
     * @return
     */
    private Page<Article> convertForList(Page<ArticleSearch> searches) {
        List<Article> articles = new ArrayList<>();
        searches.getContent().forEach(item -> {
            Article article = convertFor(item);
            articles.add(article);
        });
        Page<Article> articlePage = new PageImpl<>(articles,
                searches.getPageable(), searches.getTotalElements());
        return articlePage;
    }
}
