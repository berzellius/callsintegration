package com.callsintegration.businessprocesses.rules;

import com.callsintegration.businessprocesses.rules.exceptions.ValidationException;
import com.callsintegration.dmodel.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public class IncomingCallValidationUtilImpl implements IncomingCallValidationUtil {
    @Autowired
    private SimpleFieldsValidationUtil simpleFieldsValidationUtil;

    @Override
    public boolean validate(Call call) throws ValidationException {
        Assert.notNull(call.getNumber());

        return this.getSimpleFieldsValidationUtil().validate(call.getNumber(), SimpleFieldsValidationUtil.ValidationType.CALL_NUMBER);
    }

    @Override
    public boolean validate(Object object) throws ValidationException {
        if(object instanceof Call){
            Call call = (Call) object;
            return this.validate(call);
        }
        else{
            throw new ValidationException("Cannot validate object of class " + object.getClass() + "; expected instance of the dmodel.Call");
        }
    }

    public SimpleFieldsValidationUtil getSimpleFieldsValidationUtil() {
        return simpleFieldsValidationUtil;
    }

    @Override
    public void setSimpleFieldsValidationUtil(SimpleFieldsValidationUtil simpleFieldsValidationUtil) {
        this.simpleFieldsValidationUtil = simpleFieldsValidationUtil;
    }
}
