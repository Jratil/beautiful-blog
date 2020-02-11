package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.wrapper.VisibleWrapper;
import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.service.AbstractService;
import co.jratil.blogapi.service.ArticleCategoryService;
import co.jratil.blogarticle.mapper.ArticleCategoryMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = ArticleCategoryService.class)
@Slf4j
@Component
public class ArticleCategoryServiceImpl extends AbstractService<ArticleCategory> implements ArticleCategoryService {

    @Autowired
    private ArticleCategoryMapper categoryMapper;

    @Cacheable(value = "ArticleCategoryService::getById", key = "#categoryId")
    @Override
    public ArticleCategory getById(Integer categoryId) {

        ArticleCategory category = categoryMapper.selectOne(Wrappers.<ArticleCategory>lambdaQuery()
                .eq(ArticleCategory::getCategoryId, categoryId));

        if (category == null) {
            log.error("【类目管理】查询类目不存在，categoryId={}", categoryId);
            throw new GlobalException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        return category;
    }

    @Cacheable(value = "ArticleCategoryService::getByNameAndAuthorId", key = "#categoryName + '_' + #authorId")
    @Override
    public ArticleCategory getByNameAndAuthorId(String categoryName, Integer authorId) {

        ArticleCategory category = categoryMapper.selectOne(Wrappers.<ArticleCategory>lambdaQuery()
                .eq(ArticleCategory::getCategoryName, categoryName)
                .eq(ArticleCategory::getAuthorId, authorId));

        if (category == null) {
            log.error("【类目管理】查询的类目不存在，categoryName={}", categoryName);
            throw new GlobalException(ResponseEnum.CATEGORY_NOT_EXIST);
        }

        return category;
    }

    @Override
    public PageInfo<ArticleCategory> listByAuthorId(PageParam pageParam, Integer authorId, boolean visible) {

        VisibleWrapper<ArticleCategory> wrapper = new VisibleWrapper<>();
        wrapper.lambda()
                .eqVisible(ArticleCategory::getCategoryVisible, visible)
                .eq(ArticleCategory::getAuthorId, authorId)
                .orderByDesc(ArticleCategory::getCreateTime);

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<ArticleCategory> categoryList = categoryMapper.selectList(wrapper);

        return new PageInfo<>(categoryList);
    }

    @Caching(evict = {
//            @CacheEvict(value = "ArticleCategoryService::getById", allEntries = true),
            @CacheEvict(value = "ArticleCategoryService::getByNameAndAuthorId", allEntries = true)
    })
    @Transactional
    @Override
    public void save(ArticleCategory category) {

        // 判断类目是否已经存在
        ArticleCategory result = categoryMapper.selectOne(Wrappers.<ArticleCategory>lambdaQuery()
                .eq(ArticleCategory::getAuthorId, category.getAuthorId())
                .eq(ArticleCategory::getCategoryName, category.getCategoryName()));

        if (result != null) {
            log.error("【类目服务】添加类目出错，类目已存在，category={}", result);
            throw new GlobalException(ResponseEnum.CATEGORY_EXIST);
        }

        // 类目不存在则添加
        categoryMapper.insert(category);
    }

    @CacheEvict(value = "ArticleCategoryService::getById", key = "#category.getCategoryId()")
    @Transactional
    @Override
    public void update(ArticleCategory category) {

        // 判断类目是否存在，存在则更新
        ArticleCategory result = this.getById(category.getCategoryId());
        result.setCategoryName(category.getCategoryName());

        categoryMapper.updateById(result);
    }

    @CacheEvict(value = "ArticleCategoryService::getById", key = "#categoryId")
    @Transactional
    @Override
    public void remove(Integer categoryId) {

        // 判断类目是否存在，存在则删除
        ArticleCategory category = this.getById(categoryId);

        categoryMapper.deleteById(categoryId);
    }
}
