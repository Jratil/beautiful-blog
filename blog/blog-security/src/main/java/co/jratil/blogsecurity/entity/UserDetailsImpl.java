package co.jratil.blogsecurity.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 18:09
 */
public class UserDetailsImpl implements UserDetails {

    private Integer authorId;
    private String authorName;
    private String authorAccount;
    private String authorPassword;
    private Collection<GrantedAuthority> roles;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(Integer authorId, String authorName, String authorAccount, String authorPassword, Collection<GrantedAuthority> roles) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAccount = authorAccount;
        this.authorPassword = authorPassword;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", authorAccount='" + authorAccount + '\'' +
                ", password='" + authorPassword + '\'' +
                ", roles=" + roles +
                '}';
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorAccount(String authorAccount) {
        this.authorAccount = authorAccount;
    }

    public void setAuthorPassword(String authorPassword) {
        this.authorPassword = authorPassword;
    }

    public void setRoles(Collection<GrantedAuthority> roles) {
        this.roles = roles;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorAccount() {
        return authorAccount;
    }

    public Collection<GrantedAuthority> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return authorPassword;
    }

    @Override
    public String getUsername() {
        return authorName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
