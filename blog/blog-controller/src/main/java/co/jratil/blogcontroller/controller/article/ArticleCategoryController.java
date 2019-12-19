package co.jratil.blogcontroller.controller.article;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.response.ResponseEnum;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.service.ArticleCategoryService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value = "ArticleCategoryController", tags = "2-类目相关接口")
@Slf4j
@RestController
@RequestMapping("/category")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ArticleCategoryController extends AbstractController {

    @Reference
    private ArticleCategoryService categoryService;

    @ApiOperation(value = "查找类目", notes = "通过类目id来查找类目", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类目id", name = "categoryId", paramType = "path"),
    })
    @GetMapping("/query/{categoryId}")
    public ResponseVO queryCategoryById(@PathVariable("categoryId") Integer categoryId) {

        // 检查参数
        checkParam(categoryId, "categoryId", this.getClass());

        ArticleCategory category = categoryService.getById(categoryId);
        // 检查用户是否是自己，如果不是自己并且该类目不公开，则不可见抛出异常
        this.checkMeAndVisible(category.getAuthorId(), category.getCategoryVisible(), ResponseEnum.CATEGORY_NOT_EXIST);

        return ResponseUtils.success(category);
    }

    @ApiOperation(value = "查找类目", notes = "通过类目名称来查找类目", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "authorId", paramType = "path"),
            @ApiImplicitParam(value = "类目名称", name = "categoryName", paramType = "path")
    })
    @GetMapping("/query/{authorId}/{categoryName}")
    public ResponseVO queryCategoryByNameAndAuthorId(@PathVariable("categoryName") String categoryName,
                                                    @PathVariable("authorId") Integer authorId) {

        checkParam(categoryName, "categoryName", this.getClass());
        checkParam(authorId, "authorId", this.getClass());

        ArticleCategory category = categoryService.getByNameAndAuthorId(categoryName, authorId);
        // 检查用户是否是自己，如果不是自己并且该类目不公开，则抛出异常
        this.checkMeAndVisible(category.getAuthorId(), category.getCategoryVisible(), ResponseEnum.CATEGORY_NOT_EXIST);

        return ResponseUtils.success(category);
    }

    @ApiOperation(value = "批量查找类目", notes = "分页-批量查找用户所创建的所有类目", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "authorId", paramType = "path")
    })
    @PostMapping("/page/{authorId}")
    public ResponseVO pageQueryByAuthorId (@PathVariable("authorId") Integer authorId) {

        this.checkParam(authorId, "authorId", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<ArticleCategory> categoryList = categoryService.listByAuthorId(pageParam, authorId, getVisible(authorId));

        return ResponseUtils.success(categoryList);
    }

    @ApiOperation(value = "新增类目", notes = "新增类目时使用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleCategory", value = "类目实体类", dataTypeClass = ArticleCategory.class, paramType = "body")
    })
    @PostMapping("/add")
    public ResponseVO addCategory(@RequestBody ArticleCategory category, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("【类目管理】添加类目出错，参数错误，category={}", category);
            ResponseUtils.error(ResponseEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        // 重新设置用户id，直接从后端取
        category.setAuthorId(SecurityUtils.getAuthorId());
        category.setCreateTime(new Date());
        categoryService.save(category);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "修改类目", notes = "修改类目名称时使用", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleCategory", value = "类目实体类", dataTypeClass = ArticleCategory.class, paramType = "body")
    })
    @PutMapping("/update")
    public ResponseVO updateCategory(@RequestBody ArticleCategory category, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("【类目管理】修改类目出错，参数错误，category={}", category);
            ResponseUtils.error(ResponseEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        ArticleCategory result = categoryService.getById(category.getCategoryId());
        if (!this.checkMe(result.getAuthorId())) {
            log.error("【类目操作】修改类目，没有权限，category={}", category);
            throw new GlobalException(ResponseEnum.NOT_AUTHORITY);
        }

        categoryService.update(category);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除类目", notes = "删除类目名称时使用", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类目id", name = "categoryId", paramType = "path"),
    })
    @DeleteMapping("/delete/{categoryId}")
    public ResponseVO deleteCategory(@PathVariable("categoryId") Integer categoryId) {

        this.checkParam(categoryId, "categoryId", this.getClass() );

        ArticleCategory result = categoryService.getById(categoryId);
        if (!this.checkMe(result.getAuthorId())) {
            log.error("【类目操作】修改类目，没有权限，categoryId={}", categoryId);
            throw new GlobalException(ResponseEnum.NOT_AUTHORITY);
        }
        categoryService.remove(categoryId);

        return ResponseUtils.success();
    }
}
