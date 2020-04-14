# happiness-index-serverless

## Prerequisites
1. [Serverless Framework](https://serverless.com) installed
2. Java 11 or later

## build
`./gradlew clean build`

## deploy
`./gradlew clean deploy`

## logs

A good way to keep an eye on the app logs is using [awslogs](https://github.com/jorgebastida/awslogs)

**Installation**

`pip install awslogs`

**Tail the logs**

`awslogs get /aws/lambda/happiness-index-dev-other  ALL --watch`
