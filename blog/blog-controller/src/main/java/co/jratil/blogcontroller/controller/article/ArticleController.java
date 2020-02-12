package co.jratil.blogcontroller.controller.article;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.service.ArticleCategoryService;
import co.jratil.blogapi.service.ArticleService;
import co.jratil.blogapi.utils.SessionUtil;
import co.jratil.blogcontroller.controller.AbstractController;
import co.jratil.blogsecurity.util.SecurityUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Api(value = "ArticleController", tags = "3-文章处理接口")
@Slf4j
@RestController
@RequestMapping("/article")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ArticleController extends AbstractController<Article> {

    @Reference
    private ArticleService articleService;

    @Reference
    private ArticleCategoryService categoryService;

    @GetMapping("/main_page")
    public ResponseVO mainPage(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = SessionUtil.getHttpSession();
//        Integer authorId = (Integer) session.getAttribute("authorId");

        PageParam pageParam = new PageParam(getPage(), getPage());
        PageInfo<ArticleDTO> date = articleService.listByAuthorId(pageParam, SecurityUtils.getAuthorId(), true);

        return ResponseUtils.success(date);
    }

    @ApiOperation(value = "查找文章", notes = "根据文章id查找文章接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章id", name = "articleId", paramType = "path")
    })
    @GetMapping("/{articleId}")
    public ResponseVO queryArticle(@PathVariable("articleId") Integer articleId) {
        this.checkParam(articleId, "articleId", this.getClass());

        ArticleDTO articleDTO = articleService.getById(SecurityUtils.getAuthorId(), articleId);
        this.checkMeAndVisible(articleDTO.getAuthorId(), articleDTO.getArticleVisible(), ResponseEnum.ARTICLE_NOT_EXIST);

        return ResponseUtils.success(articleDTO);
    }

    @ApiOperation(value = "批量查找文章-类目id", notes = "根据类目id分页查找指定类目中文章接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类目id", name = "categoryId", paramType = "path")
    })
    @GetMapping("/page/category/{categoryId}")
    public ResponseVO pageQueryByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        this.checkParam(categoryId, "categoryId", this.getClass());

        ArticleCategory category = categoryService.getById(categoryId);

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<ArticleDTO> articleList = articleService.listByCategoryId(pageParam, categoryId, getVisible(category.getAuthorId()));

        return ResponseUtils.success(articleList);
    }

    @ApiOperation(value = "批量查找文章-用户id", notes = "根据用户id分页查找文章接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "authorId", paramType = "path")
    })
    @GetMapping("/page/{authorId}")
    public ResponseVO pageQueryByAuthorId(@PathVariable("authorId") Integer authorId) {
        this.checkParam(authorId, "authorId", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<ArticleDTO> articleDTOList = articleService.listByAuthorId(pageParam, authorId, getVisible(authorId));

        return ResponseUtils.success(articleDTOList);
    }

    @ApiOperation(value = "查找归档的月份", notes = "根据用户id分页查找所有的归档月份，不分页", httpMethod = "POST")
    @GetMapping("/archives/{authorId}")
    public ResponseVO listArchiveMonthByAuthorId(@PathVariable("authorId") Integer authorId) {
        this.checkParam(authorId, "authorId", this.getClass());

        List<Map<String, Object>> maps = articleService.listArchiveMonth(authorId, getVisible(authorId));

        return ResponseUtils.success(maps);
    }

    @ApiOperation(value = "批量查找文章-通过归档月份", notes = "根据用户id和归档月份分页查找文章接口,月份格式：YYYY年MM月,并且进行url编码", httpMethod = "POST")
    @GetMapping("/page/archive/{authorId}/{month}")
    public ResponseVO pageQueryByArchiveMonth(@PathVariable("authorId") Integer authorId,
                                              @PathVariable("month") String month) {
        this.checkParam(authorId, "authorId", this.getClass());
        this.checkParam(month, "month", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<ArticleDTO> info = articleService.listByArchive(pageParam, month, authorId, getVisible(authorId));

        return ResponseUtils.success(info);
    }

    @ApiOperation(value = "新增文章", notes = "新增文章接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleDTO", value = "文章dto", dataTypeClass = ArticleDTO.class, paramType = "body")
    })
    @PostMapping()
    public ResponseVO add(@RequestBody @Validated ArticleDTO articleDTO, BindingResult result) {

        if (result.hasErrors()) {
            log.error("【文章操作】添加文章，文章参数不正确，articleDTO={}", articleDTO);
            throw new GlobalException(result.getFieldError().getDefaultMessage());
        }

        articleDTO.setAuthorId(SecurityUtils.getAuthorId());
        articleService.save(articleDTO);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "修改文章", notes = "根据文章id 修改文章", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleDTO", value = "文章dto", dataTypeClass = ArticleDTO.class, paramType = "body")
    })
    @PutMapping()
    public ResponseVO update(@RequestBody @Validated ArticleDTO articleDTO, BindingResult result) {
        if (result.hasErrors()) {
            log.error("【文章操作】修改文章，文章参数不正确，articleDTO={}", articleDTO);
            throw new GlobalException(ResponseEnum.PARAM_ERROR.getCode(), result.getFieldError().getDefaultMessage());
        }

        ArticleDTO dto = articleService.getById(SecurityUtils.getAuthorId(), articleDTO.getArticleId());
        if (!this.checkMe(dto.getAuthorId())) {
            log.error("【文章操作】修改文章，没有权限，articleDTO={}", articleDTO);
            throw new GlobalException(ResponseEnum.NOT_AUTHORITY);
        }

        articleService.update(articleDTO);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除文章", notes = "根据文章id 删除文章", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章id", name = "articleId", paramType = "path")
    })
    @DeleteMapping("/{articleId}")
    public ResponseVO delete(@PathVariable("articleId") Integer articleId) {

        this.checkParam(articleId, "articleId", this.getClass());

        ArticleDTO articleDTO = articleService.getById(SecurityUtils.getAuthorId(), articleId);
        if (!this.checkMe(articleDTO.getAuthorId())) {
            log.error("【文章操作】删除文章，没有权限，articleDTO={}", articleDTO);
            throw new GlobalException(ResponseEnum.NOT_AUTHORITY);
        }
        articleService.remove(articleId);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "查询是否点赞", notes = "查询是否点赞接口，返回布尔值", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章id", name = "articleId", paramType = "path")
    })
    @GetMapping("/like/{articleId}")
    public ResponseVO getLikeStatus(@PathVariable("articleId")Integer articleId) {
        checkParam(articleId, "articleId", getClass());

        Boolean likeStatus = articleService.getLikeStatus(SecurityUtils.getAuthorId(), articleId);

        return ResponseUtils.success(likeStatus);
    }

    @ApiOperation(value = "切换喜欢状态", notes = "切换喜欢接口，返回点赞后的点赞数，只需把文章id传入，会自动判断是否点赞", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章id", name = "articleId", paramType = "path")
    })
    @PutMapping("/like/{articleId}")
    public ResponseVO addLike(@PathVariable("articleId") Integer articleId) {
        this.checkParam(articleId, "articleId", this.getClass());

        Integer articleLike = articleService.switchLike(SecurityUtils.getAuthorId(), articleId);

        return ResponseUtils.success(articleLike);
    }
}

