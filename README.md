# ping-patrol

## Description

## Local Setup

### Build Project

- Publish api specification in maven local repository

```bash
cd ping-patrol-service-api
./gradlew publishToMavenLocal
```

- Build Service

```bash
cd ping-patrol-service
./gradlew clean build 
```

### Start External Services

For now external services are consisted of:
- Database
- Keycloak

Navigate to `scripts/local-setup` directory and run:

```bash
./local-setup.sh restart ping-patrol-db
./local-setup.sh restart keycloak --rebuild
```

As you can observe we start `keycloak` with a `--rebuild` flag. This is because we have extended the default `jboss/keycloak` image with our own custom provider `ping-patrol-keycloak-custom-provider`.

Keycloak Admin Console:
- link: http://127.0.0.1:9080
- username: admin
- password: admin

Database Credentials:
- database_name: pingpatrol_db
- username: pingpatrol
- port: 3306


### Start Rest API Service

- Start `ping-patrol-service` by running:

```bash
cd ping-patrol-service
./gradlew bootRun
```

Service will realms and authentication details like realms and clients in keycloak service:
- clientId: pingpatrol-webapp
- clientId: pingpatrol-client
- roles: ["admin", "user"]
- users:
  - username: admin@test.com, password: admin
  - username: user@test.com, password: user

Example request for obtaining a token from outside the Docker container:

```bash
curl --location 'http://localhost:9080/realms/pingpatrol/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=pingpatrol-webapp' \
--data-urlencode 'username=admin@test.com' \
--data-urlencode 'password=admin' \
--data-urlencode 'scope=openid'
```







