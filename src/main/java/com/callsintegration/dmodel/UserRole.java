package com.callsintegration.dmodel;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 26.10.14.
 */
@Entity
@Table(name = "user_roles")
@Access(AccessType.FIELD)
public class UserRole extends DModelEntity implements GrantedAuthority{

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public enum Role{
        ADMIN
    }

    public UserRole(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_role_id_generator")
    @SequenceGenerator(name = "user_role_id_generator", sequenceName = "user_role_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;


    @Override
    public String getAuthority() {
        return this.getRole().toString();
    }

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object obj){
        return ((UserRole) obj).getId().equals(this.getId()) &&
                obj instanceof UserRole;
    }

    @Override
    public String toString(){
        return getAuthority();
    }


}
