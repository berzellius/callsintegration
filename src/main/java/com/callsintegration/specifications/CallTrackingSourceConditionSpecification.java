package com.callsintegration.specifications;

import com.callsintegration.dmodel.CallTrackingSourceCondition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berz on 06.03.2016.
 */
public class CallTrackingSourceConditionSpecification {

    public static Specification<CallTrackingSourceCondition> selectByUtmParamsAndProjectId(
            final String utmSource, final String utmMedium, final String utmCampaign, final Integer projectId
    ){
        return new Specification<CallTrackingSourceCondition>() {
            @Override
            public Predicate toPredicate(Root<CallTrackingSourceCondition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate pUtm = byUtm(utmSource, utmMedium, utmCampaign).toPredicate(root, criteriaQuery, criteriaBuilder);
                Predicate pProject = byProjectId(projectId).toPredicate(root, criteriaQuery, criteriaBuilder);
                //Predicate pOrder = orderByTruthAndPhonesCount().toPredicate(root, criteriaQuery, criteriaBuilder);

                Path<Integer> truth = root.get("truth");
                Path<Integer> phonesCount = root.get("phonesCount");

                List<Order> orderList = new LinkedList<>();
                orderList.add(criteriaBuilder.desc(truth));
                orderList.add(criteriaBuilder.desc(phonesCount));

                return criteriaQuery.where(criteriaBuilder.and(pUtm, pProject)).orderBy(orderList).getRestriction();
            }
        };
    }

    public static Specification<CallTrackingSourceCondition> byUtm(final String utmSource, final String utmMedium, final String utmCampaign){
        return new Specification<CallTrackingSourceCondition>() {
            @Override
            public Predicate toPredicate(Root<CallTrackingSourceCondition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate pUtmSource = criteriaBuilder.equal(root.get("utmSource"), utmSource);
                Predicate pUtmMedium = criteriaBuilder.equal(root.get("utmMedium"), utmMedium);
                Predicate pUtmCampaign = criteriaBuilder.equal(root.get("utmCampaign"), utmCampaign);

                Predicate pUtmSourceEmpty = criteriaBuilder.equal(root.get("utmSource"), "");
                Predicate pUtmMediumEmpty = criteriaBuilder.equal(root.get("utmMedium"), "");
                Predicate pUtmCampaignEmpty = criteriaBuilder.equal(root.get("utmCampaign"), "");

                return criteriaBuilder.and(
                        criteriaBuilder.or(pUtmSource, pUtmSourceEmpty),
                        criteriaBuilder.or(pUtmMedium, pUtmMediumEmpty),
                        criteriaBuilder.or(pUtmCampaign, pUtmCampaignEmpty)
                );
            }
        };
    }

    public static Specification<CallTrackingSourceCondition> byProjectId(final Integer projectId){
        return new Specification<CallTrackingSourceCondition>() {
            @Override
            public Predicate toPredicate(Root<CallTrackingSourceCondition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("projectId"), projectId);
            }
        };
    }

}
