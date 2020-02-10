package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Comment;
import co.jratil.blogapi.entity.dto.CommentDTO;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.service.ArticleService;
import co.jratil.blogapi.service.AuthorService;
import co.jratil.blogapi.service.CommentService;
import co.jratil.blogarticle.constant.ArticleConstant;
import co.jratil.blogarticle.mapper.CommentMapper;
import co.jratil.blogsecurity.util.JwtUtils;
import co.jratil.blogsecurity.util.SecurityUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-01-02 21:53
 */
@Slf4j
@Service(interfaceClass = CommentService.class)
@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Reference
    private AuthorService authorService;

    @Autowired
    private ArticleService articleService;

    @Override
    public CommentDTO getById(Integer commentId) {

        QueryWrapper<Comment> wrapper = this.buildWrapper();
        wrapper.lambda()
                .eq(Comment::getCommentId, commentId);
        Comment comment = commentMapper.selectOne(wrapper);

        if (comment == null) {
            log.info("【文章评论】查询出的评论为null，commentId = {}", commentId);
            throw new GlobalException(ResponseEnum.COMMENT_NOT_EXIST);
        }

        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);

        // 查询评论者名称
        String authorName = authorService.getById(commentDTO.getAuthorId()).getAuthorName();
        commentDTO.setAuthorName(authorName);

        if (ArticleConstant.LEVEL_2.equals(commentDTO.getCommentLevel())) {
            // 如果是二级评论则需查询被回复者名称
            String replyAuthorName = authorService.getById(commentDTO.getAuthorId()).getAuthorName();
            commentDTO.setReplyCommentAuthorName(replyAuthorName);
        }

        return commentDTO;
    }

    @Override
    public PageInfo<CommentDTO> getCommentByArticleId(PageParam pageParam, Integer articleId) {
        // 查询并且判断文章是否存在
        articleService.getById(articleId);

        QueryWrapper<Comment> wrapper = this.buildWrapper();
        wrapper.lambda()
                .eq(Comment::getArticleId, articleId)
                .eq(Comment::getCommentLevel, 1);

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Comment> comments = commentMapper.selectList(wrapper);

        return dosToDtos(comments);
    }

    @Override
    public PageInfo<CommentDTO> getCommentByParentCommentId(PageParam pageParam, Integer parentCommentId) {

        // 查询并且判断该评论是否存在
        this.getById(parentCommentId);

        QueryWrapper<Comment> wrapper = buildWrapper();
        wrapper.lambda()
                .eq(Comment::getParentCommentId, parentCommentId)
                .eq(Comment::getCommentLevel, 2);

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Comment> comments = commentMapper.selectList(wrapper);

        return secondDosToDtos(comments);
    }

    @Override
    public void save(Comment comment) {

        // 判断文章是否存在
        articleService.getById(comment.getArticleId());

        // 判断用户是否存在
        authorService.getById(comment.getAuthorId());

        // 如果是二级评论，则要判断父评论、回复人、回复的评论id 是否存在
        if (comment.getCommentLevel() == 2) {
            // 判断父评论是否存在
            if (commentMapper.selectById(comment.getParentCommentId()) == null) {
                throw new GlobalException(ResponseEnum.COMMENT_NOT_EXIST);
            }
            // 判断回复人是否存在
            authorService.getById(comment.getReplyCommentAuthorId());
            // 判断回复的评论是否存在
            if (commentMapper.selectById(comment.getReplyCommentId()) == null) {
                throw new GlobalException(ResponseEnum.COMMENT_NOT_EXIST);
            }
        }
        commentMapper.insert(comment);
    }

    @Override
    public void update(Comment comment) {
        // 不能修改评论，只能删除，先留着
    }

    @Override
    public void delete(Integer commentId) {
        // 逻辑删除
        commentMapper.deleteComment(commentId);
    }

    @Override
    public synchronized int switchPraise(Integer authorId, Integer commentId) {
        CommentDTO commentDTO = getById(commentId);
        int articleId = commentDTO.getArticleId();
        List<Integer> commentIds = commentMapper.selectPraiseCommentIds(authorId, articleId);

        int praise = commentMapper.selectPraiseNum(commentId);

        // 如果点赞列表里面包含了该评论，则取消点赞
        // 否则点赞+1
        if (commentIds.contains(commentId) && praise > 0) {
            praise = praise - 1;
            commentMapper.deletePraise(authorId, commentId);
            commentMapper.updatePraiseNum(commentId, praise);
        } else {
            praise = praise + 1;
            commentMapper.addPraise(authorId, articleId, commentId);
            commentMapper.updatePraiseNum(commentId, praise);
        }
        return praise;
    }

    private QueryWrapper<Comment> buildWrapper() {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("delete_status", false)
                .orderByDesc("create_time");
        return wrapper;
    }

    /**
     * 一级评论回复的转换
     * @param comments
     * @return
     */
    private PageInfo<CommentDTO> dosToDtos(List<Comment> comments) {
        PageInfo<Comment> doPage = new PageInfo<>(comments);

        List<CommentDTO> list = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    // 查出评论的用户名称
                    String authorName = authorService.getById(comment.getAuthorId()).getAuthorName();
                    commentDTO.setAuthorName(authorName);
                    return commentDTO;
                })
                .collect(Collectors.toList());

        PageInfo<CommentDTO> dtoPage = new PageInfo<>();
        BeanUtils.copyProperties(doPage, dtoPage);
        dtoPage.setList(list);
        dtoPage.setTotal(doPage.getTotal());

        return dtoPage;
    }

    /**
     * 二级评论回复的转换
     * @param comments
     * @return
     */
    private PageInfo<CommentDTO> secondDosToDtos(List<Comment> comments) {
        PageInfo<Comment> doPage = new PageInfo<>(comments);
        List<CommentDTO> list = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    // 查出评论的用户名称
                    String authorName = authorService.getById(comment.getAuthorId()).getAuthorName();
                    // 如果二级评论中评论被删除了，则显示为匿名评论并且设置内容显示已删除
                    if (comment.getDeleteStatus()) {
                        commentDTO.setAuthorId(-1);
                        commentDTO.setAuthorName(ArticleConstant.DELETE_COMMENT_NAME);
                        commentDTO.setContent(ArticleConstant.DELETE_COMMENT_CONTENT);
                        return commentDTO;
                    }

                    // 查出被回复的用户名称
                    String replyAuthorName = authorService.getById(comment.getReplyCommentAuthorId()).getAuthorName();
                    commentDTO.setAuthorName(authorName);
                    commentDTO.setReplyCommentAuthorName(replyAuthorName);
                    return commentDTO;
                })
                .collect(Collectors.toList());

        PageInfo<CommentDTO> dtoPage = new PageInfo<>();
        BeanUtils.copyProperties(doPage, dtoPage);
        dtoPage.setTotal(doPage.getTotal());
        dtoPage.setList(list);

        return dtoPage;
    }
}
