package co.jratil.blogapi.entity.dataobject;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorFollow implements Serializable {

    private static final long serialVersionUID = 3845700596434501591L;

    private Integer id;

    /**
     * 作者id
     */
    private Integer authorId;

    /**
     * 被关注人的id
     */
    private String followId;

    /**
     * 被关注人的昵称
     */
    private String followName;

    public AuthorFollow() {
    }

    public AuthorFollow(Integer id, Integer authorId, String followId, String followName) {
        this.id = id;
        this.authorId = authorId;
        this.followId = followId;
        this.followName = followName;
    }
}