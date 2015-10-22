package com.callsintegration.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by berz on 27.09.2015.
 */
@Service
public interface CallsService {

    public Long callsAlreadyLoaded();

    Long callsAlreadyLoaded(Integer projectId);

    Long callsAlreadyLoaded(Integer project, Date from, Date to);
}
