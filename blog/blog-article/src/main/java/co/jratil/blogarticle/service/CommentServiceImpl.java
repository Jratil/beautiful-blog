package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dataobject.Comment;
import co.jratil.blogapi.entity.dto.AuthorDTO;
import co.jratil.blogapi.entity.dto.CommentDTO;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.service.ArticleService;
import co.jratil.blogapi.service.AuthorService;
import co.jratil.blogapi.service.CommentService;
import co.jratil.blogarticle.constant.ArticleConstant;
import co.jratil.blogarticle.mapper.ArticleMapper;
import co.jratil.blogarticle.mapper.CommentMapper;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private ArticleMapper articleMapper;

    @Reference
    private AuthorService authorService;

    @Cacheable(value = "CommentService::getById", key = "#commentId")
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

        int result = commentMapper.selectLikeStatus(commentId, comment.getAuthorId());
        commentDTO.setHasLike(result > 0);

        // 查询评论者名称
        AuthorDTO authorDTO = authorService.getById(commentDTO.getAuthorId());
        String authorName = authorDTO.getAuthorName();
        String authorAvatar = authorDTO.getAuthorAvatar();
        commentDTO.setAuthorName(authorName);
        commentDTO.setAuthorAvatar(authorAvatar);

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
        this.articleExist(articleId);

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

        // 因为通过一级评论查出二级评论，对于删除了的二级评论会显示该评论已删除，所以需要把已删除状态的评论也查出来，不能用buildWrapper()
        LambdaQueryWrapper<Comment> wrapper = Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getParentCommentId, parentCommentId)
                .eq(Comment::getCommentLevel, 2);

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Comment> comments = commentMapper.selectList(wrapper);

        return secondDosToDtos(comments);
    }

    @Transactional
    @Override
    public void save(Comment comment) {

        // 判断文章是否存在
        this.articleExist(comment.getArticleId());

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

    @CacheEvict(value = "CommentService::getById", allEntries = true)
    @Transactional
    @Override
    public void update(Comment comment) {
        // 不能修改评论，只能删除，先留着
    }

    @CacheEvict(value = "CommentService::getById", allEntries = true)
    @Transactional
    @Override
    public void delete(Integer commentId) {
        // 逻辑删除
        commentMapper.deleteComment(commentId);
    }

    @Override
    public List<Integer> getLikeIds(Integer authorId, Integer articleId) {
        List<Integer> praiseCommentIds = commentMapper.selectPraiseCommentIds(authorId, articleId);
        return praiseCommentIds;
    }

    @Transactional
    @Override
    public synchronized int switchPraise(Integer authorId, Integer commentId) {
        CommentDTO commentDTO = getById(commentId);
        int articleId = commentDTO.getArticleId();
        List<Integer> commentIds = commentMapper.selectPraiseCommentIds(authorId, articleId);

        int praise = commentMapper.selectPraiseNum(commentId);

        // 如果点赞列表里面包含了该评论，则取消点赞
        // 否则点赞+1
        if (commentIds.contains(commentId)) {
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


    private Article articleExist(Integer articleId) {
        Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery()
                .eq(Article::getArticleId, articleId));

        if (article == null) {
            log.error("【文章服务】查询文章出错，文章不存在，articleId={}", articleId);
            throw new GlobalException(ResponseEnum.ARTICLE_NOT_EXIST);
        }
        return article;
    }

    private QueryWrapper<Comment> buildWrapper() {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("delete_status", false)
                .orderByDesc("create_time");
        return wrapper;
    }

    /**
     * 一级评论回复的转换
     *
     * @param comments
     * @return
     */
    private PageInfo<CommentDTO> dosToDtos(List<Comment> comments) {
        PageInfo<Comment> doPage = new PageInfo<>(comments);

        List<CommentDTO> list = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    // 查出评论的用户名称和头像
                    AuthorDTO authorDTO = authorService.getById(commentDTO.getAuthorId());
                    String authorName = authorDTO.getAuthorName();
                    String authorAvatar = authorDTO.getAuthorAvatar();
                    commentDTO.setAuthorName(authorName);
                    commentDTO.setAuthorAvatar(authorAvatar);

                    //查询自己是否点赞
                    int result = commentMapper.selectLikeStatus(comment.getCommentId(), comment.getAuthorId());
                    commentDTO.setHasLike(result > 0);

                    // 查询一级评论默认带出的两条二级评论
                    QueryWrapper<Comment> wrapper = new QueryWrapper<>();
                    wrapper.lambda()
                            .eq(Comment::getParentCommentId, comment.getCommentId())
                            .orderByDesc(Comment::getCreateTime);
                    // 默认显示出两条
                    PageHelper.startPage(1, 2);
                    List<Comment> commentList = commentMapper.selectList(wrapper);
                    List<CommentDTO> commentDTOS = this.secondDosToDtos(commentList).getList();
                    commentDTO.setChildCommentList(commentDTOS);

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
     *
     * @param comments
     * @return
     */
    private PageInfo<CommentDTO> secondDosToDtos(List<Comment> comments) {
        PageInfo<Comment> doPage = new PageInfo<>(comments);
        List<CommentDTO> list = comments.stream()
                .map(comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    // 如果二级评论中评论被删除了，则显示为匿名评论并且设置内容显示已删除
                    if (comment.getDeleteStatus()) {
                        commentDTO.setAuthorId(-1);
                        commentDTO.setAuthorName(ArticleConstant.DELETE_COMMENT_NAME);
                        commentDTO.setContent(ArticleConstant.DELETE_COMMENT_CONTENT);
                        return commentDTO;
                    }

                    //查询自己是否点赞
                    int result = commentMapper.selectLikeStatus(comment.getCommentId(), comment.getAuthorId());
                    commentDTO.setHasLike(result > 0);

                    // 查询评论者名称和头像
                    AuthorDTO authorDTO = authorService.getById(commentDTO.getAuthorId());
                    String authorName = authorDTO.getAuthorName();
                    String authorAvatar = authorDTO.getAuthorAvatar();
                    commentDTO.setAuthorName(authorName);
                    commentDTO.setAuthorAvatar(authorAvatar);

                    // 查出被回复的用户名称
                    String replyAuthorName = authorService.getById(comment.getReplyCommentAuthorId()).getAuthorName();
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
