package com.percyvega.jms;

import com.percyvega.model.CarrierInquiry;
import com.percyvega.rest.InquiryProcessor;
import com.percyvega.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by pevega on 1/21/2015.
 */
@Component
public class JMSReceiver implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(JMSReceiver.class);

    private InitialContext initialContext;
    private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private QueueReceiver queueReceiver;
    private Queue queue;

    private long messageCounter = 0;

    @Value("${jms.icfName}")
    private String icfName;

    @Value("${jms.qcfName}")
    private String qcfName;

    @Value("${jms.sourceQueueName}")
    private String sourceQueueName;

    @Value("${jms.providerUrl}")
    private String providerUrl;

    public void init() {
        try {
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, icfName);
            properties.put(Context.PROVIDER_URL, providerUrl);
            initialContext = new InitialContext(properties);
            queueConnectionFactory = (QueueConnectionFactory) initialContext.lookup(qcfName);
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = (Queue) initialContext.lookup(sourceQueueName);
            queueReceiver = queueSession.createReceiver(queue);
            queueReceiver.setMessageListener(this);
            queueConnection.start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            logger.warn(e.toString());
        }
    }

    @Autowired
    private InquiryProcessor inquiryProcessor;

    @Override
    public void onMessage(Message msg) {
        try {
            String messageText;
            if (msg instanceof TextMessage) {
                messageText = ((TextMessage) msg).getText();
            } else {
                messageText = msg.toString();
            }

            logger.debug("Received JMS message #" + ++messageCounter + ": " + messageText);

            try {
                CarrierInquiry carrierInquiry = JacksonUtil.fromJson(messageText, CarrierInquiry.class);
                inquiryProcessor.process(carrierInquiry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        queueReceiver.close();
        queueSession.close();
        queueConnection.close();
    }

    @PostConstruct
    public void postConstruct() {
        logger.debug(this.toString());
    }

    @Override
    public String toString() {
        return "JMSReceiver [icfName=" + icfName + ", providerUrl=" + providerUrl + ", qcfName=" + qcfName + ", sourceQueueName=" + sourceQueueName + "]";
    }

}
