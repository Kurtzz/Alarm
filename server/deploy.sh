#!/usr/bin/env bash

mvn clean package -DskipTests
scp target/push-server-poc-1.0-SNAPSHOT.jar yorg@jdabrowa.pl:/home/yorg/apps
