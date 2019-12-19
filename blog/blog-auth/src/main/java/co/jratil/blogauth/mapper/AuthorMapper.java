package co.jratil.blogauth.mapper;

import co.jratil.blogapi.entity.dataobject.ArticleCategory;
import co.jratil.blogapi.entity.dataobject.Author;
import co.jratil.blogapi.entity.dataobject.AuthorRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AuthorMapper extends BaseMapper<Author> {

    int insertAuthor(Author author);

    int insertRole(AuthorRole authorRole);

    int insertCategory(ArticleCategory category);
}