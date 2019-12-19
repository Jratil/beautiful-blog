package co.jratil.blogapi.service;


import co.jratil.blogapi.entity.dataobject.Author;
import co.jratil.blogapi.entity.dataobject.AuthorRole;

import java.util.List;

public interface RoleService {

    List<String> getRole(Integer authorId);

    Author getAuthorByAccount(String account);

    void deleteRole(AuthorRole authorRole);

    void updateRole(AuthorRole authorRole);

    void addRole(AuthorRole authorRole);
}
