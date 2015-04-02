package com.percyvega.application;

import com.percyvega.jms.JMSReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by pevega on 4/1/2015.
 */
@Component
public class MyCLR implements CommandLineRunner {

//    private static final Logger logger = LoggerFactory.getLogger(MyCLR.class);

    @Autowired
    private JMSReceiver jmsReceiver;

    @Override
    public void run(String... args) throws Exception {
        jmsReceiver.init();
    }
}
