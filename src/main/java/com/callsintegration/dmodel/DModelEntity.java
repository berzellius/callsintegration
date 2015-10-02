package com.callsintegration.dmodel;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by berz on 20.10.14.
 */
@MappedSuperclass
public abstract class DModelEntity implements Serializable {


    public abstract Long getId();

    public abstract void setId(Long id);

    @Override
    public int hashCode(){
        int result = (int) (getId() ^ (getId() >>> 32));

        return result;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract String toString();


}
