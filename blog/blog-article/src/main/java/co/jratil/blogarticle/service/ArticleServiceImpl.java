package co.jratil.blogarticle.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dto.AuthorDTO;
import co.jratil.blogapi.service.*;
import co.jratil.blogapi.wrapper.VisibleWrapper;
import co.jratil.blogapi.entity.dataobject.Article;
import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import co.jratil.blogapi.entity.dto.ArticleDTO;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogarticle.mapper.ArticleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = ArticleService.class)
@Slf4j
@Component
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCategoryService categoryService;

    @Reference
    private AuthorService authorService;

    @Cacheable(value = "ArticleService::getById", key = "#articleId")
    @Override
    public ArticleDTO getById(Integer authorId, Integer articleId) {

        Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery()
                .eq(Article::getArticleId, articleId));

        if (article == null) {
            log.error("【文章服务】查询文章出错，文章不存在，articleId={}", articleId);
            throw new GlobalException(ResponseEnum.ARTICLE_NOT_EXIST);
        }

        // 查询文章作者的一些信息
        AuthorDTO authorDTO = authorService.getById(article.getAuthorId());
        String authorName = authorDTO.getAuthorName();
        String categoryName = categoryService.getById(article.getCategoryId()).getCategoryName();

        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        articleDTO.setCategoryName(categoryName);
        articleDTO.setAuthorName(authorName);
        articleDTO.setAuthorAvatar(authorDTO.getAuthorAvatar());

        // 查询自己是否点赞
        int result = 0;
        if (authorId != null) {
            result = articleMapper.selectLikeStatus(articleId, authorId);
        }
        articleDTO.setHasLike(result > 0);

        return articleDTO;
    }

    /**
     * 通过类目id，查询该类目下得文章
     *
     * @param categoryId
     * @return
     */
    @Override
    public PageInfo<ArticleDTO> listByCategoryId(PageParam pageParam, Integer categoryId, boolean visible) {

        // 先查询类目
        ArticleCategory category = categoryService.getById(categoryId);

        VisibleWrapper<Article> wrapper = new VisibleWrapper<>();
        wrapper.select(Article.class, field -> !field.getColumn().equals("article_content"));
        // 分页查询，排除查询文章内容，添加查询条件
        wrapper.lambda()
                .eqVisible(Article::getArticleVisible, visible)
                .eq(Article::getCategoryId, categoryId)
                .orderByDesc(Article::getCreateTime);

        // 分页查询
        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Article> articleList = articleMapper.selectList(wrapper);

        return dosToDtos(articleList);
    }

    /**
     * 根据时间顺序查询所有的文章
     * 不返回文章的内容，只返回所有文章的标题所属类目等
     *
     * @param authorId 用户id
     * @return 查询到的文章
     */
    @Override
    public PageInfo<ArticleDTO> listByAuthorId(PageParam pageParam, Integer authorId, boolean visible) {

        VisibleWrapper<Article> wrapper = new VisibleWrapper<>();

        wrapper.select(Article.class, field -> !field.getColumn().equals("article_content"));
        wrapper.lambda()
                .eqVisible(Article::getArticleVisible, visible)
                .eq(Article::getAuthorId, authorId)
                .orderByDesc(Article::getCreateTime);

        // 分页查询
        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Article> articleList = articleMapper.selectList(wrapper);

        return dosToDtos(articleList);
    }

    @Override
    public List<Map<String, Object>> listArchiveMonth(Integer authorId, boolean visible) {

        VisibleWrapper<Article> wrapper = new VisibleWrapper<>();
        wrapper.lambda()
                .eqVisible(Article::getArticleVisible, visible)
                .eq(Article::getAuthorId, authorId);

        List<Map<String, Object>> archiveList = articleMapper.selectListArchiveMonth(wrapper);

        return archiveList;
    }

    /**
     * 根据用户id和用户归档月份,查出该归档的文章
     *
     * @param month    归档的月份
     * @param authorId 需要查询的用户id
     * @return 分页后的文章列表
     */
    @Override
    public PageInfo<ArticleDTO> listByArchive(PageParam pageParam, String month, Integer authorId, boolean visible) {

        // 根据归档的月份，用户id，是否可见，倒序查询
        VisibleWrapper<Article> wrapper = new VisibleWrapper<>();
        wrapper.select(Article.class, field -> !field.getColumn().equals("article_content"));
        wrapper.eq("date_format(create_time, '%Y年%m月')", month)
                .lambda()
                .eqVisible(Article::getArticleVisible, visible)
                .eq(Article::getAuthorId, authorId)
                .orderByDesc(Article::getCreateTime);

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Article> articleList = articleMapper.selectList(wrapper);

        return dosToDtos(articleList);
    }

    /**
     * 根据时间顺序查询所有的文章
     * 不返回文章的内容，只返回所有文章的标题所属类目等
     *
     * @return 查询到的文章
     */
    @Override
    public PageInfo<ArticleDTO> list(PageParam pageParam) {

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.select(Article.class, field -> !field.getColumn().equals("article_content"));
        wrapper.lambda().orderByDesc(Article::getCreateTime);
        // TODO: 不知道干嘛的。先写着
        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Article> articles = articleMapper.selectList(wrapper);

        return dosToDtos(articles);
    }

    @Transactional
    @Override
    public void save(ArticleDTO articleDTO) {

        // service 判断该用户或者类目是否存在
        categoryService.getById(articleDTO.getCategoryId());
        authorService.getById(articleDTO.getAuthorId());

        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setCreateTime(new Date());

        int result = articleMapper.insert(article);
        if (result < 1) {
            log.error("【文章服务】添加文章出错");
            throw new GlobalException(ResponseEnum.ADD_ARTICLE_ERROR);
        }
    }

    @CacheEvict(value = "ArticleService::getById", key = "#articleId")
    @Transactional
    @Override
    public void remove(Integer articleId) {

        // 判断该文章是否存在
        this.articleExist(articleId);

        // 存在则删除
        articleMapper.deleteById(articleId);
    }

    @CacheEvict(value = "ArticleService::getById", key = "#articleDTO.getArticleId()")
    @Transactional
    @Override
    public void update(ArticleDTO articleDTO) {

        Article article = new Article();
        article.setArticleId(articleDTO.getArticleId());
        article.setArticleTitle(articleDTO.getArticleTitle());
        article.setArticleSubtitle(articleDTO.getArticleSubtitle());
        article.setArticleContent(articleDTO.getArticleContent());
        article.setArticleVisible(articleDTO.getArticleVisible());
        article.setCategoryId(articleDTO.getCategoryId());
        article.setLastUpdate(new Date());

        articleMapper.updateById(article);
    }

    @Override
    public Boolean getLikeStatus(Integer authorId, Integer articleId) {
        int result = articleMapper.selectLikeStatus(articleId, authorId);
        return result > 0;
    }

    /**
     * 增加文章喜欢数
     *
     * @param articleId 文章id
     * @return 点击之后的喜欢的数量
     */
    @Transactional
    @Override
    public synchronized Integer switchLike(Integer authorId, Integer articleId) {

        Article article = this.articleExist(articleId);
        int result = articleMapper.selectLikeStatus(articleId, authorId);
        int praise = article.getArticleLike();

        if (result > 0) {
            praise = praise - 1;
            articleMapper.deleteLikeArticleId(articleId, authorId);
            articleMapper.updateArticleLikeNum(praise, articleId);
        } else {
            praise = praise + 1;
            articleMapper.saveLikeArticleId(articleId, authorId);
            articleMapper.updateArticleLikeNum(praise, articleId);
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

    private PageInfo<ArticleDTO> dosToDtos(List<Article> articleList) {
        // 先将 list 放到 pageInfo 中，以获取分页的参数以及 total
        PageInfo<Article> doPage = new PageInfo<>(articleList);

        // 用户名称只查询一次就好
        final String[] authorName = {null};

        // 将 list<do> 转换为 list<dto>
        List<ArticleDTO> list = doPage.getList().stream()
                .map(item -> {
                    ArticleDTO dto = new ArticleDTO();
                    BeanUtils.copyProperties(item, dto);

                    String categoryName = categoryService.getById(item.getCategoryId()).getCategoryName();
                    if (StringUtils.isEmpty(authorName[0])) {
                        authorName[0] = authorService.getById(item.getAuthorId()).getAuthorName();
                    }
                    int result = articleMapper.selectLikeStatus(item.getArticleId(), item.getAuthorId());
                    dto.setHasLike(result > 0);
                    dto.setCategoryName(categoryName);
                    dto.setAuthorName(authorName[0]);
                    return dto;
                })
                .collect(Collectors.toList());

        // 设置 dtoPage 的参数
        PageInfo<ArticleDTO> dtoPage = new PageInfo<>();
        BeanUtils.copyProperties(doPage, dtoPage);
        dtoPage.setList(list);
        dtoPage.setTotal(doPage.getTotal());

        return dtoPage;
    }
}
