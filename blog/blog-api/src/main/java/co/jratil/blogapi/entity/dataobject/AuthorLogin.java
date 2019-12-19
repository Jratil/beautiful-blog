package co.jratil.blogapi.entity.dataobject;

import java.io.Serializable;
import java.util.Date;

public class AuthorLogin implements Serializable {

    private static final long serialVersionUID = 2407206228169953267L;

    /**
     * 用户的id
     */
    private Integer authorId;

    /**
     * 最后登录的时间
     */
    private Date loginTime;

    /**
     * 最后登录的ip
     */
    private String loginIp;

    public AuthorLogin(Integer authorId, Date loginTime, String loginIp) {
        this.authorId = authorId;
        this.loginTime = loginTime;
        this.loginIp = loginIp;
    }


    public AuthorLogin() {
        super();
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public String toString() {
        return "AuthorLogin{" +
                ", authorId=" + authorId +
                ", loginTime=" + loginTime +
                ", loginIp='" + loginIp + '\'' +
                '}';
    }
}