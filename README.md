# happiness-index-serverless

## Prerequisites
1. [Serverless Framework](https://serverless.com) installed
2. Java 11 or later

Before running the tests or deploying the on the command line, please remember to set up the `xpeppers-develop` role with

```bash
setup_aws_credentials xpeppers-develop
```

## test
`./gradlew test`

## deploy
`./gradlew clean deploy`

## logs

Serverless provides an easy way to read the logs. 

Given our function is called `other`:
```
sls logs -f other --tail
```

A good alternative to keep an eye on the app logs is using [awslogs](https://github.com/jorgebastida/awslogs)

**Installation**

`pip install awslogs`

**Tail the logs**

`awslogs get /aws/lambda/happiness-index-dev-other  ALL --watch`
