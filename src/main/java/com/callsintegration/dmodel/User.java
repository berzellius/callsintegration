package com.callsintegration.dmodel;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by berz on 26.10.14.
 */
@Entity(name = "User")
@Table(
        name = "users"
        )
@Access(AccessType.FIELD)
public class User extends DModelEntity implements UserDetails {

    public User(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    private String username;

    private String password;

    private Boolean enabled;


    public Long getId(){
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.getId().equals(((User) obj).getId());
    }

    @Override
    public String toString() {
        return this.getUsername();
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "auth_id")
            }
    )
    private Set<UserRole> authorities;


    @Override
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return this.enabled;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


}
