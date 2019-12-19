package co.jratil.blogapi.entity.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@TableName("t_author_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRole implements Serializable {

    private static final long serialVersionUID = -9083832366105991189L;

    private Integer authorId;

    private String role;

    private Date lastUpdate;
}