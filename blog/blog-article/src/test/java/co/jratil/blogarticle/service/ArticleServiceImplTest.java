package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import co.jratil.blogapi.service.ArticleSearchService;
import co.jratil.blogapi.service.ArticleService;
import co.jratil.blogarticle.mapper.ArticleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ArticleServiceImplTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleMapper articleMapper;

    @Reference
    ArticleSearchService articleSearchService;

    @Test
    public void importAll() {
        List<Article> articles = articleMapper.selectList(new QueryWrapper<>());
        articles.forEach(article ->
                articleSearchService.addArticleSearch(article)
        );
    }

    @Test
    public void getTest() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Article> page = articleSearchService.findByTitleOrContentAndVisible(null, "测试1", true, pageable);
        page.getContent().forEach(item -> {
            System.out.println("[" + item.getArticleTitle());
            System.out.println(item.getArticleContent());
            System.out.println(item.getArticleVisible() + "]\n");
        });
        System.out.println(page.getTotalElements());
    }
}