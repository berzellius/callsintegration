package com.callsintegration.repository;

import com.callsintegration.dmodel.Call;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by berz on 27.09.2015.
 */
@Transactional
public interface CallRepository extends CrudRepository<Call, Long>, JpaSpecificationExecutor {
}