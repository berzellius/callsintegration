package com.callsintegration.service;

import com.callsintegration.dmodel.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by berz on 26.10.14.
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Query q = entityManager.createQuery("SELECT o FROM User o WHERE username = :u", User.class)
                .setParameter("u", s);


        if(q.getResultList().isEmpty()) throw new UsernameNotFoundException("no user with name " + s);


        return (UserDetails) q.getSingleResult();
    }



}
