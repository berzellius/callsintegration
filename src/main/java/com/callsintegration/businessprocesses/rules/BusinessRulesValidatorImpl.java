package com.callsintegration.businessprocesses.rules;

import com.callsintegration.businessprocesses.rules.exceptions.ValidationException;
import com.callsintegration.dmodel.Call;
import com.callsintegration.dmodel.LeadFromSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public class BusinessRulesValidatorImpl implements BusinessRulesValidator {

    @Autowired
    private IncomingCallValidationUtil incomingCallValidationUtil;

    @Autowired
    private LeadFromSiteValidationUtil leadFromSiteValidationUtil;

    @Override
    public ValidationUtil getValidationUtil(Object object) throws ValidationException {

        Assert.notNull(object);

        if(object instanceof Call){
            return this.getIncomingCallValidationUtil();
        }

        if(object instanceof LeadFromSite){
            return this.getLeadFromSiteValidationUtil();
        }

        throw new ValidationException("cannot get implementation of the ValidationUtil for object of class " + object.getClass().getName());
    }

    public IncomingCallValidationUtil getIncomingCallValidationUtil() {
        return incomingCallValidationUtil;
    }

    @Override
    public void setIncomingCallValidationUtil(IncomingCallValidationUtil incomingCallValidationUtil) {
        this.incomingCallValidationUtil = incomingCallValidationUtil;
    }

    public LeadFromSiteValidationUtil getLeadFromSiteValidationUtil() {
        return leadFromSiteValidationUtil;
    }

    @Override
    public void setLeadFromSiteValidationUtil(LeadFromSiteValidationUtil leadFromSiteValidationUtil) {
        this.leadFromSiteValidationUtil = leadFromSiteValidationUtil;
    }

    @Override
    public boolean validate(Object object) throws ValidationException {
        return this.getValidationUtil(object).validate(object);
    }
}
