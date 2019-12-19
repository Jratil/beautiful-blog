package co.jratil.blogsecurity.service;

import co.jratil.blogapi.entity.dataobject.Author;
import co.jratil.blogapi.service.RoleService;
import co.jratil.blogsecurity.entity.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 17:43
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RoleService roleService;

    /**
     * 根据用户id 来获取查询用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username（account）查询用户信息
        Author author = roleService.getAuthorByAccount(username);
        UserDetailsImpl userDetails = new UserDetailsImpl();
        BeanUtils.copyProperties(author, userDetails);

        // 查询角色信息
        List<GrantedAuthority> authorities = roleService.getRole(userDetails.getAuthorId()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        userDetails.setRoles(authorities);

        return userDetails;
    }
}
