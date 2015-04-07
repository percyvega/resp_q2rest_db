#!/bin/bash

APP_NAME=resp_q2rest_db
export APP_NAME

destinationUrl=http://dp-devcarrier1:8181/carrierInquiries/
export destinationUrl
jms_qcfName=jms/myConnectionFactory
export jms_qcfName
jms_providerUrl=t3://dp-devcarrier1:8001
export jms_providerUrl
jms_icfName=weblogic.jndi.WLInitialContextFactory
export jms_icfName
jms_sourceQueueName=jms/percyvegaRespQueue
export jms_sourceQueueName
