package co.jratil.blogcontroller.controller.article;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import co.jratil.blogapi.service.ArticleService;
import co.jratil.blogcommon.response.ResponseUtils;
import co.jratil.blogcommon.response.ResponseVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 16:20
 */
@Api(value = "CommonArticleController", tags = "0-公开的接口，不用登录")
@RequestMapping("/common")
@RestController
public class CommonArticleController {

    @DubboReference
    private ArticleService  articleService;

    @RequestMapping("/")
    public ResponseVO mainPage(HttpServletRequest request, HttpServletResponse response) {
        PageParam pageParam = new PageParam(1, 10);
        PageInfo<ArticleDTO> allArticle = articleService.list(pageParam);
        return ResponseUtils.success(allArticle);
    }
}
