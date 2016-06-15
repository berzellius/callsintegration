package com.callsintegration.dmodel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 15.06.2016.
 */
@Entity(name = "Site")
@Table(
        name = "site"
)
@Access(AccessType.FIELD)
public class Site extends DModelEntity {
    public Site(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "site_id_generator")
    @SequenceGenerator(name = "site_id_generator", sequenceName = "site_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    private String url;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Site && this.getId().equals(((Site) obj).getId());
    }

    @Override
    public String toString() {
        return "site#".concat(this.getId().toString());
    }
}
