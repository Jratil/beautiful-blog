package co.jratil.blogapi.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import com.github.pagehelper.PageInfo;

public interface ArticleCategoryService {

    ArticleCategory getById(Integer categoryId);

    /**
     * 通过类目名称和用户id查询类目
     *
     * @param categoryName
     * @param authorId
     * @return
     */
    ArticleCategory getByNameAndAuthorId(String categoryName, Integer authorId);

    /**
     * 通过用户id查询该用户的全部类目
     *
     * @param authorId
     * @return
     */
    PageInfo<ArticleCategory> listByAuthorId(PageParam pageParam, Integer authorId, boolean visible);

    void save(ArticleCategory category);

    void update(ArticleCategory category);

    void remove(Integer categoryId);
}
