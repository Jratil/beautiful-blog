package co.jratil.blogarticle.mapper;


import co.jratil.blogapi.entity.dataobject.Article;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select date_format(create_time, '%Y年%m月') as archive, count(*) as count " +
            "from t_article " +
            "${ew.customSqlSegment} " +
            "group by archive " +
            "order by archive desc")
    List<Map<String, Object>> selectListArchiveMonth(@Param(Constants.WRAPPER) Wrapper wrapper);

//    @Select("select * from t_article " +
//            "where date_format(create_time, '%Y年%m月') = #{month} and " +
//            "author_id = #{authorId} " +
//            "order by create_time desc")
//    List<Article> selectListByArchive(@Param("authorId") Integer authorId, @Param("month") String month);

    @Select("select article_id from t_praise_article where author_id = #{authorId}")
    List<Integer> selectLikeArticleIds(@Param("authorId") Integer authorId);

    @Update("update t_article set article_like = #{articleLike}")
    int updateArticleLikeNum(@Param("articleLikeNum") Integer articleLikeNum);

    @Select("select article_id from t_praise_article where author_id = #{authorId} and article_id = #{articleId}")
    int selectLikeStatus(@Param("articleId") Integer articleId, @Param("authorId") Integer authorId);

    @Insert("insert into t_praise_article (article_id, author_id) values (#{articleId}, #{authorId})")
    int saveLikeArticleId(@Param("articleId") Integer articleId, @Param("authorId") Integer authorId);

    @Delete("delete from t_praise_article where article_id = #{articleId} and author_id = #{authorId}")
    int deleteLikeArticleId(@Param("articleId") Integer articleId, @Param("authorId") Integer authorId);
}