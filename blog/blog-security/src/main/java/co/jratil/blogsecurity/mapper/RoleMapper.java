package co.jratil.blogsecurity.mapper;


import co.jratil.blogapi.entity.dataobject.Author;
import co.jratil.blogapi.entity.dataobject.AuthorRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<AuthorRole> {

    @Select("select author_id, author_name, author_account, author_password from t_author where author_account = #{account} ")
    Author selectAuthorByAccount(String account);

}