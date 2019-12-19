package co.jratil.blogsecurity.constant;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 17:46
 */
public class JwtConstant {

    /**
     * token 相关内容
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String TOKEN_TYPE = "JWT";

    /**
     * 放在token中的用户角色的key
     */
    public static final String ROLE_CLAIMS = "rol";

    /**
     * 没有rememberMe则过期时间为1天，否则为7天过期
     */

    public static final long EXPIRATION = 60 * 60 * 24;
    public static final long EXPIRATION_REMEMBER = 60 * 60 * 24 * 7;

    public static final String CHARACTER_UTF_8 = "text/json;charset=utf-8";

    public static final String LOGIN_PATH = "/auth/login";

}
