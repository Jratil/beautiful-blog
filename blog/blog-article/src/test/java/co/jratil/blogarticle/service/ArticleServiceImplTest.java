package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import co.jratil.blogapi.service.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class ArticleServiceImplTest {

    @Autowired
    ArticleService articleService;

    MockHttpServletRequest request;

    private PageParam pageParam;

    @Before
    public void before() {
        pageParam = new PageParam(1, 10);
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2wiOiJST0xFX1VTRVIiLCJpc3MiOiJhZG1pbiIsImlhdCI6MTU3NjA0NDU1OCwiZXhwIjoxNTc2MTMwOTU4LCJzdWIiOiIxMDAwMDkifQ.wwZjcN6WjthyApb_7d8Psg2Uuxyl3ZJsl8apKO69nCE");
    }

    @Test
    void selectArticleListByAuthorId() throws JsonProcessingException {
        PageParam pageParam = new PageParam(1, 3);
        PageInfo<ArticleDTO> list = articleService.listByAuthorId(pageParam, 100009, true);
        System.out.println(new ObjectMapper().writeValueAsString(list));
    }

    @Test
    void selectArticleArchive() throws JsonProcessingException {
        PageParam pageParam = new PageParam(1, 10);
        PageInfo<ArticleDTO> info = articleService.listByArchive(pageParam, "2019年09月", 100009, true);
        System.out.println(new ObjectMapper().writeValueAsString(info));
    }


    @Test
    void selectArticleArchiveMonth() throws JsonProcessingException {

        List<Map<String, Object>> list = articleService.listArchiveMonth(100009, true);
        System.out.println(new ObjectMapper().writeValueAsString(list));
    }

    @Test
    void selectArchiveMonth() {

//        List<String> list = articleService.listArchiveMonth(100009);
//        System.out.println(list);
    }

    @Test
    void selectArticleByArchive() {
//        PageInfo<ArticleDTO> info = articleService.listByArchive("2019年06月", 100009);
//        print(info);
    }

    private void print(List<Object> list) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void print(PageInfo<ArticleDTO> list) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}