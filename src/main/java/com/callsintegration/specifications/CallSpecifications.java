package com.callsintegration.specifications;

import com.callsintegration.dmodel.Call;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by berz on 27.09.2015.
 */
public class CallSpecifications {

    public static Specification<Call> byDates(final Date date1, final Date date2){
        return new Specification<Call>() {
            @Override
            public Predicate toPredicate(Root<Call> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Calendar dtFromCal = Calendar.getInstance();
                dtFromCal.setTime(date1);
                dtFromCal.set(Calendar.HOUR_OF_DAY, 0);
                dtFromCal.set(Calendar.MINUTE, 0);
                dtFromCal.set(Calendar.SECOND, 0);
                dtFromCal.set(Calendar.MILLISECOND, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                System.out.println("bound1: " + sdf.format(dtFromCal.getTime()));

                Calendar dtToCal = Calendar.getInstance();
                dtToCal.setTime(date2);
                dtToCal.set(Calendar.HOUR_OF_DAY, 23);
                dtToCal.set(Calendar.MINUTE, 59);
                dtToCal.set(Calendar.SECOND, 59);
                dtToCal.set(Calendar.MILLISECOND, 999);
                System.out.println("bound2: " + sdf.format(dtToCal.getTime()));

                Predicate dtFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("dt"), dtFromCal.getTime());
                Predicate dtToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("dt"), dtToCal.getTime());

                return criteriaBuilder.and(dtFromPredicate, dtToPredicate);
            }
        };
    }

    public static Specification<Call> byProjectId(final Integer projectId) {
        return new Specification<Call>() {
            @Override
            public Predicate toPredicate(Root<Call> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate projectIdPredicate = criteriaBuilder.equal(root.get("projectId"), projectId);

                return projectIdPredicate;
            }
        };
    }
}
