package co.jratil.blogauth.mapper;

import co.jratil.blogapi.entity.dataobject.AuthorLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

public interface AuthorLoginMapper extends BaseMapper<AuthorLogin> {

    /**
     * 插入登录地址，如果存在则进行更新
     * @param record
     * @return
     */
    @Insert(" insert into t_author_login (author_id, login_time, login_ip)\n" +
            "        values (#{authorId,jdbcType=VARCHAR}, current_timestamp, #{loginIp,jdbcType=VARCHAR})\n" +
            "        on duplicate key\n" +
            "            update login_time = current_timestamp,\n" +
            "                   login_ip   = #{loginIp,jdbcType=VARCHAR};")
    int insertOrUpdate(AuthorLogin record);
}