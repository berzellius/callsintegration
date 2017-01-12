package com.callsintegration.businessprocesses.rules;

import com.callsintegration.businessprocesses.rules.exceptions.ValidationException;

/**
 * Created by berz on 11.01.2017.
 */
public interface ValidationUtil {
    public boolean validate(Object object) throws ValidationException;
}
