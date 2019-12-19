package co.jratil.blogapi.service;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dto.AuthorDTO;
import co.jratil.blogapi.entity.dto.AuthorForm;
import com.github.pagehelper.PageInfo;

public interface AuthorService {

    AuthorDTO login(String account, String password, String ipAddr);

    void register(AuthorForm authorForm);

    AuthorDTO getById(Integer authorId);

    AuthorDTO getByAccount(String account);

    AuthorDTO getByName(String name);

    PageInfo<AuthorDTO> listByBlueName(PageParam pageParam, String blurName);

    PageInfo<AuthorDTO> list(PageParam pageParam);

    void insertAuthor(AuthorDTO author);

    void updateAuthor(AuthorDTO author);

    void updatePassword(AuthorForm authorForm) ;

    void deleteAuthor(Integer authorId);

}
