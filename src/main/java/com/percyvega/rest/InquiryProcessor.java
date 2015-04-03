package com.percyvega.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.percyvega.model.CarrierInquiry;
import com.percyvega.util.JacksonUtil;
import com.percyvega.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by pevega on 3/30/2015.
 */
@Component
public class InquiryProcessor {

    private static final Logger logger = LoggerFactory.getLogger(InquiryProcessor.class);

    public static final int SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL = 10000;

    private static RestTemplate restTemplate = new RestTemplate();

    @Value("${destinationUrl}")
    private String destinationUrl;

    public void process(CarrierInquiry carrierInquiry) {
        int destinationUrlUnavailableCount = 0;
        do {
            try {
                restTemplate.put(destinationUrl + "{id}", carrierInquiry, carrierInquiry.getObjid());
                try {
                    logger.debug("JSON message was PUT: " + JacksonUtil.toJson(carrierInquiry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } finally {
                    break;
                }
            } catch (ResourceAccessException e) {
                logger.debug("Destination URL unavailable #" + ++destinationUrlUnavailableCount + ". About to sleep(" + SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL + ").");
                Sleeper.sleep(SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL);
            }
        } while (true);
    }

}
