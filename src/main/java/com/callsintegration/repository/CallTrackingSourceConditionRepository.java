package com.callsintegration.repository;

import com.callsintegration.dmodel.CallTrackingSourceCondition;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by berz on 01.03.2016.
 */
@Service
@Transactional
public interface CallTrackingSourceConditionRepository extends CrudRepository<CallTrackingSourceCondition, Long>, JpaSpecificationExecutor<CallTrackingSourceCondition> {

}
