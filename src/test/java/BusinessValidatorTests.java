import com.callsintegration.businessprocesses.rules.*;
import com.callsintegration.dmodel.Call;
import com.callsintegration.dmodel.LeadFromSite;
import com.callsintegration.dto.site.Lead;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Created by berz on 11.01.2017.
 */
public class BusinessValidatorTests {
    private BusinessRulesValidator businessRulesValidator(){
        SimpleFieldsValidationUtil simpleFieldsValidationUtil = new SimpleFieldValidationUtilImpl();

        IncomingCallValidationUtil incomingCallValidationUtil = new IncomingCallValidationUtilImpl();
        incomingCallValidationUtil.setSimpleFieldsValidationUtil(simpleFieldsValidationUtil);
        LeadFromSiteValidationUtil leadFromSiteValidationUtil = new LeadFromSiteValidationUtilImpl();
        leadFromSiteValidationUtil.setSimpleFieldsValidationUtil(simpleFieldsValidationUtil);

        BusinessRulesValidator businessRulesValidator = new BusinessRulesValidatorImpl();
        businessRulesValidator.setIncomingCallValidationUtil(incomingCallValidationUtil);
        businessRulesValidator.setLeadFromSiteValidationUtil(leadFromSiteValidationUtil);

        return businessRulesValidator;
    }

    private BusinessRulesValidator businessRulesValidator;

    @Before
    public void setup(){
        this.setBusinessRulesValidator(businessRulesValidator());
    }

    @Test
    public void testOnJustPrettyCall(){
        Call call = new Call();
        call.setNumber("4998801160");

        Assert.isTrue(this.getBusinessRulesValidator().validate(call));
    }

    @Test
    public void testVeryShortNumberCall(){
        Call call = new Call();
        call.setNumber("12");

        Assert.isTrue(!this.getBusinessRulesValidator().validate(call));
    }

    @Test
    public void testNumberWithNotOnlyDigitsCall(){
        Call call = new Call();
        call.setNumber("asd1234");

        Assert.isTrue(!this.getBusinessRulesValidator().validate(call));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithoutNumber(){
        this.getBusinessRulesValidator().validate(new Call());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLeadFromSiteWithoutCallNumberAndEmail(){
        this.getBusinessRulesValidator().validate(new LeadFromSite());
    }

    @Test
    public void testLeadWithNotNullButEmptyCallNumber(){
        LeadFromSite leadFromSite = new LeadFromSite();
        Lead lead = new Lead();
        lead.setPhone("");
        leadFromSite.setLead(lead);

        Assert.isTrue(!this.getBusinessRulesValidator().validate(leadFromSite));
    }

    public BusinessRulesValidator getBusinessRulesValidator() {
        return businessRulesValidator;
    }

    public void setBusinessRulesValidator(BusinessRulesValidator businessRulesValidator) {
        this.businessRulesValidator = businessRulesValidator;
    }
}
