package co.jratil.blogcontroller.controller.article;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Comment;
import co.jratil.blogapi.entity.dto.CommentDTO;
import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.service.CommentService;
import co.jratil.blogcontroller.controller.AbstractController;
import co.jratil.blogsecurity.util.SecurityUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-01-31 20:52
 */
@Api(value = "CommentController", tags = "4-评论处理接口")
@Slf4j
@RestController
@RequestMapping("/comment")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ArticleCommentController extends AbstractController<Comment> {

    @Reference
    private CommentService commentService;

    @ApiOperation(value = "查找评论", notes = "根据评论id查找指定评论", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评论id", name = "commentId", paramType = "path")
    })
    @GetMapping("/{commentId}")
    public ResponseVO queryCommentById(@PathVariable("commentId") Integer commentId) {
        checkParam(commentId, "commentId", this.getClass());

        CommentDTO comment = commentService.getById(commentId);
        return ResponseUtils.success(comment);
    }

    @ApiOperation(value = "分页查找查找评论", notes = "根据文章id分页查找评论", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章id", name = "articleId", paramType = "path")
    })
    @GetMapping("/page/article/{articleId}")
    public ResponseVO pageQueryByArticleId(@PathVariable("articleId") Integer articleId) {
        checkParam(articleId, "articleId", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<CommentDTO> comments = commentService.getCommentByArticleId(pageParam, articleId);

        return ResponseUtils.success(comments);
    }

    @ApiOperation(value = "分页查找评论", notes = "根据一级评论id分页查找评论", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评论id", name = "commentId", paramType = "path")
    })
    @GetMapping("/page/comment/{commentId}")
    public ResponseVO pageQueryByParentCommentId(@PathVariable("commentId") Integer commentId) {
        checkParam(commentId, "commentId", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<CommentDTO> comments = commentService.getCommentByParentCommentId(pageParam, commentId);

        return ResponseUtils.success(comments);
    }

    @ApiOperation(value = "添加评论", notes = "添加评论", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "commentDTO", name = "commentDTO", paramType = "body")
    })
    @PostMapping()
    public ResponseVO addComment(@Validated @RequestBody CommentDTO commentDTO, BindingResult result) {
        checkBindindResult(result, commentDTO);

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);

        commentService.save(comment);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除评论", notes = "根据评论id 删除评论", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评论id", name = "commentId", paramType = "path")
    })
    @DeleteMapping("/{commentId}")
    public ResponseVO delComment(@PathVariable("commentId") Integer commentId) {

        CommentDTO commentDTO = commentService.getById(commentId);
        checkMe(commentDTO.getAuthorId());

        commentService.delete(commentId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "切换喜欢", notes = "如果是点过了则会取消，没点过会点赞，自动切换，只需要把点赞的id传过来就好", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "评论id", name = "commentId", paramType = "path")
    })
    @PutMapping("/like/{commentId}")
    public ResponseVO addLike(@PathVariable("commentId") Integer commentId) {
        checkParam(commentId, "commentId", getClass());

        int praise = commentService.switchPraise(SecurityUtils.getAuthorId(), commentId);
        return ResponseUtils.success(praise);
    }
}

