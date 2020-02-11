package co.jratil.blogapi.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.Comment;
import co.jratil.blogapi.entity.dto.CommentDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommentService {

    CommentDTO getById(Integer commentId);

    PageInfo<CommentDTO> getCommentByArticleId(PageParam pageParam, Integer articleId);

    PageInfo<CommentDTO> getCommentByParentCommentId(PageParam pageParam, Integer parentCommentId);

    void save(Comment comment);

    /**
     * 保留方法
     *
     * @param comment
     */
    void update(Comment comment);

    void delete(Integer commentId);

    List<Integer> getLikeIds(Integer authorId, Integer articleId);

    int switchPraise(Integer authorId, Integer commentId);
}
