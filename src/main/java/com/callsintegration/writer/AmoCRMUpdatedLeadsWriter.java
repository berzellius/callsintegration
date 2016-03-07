package com.callsintegration.writer;

import com.callsintegration.dto.api.amocrm.AmoCRMEntities;
import com.callsintegration.dto.api.amocrm.AmoCRMLead;
import com.callsintegration.service.AmoCRMService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berz on 06.03.2016.
 */
@Component
public class AmoCRMUpdatedLeadsWriter implements ItemWriter<List<AmoCRMLead>> {
    @Autowired
    AmoCRMService amoCRMService;

    @Override
    public void write(List<? extends List<AmoCRMLead>> list) throws Exception {
        for(List<AmoCRMLead> list1 : list) {
            ArrayList<AmoCRMLead> amoCRMLeads = new ArrayList<>(list1);
            AmoCRMEntities amoCRMEntities = new AmoCRMEntities();
            amoCRMEntities.setUpdate(amoCRMLeads);

            amoCRMService.editLeads(amoCRMEntities);
        }
    }
}
