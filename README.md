To run AccountService, provide database url, username and password in application.yml or as command line arguments:

`./gradlew bootRun --args='--DATABASE_URL=myurl --DATABASE_USER=user --DATABASE_PASSWORD=password'`

Statistics is available from:

/actuator/metrics/counter.amount.get.total

/actuator/metrics/counter.amount.add.total

/actuator/metrics/counter.amount.get.second

/actuator/metrics/counter.amount.add.second

To reset statistics, send a POST request to /metrics/reset/

To run AccountClient, provide service url, readers count, writers count and a list of ids in application.yml or as command line arguments:

`./gradlew bootRun --args='--serverUrl=http://localhost:8080/accounts/ --rCount=100 --wCount=60 --idList=2,3'`
