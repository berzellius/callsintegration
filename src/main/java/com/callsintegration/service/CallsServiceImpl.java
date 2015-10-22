package com.callsintegration.service;

import com.callsintegration.repository.CallRepository;
import com.callsintegration.specifications.CallSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by berz on 27.09.2015.
 */
@Service
@Transactional
public class CallsServiceImpl implements CallsService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CallRepository callRepository;

    @Override
    public Long callsAlreadyLoaded() {
        return callRepository.count(CallSpecifications.byDates(new Date(), new Date()));
    }

    @Override
    public Long callsAlreadyLoaded(Integer projectId) {
        return callsAlreadyLoaded(projectId, null, null);
    }

    @Override
    public Long callsAlreadyLoaded(Integer project, Date from, Date to) {
        Long count = callRepository.count(
                Specifications.where(CallSpecifications.byDates((from != null)? from : new Date(), (to != null)? to : new Date()))
                        .and(CallSpecifications.byProjectId(project))
        );
        return count;
    }
}
