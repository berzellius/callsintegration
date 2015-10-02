package com.callsintegration.specifications;

import com.callsintegration.dmodel.Call;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by berz on 27.09.2015.
 */
public class CallSpecifications {

    public static Specification<Call> byDate(final Date date){
        return new Specification<Call>() {
            @Override
            public Predicate toPredicate(Root<Call> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Calendar dtFromCal = Calendar.getInstance();
                dtFromCal.setTime(date);
                dtFromCal.set(Calendar.HOUR, 0);
                dtFromCal.set(Calendar.MINUTE, 0);
                dtFromCal.set(Calendar.SECOND, 0);
                dtFromCal.set(Calendar.MILLISECOND, 0);

                Calendar dtToCal = Calendar.getInstance();
                dtToCal.setTime(date);
                dtToCal.set(Calendar.HOUR, 23);
                dtToCal.set(Calendar.MINUTE, 59);
                dtFromCal.set(Calendar.SECOND, 59);
                dtFromCal.set(Calendar.MILLISECOND, 999);

                Predicate dtFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("dt"), dtFromCal.getTime());
                Predicate dtToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("dt"), dtToCal.getTime());

                return criteriaBuilder.and(dtFromPredicate, dtToPredicate);
            }
        };
    }

}
