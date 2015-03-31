package com.percyvega.rest;

import com.percyvega.model.IntergateTransaction;
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
public class TransactionPoster {

    private static final Logger logger = LoggerFactory.getLogger(TransactionPoster.class);

    public static final int SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL = 15000;

    private RestTemplate restTemplate = new RestTemplate();

    private static String destinationUrl;

    @Value("${destinationUrl}")
    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public void processTransaction(IntergateTransaction intergateTransaction) {
        int destinationUrlUnavailableCount = 0;
        do {
            try {
                restTemplate.put(destinationUrl + "{id}", intergateTransaction, intergateTransaction.getObjid());
                break;
            } catch (ResourceAccessException e) {
                logger.debug("Destination URL unavailable #" + ++destinationUrlUnavailableCount + ". About to sleep(" + SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL + ").");
                Sleeper.sleep(SLEEP_WHEN_UNAVAILABLE_DESTINATION_URL);
            }
        } while (true);
    }

}
