package co.jratil.blogapi.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    ArticleDTO getById(Integer articleId);

    PageInfo<ArticleDTO> listByAuthorId(PageParam pageParam, Integer authorId, boolean visible);

    PageInfo<ArticleDTO> listByCategoryId(PageParam pageParam, Integer categoryId, boolean visible);

    List<Map<String, Object>> listArchiveMonth(Integer authorId, boolean visible);

    PageInfo<ArticleDTO> listByArchive(PageParam pageParam, String month, Integer authorId, boolean visible);

    PageInfo<ArticleDTO> list(PageParam pageParam);

    void save(ArticleDTO articleDTO);

    void remove(Integer articleId);

    void update(ArticleDTO articleDTO);

    /**
     * 喜欢量增加
     * @param articleId 文章id
     * @return 返回点赞后的喜欢的数量
     */
    Integer saveOrUpdateLike(Integer articleId);

    /**
     * 取消喜欢
     * @param articleId 文章id
     * @return  返回取消后的喜欢的数量
     */
    Integer removeOrUpdateLike(Integer articleId);
}
