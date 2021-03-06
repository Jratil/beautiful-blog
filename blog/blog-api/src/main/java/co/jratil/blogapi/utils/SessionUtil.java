package co.jratil.blogapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @date 2019-08-13 23:10
 */
@Slf4j
public class SessionUtil {

    /*public static boolean authorityCheck(String authorId, String userRole) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();

        String sAuthorId = (String) session.getAttribute("authorId");

        // 如果是管理员，并且已经登陆了
        // 或者已经登陆了，并且查询到需要操作的内容的 authorId所有者和登录的 authorId一样
        // 则有权限删除
        if (UserRoleEnum.ROLE_ADMIN.getUserRole().equals(userRole)
                && !StringUtils.isEmpty(sAuthorId)) {
            return true;
        } else if (!StringUtils.isEmpty(sAuthorId) && sAuthorId.equals(authorId)) {
            return true;
        }
        return false;
    }*/

    public static HttpSession getHttpSession() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        return request.getSession();
    }

    public static Integer getAuthorId() {
        HttpSession session = getHttpSession();
        return (Integer) session.getAttribute("authorId");
    }
}
