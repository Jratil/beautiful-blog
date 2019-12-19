package co.jratil.blogarticle.mapper;


import co.jratil.blogapi.entity.dataobject.Article;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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


    Integer selectLike(Integer articleId);

    int updateArticleLike(Integer articleLike);
}