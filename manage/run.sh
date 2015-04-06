#!/bin/bash

. ./setName.sh
if [ -z "$APP_NAME" ]
then
    echo "$APP_NAME cannot be empty."
    exit
fi

cd ..

nohup /opt/bea/bea1033/jdk1.6.0_20/bin/java \
-D$APP_NAME \
-jar target/$APP_NAME-1.0-SNAPSHOT.jar $1 $2 $3 $4 $5 $6 $7 $8 $9 &
