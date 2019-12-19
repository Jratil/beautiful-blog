package co.jratil.blogsecurity.util;

import co.jratil.blogsecurity.entity.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-08 16:12
 */
public class SecurityUtils {

    /**
     * 判断用户是否有权限做此操作
     * 如果数据库查询出来的id不是用户自己的并且不是管理员，则返回false
     * @param authorId 从数据库查询到的所操作的用户id
     * @return
     */
    public static boolean isMeOrAdmin(Integer authorId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return authorId.equals(userDetails.getAuthorId()) || roles.contains("ADMIN");
    }

    public static Integer getAuthorId() {
        Integer authorId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authorId;
    }
}
