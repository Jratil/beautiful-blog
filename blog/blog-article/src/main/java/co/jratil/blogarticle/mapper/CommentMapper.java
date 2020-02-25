package co.jratil.blogarticle.mapper;

import co.jratil.blogapi.entity.dataobject.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 逻辑删除，将删除状态设置为true
     *
     * @param commentId
     * @return
     */
    @Update("update t_comment set delete_status = true where comment_id = #{commentId}")
    int deleteComment(@Param("commentId") Integer commentId);

    @Select("select praise_num from t_comment where comment_id = #{commentId}")
    int selectPraiseNum(@Param("commentId") Integer commentId);

    @Update("update t_comment set praise_num = #{praiseNum} where comment_id = #{commentId}")
    int updatePraiseNum(@Param("commentId") Integer commentId, @Param("praiseNum") int praiseNum);

    @Select("select comment_id from t_praise_comment where author_id = #{authorId} and article_id = #{articleId}")
    List<Integer> selectPraiseCommentIds(@Param("authorId") Integer authorId, @Param("articleId") Integer articleId);

    @Select("select count(comment_id) from t_praise_comment where comment_id = #{commentId} and author_id = #{authorId}")
    int selectLikeStatus(@Param("commentId") Integer commentId, @Param("authorId") Integer authorId);

    @Insert("insert into t_praise_comment (author_id, article_id, comment_id) values (#{authorId}, #{articleId}, #{commentId});")
    int addPraise(@Param("authorId") Integer authorId, @Param("articleId") Integer articleId, @Param("commentId") Integer commentId);

    @Delete("delete from t_praise_comment where author_id = #{authorId} and comment_id = #{commentId}")
    int deletePraise(@Param("authorId") Integer authorId, @Param("commentId") Integer commentId);
}