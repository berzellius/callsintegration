package com.callsintegration.businessprocesses.rules;

import com.callsintegration.businessprocesses.rules.exceptions.ValidationException;
import com.callsintegration.dmodel.LeadFromSite;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public interface LeadFromSiteValidationUtil extends ValidationUtil {
    public boolean validate(LeadFromSite leadFromSite) throws ValidationException;

    void setSimpleFieldsValidationUtil(SimpleFieldsValidationUtil simpleFieldsValidationUtil);
}
