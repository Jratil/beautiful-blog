package co.jratil.blogsecurity.service;

import co.jratil.blogapi.entity.dataobject.Author;
import co.jratil.blogapi.entity.dataobject.AuthorRole;
import co.jratil.blogcommon.exception.GlobalException;
import co.jratil.blogsecurity.mapper.RoleMapper;
import co.jratil.blogcommon.enums.ResponseEnum;
import co.jratil.blogapi.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @date 2019-08-14 9:35
 */
@Component
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Transactional
    @Override
    public List<String> getRole(Integer authorId) {
        List<AuthorRole> authorRole = roleMapper.selectList(new QueryWrapper<AuthorRole>().eq("author_id", authorId));
        if (Collections.isEmpty(authorRole)) {
            log.error("【权限服务】该用户的没有权限");
            throw new GlobalException(ResponseEnum.ROLE_NOT_EXIST);
        }
        List<String> roles = authorRole.stream()
                .map(AuthorRole::getRole)
                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public Author getAuthorByAccount(String account) {
        Author author = roleMapper.selectAuthorByAccount(account);
        if (author == null) {
            log.error("【权限服务】该用户不存在");
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }
        return author;
    }

    @Transactional
    @Override
    public void deleteRole(AuthorRole authorRole) {
        getRole(authorRole.getAuthorId());
        roleMapper.delete(Wrappers.<AuthorRole>lambdaQuery()
                .eq(AuthorRole::getAuthorId, authorRole.getAuthorId())
                .eq(AuthorRole::getRole, authorRole.getRole()));
    }

    @Transactional
    @Override
    public void updateRole(AuthorRole authorRole) {
        getRole(authorRole.getAuthorId());
        roleMapper.updateById(authorRole);
    }

    @Transactional
    @Override
    public void addRole(AuthorRole authorRole) {
        List<String> roles = getRole(authorRole.getAuthorId());
        if (roles.contains(authorRole.getRole())) {
            log.error("【权限服务】该用户已有该权限");
            throw new GlobalException(ResponseEnum.ROLE_EXIST);
        }
        roleMapper.insert(authorRole);
    }
}
