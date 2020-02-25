package co.jratil.blogarticle.constant;

/**
 * @author jun
 * @version 1.0.0
 * @date 2020-02-10 21:49
 */
public class ArticleConstant {

    /**
     * 评论回复层级：1-直接回复文章的
     */
    public static final Integer LEVEL_1 = 1;
    /**
     * 评论回复层级：2-回复其他评论的
     */
    public static final Integer LEVEL_2 = 2;

    public static final String DELETE_COMMENT_NAME = "匿名";
    public static final String DELETE_COMMENT_CONTENT = "该评论已删除！";
}
