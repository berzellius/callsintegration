package com.callsintegration.dmodel;

import com.callsintegration.attrconverter.DalabmxSitesLeadsConverter;
import com.callsintegration.dto.site.Lead;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 15.06.2016.
 */
@Entity(name = "LeadFromSite")
@Table(
        name = "leads_from_site"
)
@Access(AccessType.FIELD)
public class LeadFromSite extends DModelEntityFiscalable {

    public LeadFromSite() {
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static enum State{
        NEW, DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "leads_from_site_id_generator")
    @SequenceGenerator(name = "leads_from_site_id_generator", sequenceName = "leads_from_site_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    @Column(columnDefinition = "character varying(3000)")
    @Convert(converter = DalabmxSitesLeadsConverter.class)
    private Lead lead;

    @JoinColumn(name = "site_id")
    @OneToOne
    private Site site;

    @Enumerated(EnumType.STRING)
    private State state;

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
        return obj instanceof LeadFromSite && this.getId().equals(((LeadFromSite) obj).getId());
    }

    @Override
    public String toString() {
        return "leadFromSite#".concat(this.getId().toString());
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
