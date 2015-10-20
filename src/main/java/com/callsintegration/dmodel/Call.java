package com.callsintegration.dmodel;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by berz on 21.09.2015.
 */
@Entity(name = "Call")
@Table(
        name = "calls"
)
@Access(AccessType.FIELD)
public class Call extends DModelEntityFiscalable {

    public Call(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "call_id_generator")
    @SequenceGenerator(name = "call_id_generator", sequenceName = "call_id_seq")
    @NotNull
    @Column(updatable = false, insertable = false, columnDefinition = "bigint")
    private Long id;

    private Integer projectId;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /*
     * Состояние в контексте обработки входящих звонков
     */
    public static enum State {
        NEW,
        DONE
    }

    public static enum Status {

        ANSWERED("ANSWERED"),
        NO_ANSWER("NO ANSWER"),
        BUSY("BUSY"),
        FAILED("FAILED");

        private String str;

        Status(String str) {
            this.str = str;
        }


        public static Status valueByString(String str) {
            for (Status s : Status.values()) {
                if (s.getStr().equals(str)) {
                    return s;
                }
            }
            return null;
        }

        public String getStr() {
            return str;
        }
    }

    public Call(String number, Date dt, String source, Status status, HashMap<String, String> params){
        this.setNumber(number);
        this.setDt(dt);
        this.setSource(source);
        this.setStatus(status);
        this.setParams(params);
    }

    private String number;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    private Date dt;

    private String source;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private State state;

    @ElementCollection
    @CollectionTable(name = "calls_params", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> params = new LinkedHashMap<>();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Call) && (this.getId().equals(((Call) obj).getId()));
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return "num: " + this.getNumber() +
                ", date: " + sdf.format(this.getDt()) +
                ", source: " + this.getSource() +
                ", params: " + this.getParams().toString();
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public State getState(){
        return this.state;
    }

    public void setState(State state){
        this.state = state;
    }
}
