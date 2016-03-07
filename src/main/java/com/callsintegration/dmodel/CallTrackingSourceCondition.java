package com.callsintegration.dmodel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 23.02.2016.
 */
@Entity(name = "CallTrackingSourceCondition")
@Table(
        name = "calltracking_source_conditions"
)
@Access(AccessType.FIELD)
public class CallTrackingSourceCondition extends DModelEntityFiscalable {

    public CallTrackingSourceCondition(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "calltracking_source_conditions_id_generator")
    @SequenceGenerator(name = "calltracking_source_conditions_id_generator", sequenceName = "calltracking_source_conditions_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "utm_source")
    private String utmSource;

    @Column(name = "utm_medium")
    private String utmMedium;

    @Column(name = "utm_campaign")
    private String utmCampaign;

    @Column(name = "project_id")
    private Integer projectId;

    private Integer truth;

    @Column(name = "phones_count")
    private Integer phonesCount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = 31*hash + (this.getSourceName() != null? 0 : this.getSourceName().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CallTrackingSourceCondition && this.getId().equals(((CallTrackingSourceCondition) obj).getId());
    }

    @Override
    public String toString() {
        return "CallTrackingSourceCondition#".concat(this.getId().toString());
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTruth() {
        return truth;
    }

    public void setTruth(Integer truth) {
        this.truth = truth;
    }

    public Integer getPhonesCount() {
        return phonesCount;
    }

    public void setPhonesCount(Integer phonesCount) {
        this.phonesCount = phonesCount;
    }
}
