#!/bin/bash

cd ..

nohup /opt/bea/bea1033/jdk1.6.0_20/bin/java \
-Dresp_q2rest_db \
-jar target/resp_q2rest_db-1.0-SNAPSHOT.jar &
