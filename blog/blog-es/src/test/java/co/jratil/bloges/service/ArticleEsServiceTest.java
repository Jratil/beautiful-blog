package co.jratil.bloges.service;

import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dto.ArticleArchiveDTO;
import co.jratil.bloges.entity.ArticleEs;
import co.jratil.bloges.entity.ArticleEsRequest;
import co.jratil.bloges.enums.IndexEnum;
import co.jratil.bloges.util.IndexUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author wenjunjun9
 * @created 2021/12/12 16:25
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleEsServiceTest {

    @Autowired
    private ArticleEsService articleEsService;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Test
    public void index() {
        boolean b = restTemplate.indexOps(ArticleEs.class).create();
        System.out.println(b);
    }

    @Test
    public void initData() {
        Article article = new Article();
        article.setArticleId(1);
        article.setArticleTitle("test es");
        article.setArticleSubtitle("");
        article.setArticleVisible(true);
        article.setArticleLike(0);
        article.setAuthorId(1);
        article.setCategoryId(1);
        article.setArticleContent("test content, this is content, and is very long");
        article.setCreateTime(new Date());
        article.setLastUpdate(new Date());

        Article article2 = new Article();
        article2.setArticleId(2);
        article2.setArticleTitle("test es 2");
        article2.setArticleSubtitle("");
        article2.setArticleVisible(false);
        article2.setArticleLike(0);
        article2.setAuthorId(1);
        article2.setCategoryId(1);
        article2.setArticleContent("test content, the visible is false, only visible for author or be share user");
        article2.setCreateTime(new Date());
        article2.setLastUpdate(new Date());

        restTemplate.save(Arrays.asList(article, article2), IndexUtils.indexName(IndexEnum.Article));
    }

    @Test
    public void test_allCondition() {
        ArticleEsRequest request = new ArticleEsRequest();
        request.setAuthorId(1);

        List<Article> articleList = articleEsService.searchBatch(request);
        assertNotNull(articleList);
    }

    @Test
    public void test_searchArchive() {
        List<ArticleArchiveDTO> archiveDTOList = articleEsService.searchArchive(1, false);
        assertNotNull(archiveDTOList);
    }
}