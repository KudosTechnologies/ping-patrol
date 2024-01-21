# ping-patrol

## Description
PingPatrol is a web-based uptime monitoring service, designed to offer basic functionalities similar to UptimeRobot. It aims to provide users with the capability to monitor the availability of their websites and receive notifications in case of downtime. The project's goal is to deepen knowledge in Java Spring Boot, ReactJS, and cloud technologies, while creating a functional and user-friendly service.

## Functionalities:
1. **Website Monitoring**: Regular checks of user-specified websites to determine availability.
2. **Alerts**: Notification system to inform users of downtime incidents.
3. **User Dashboard**: For adding and managing monitored websites.
4. **User Authentication**: Secure login for users and admins.
5. **Admin Panel**: For administrative functions and user management.
6. **Reporting**: Basic reporting on website uptime and performance.

## Technologies:
1. **Backend**: Java Spring Boot (latest version).
2. **Frontend**: ReactJS with Material UI or Tailwind for styling.
3. **Identity Management**: Keycloak for Single Sign-On (SSO) and user management.
4. **Database**: MariaDB.
5. **Testing**: JUnit, Mockito for backend; React Testing Library for frontend; Plawright for e2e.
6. **CI/CD**: GitHub Actions or Jenkins.
7. **Cloud Services**: AWS/Azure for hosting and related cloud services.

## Architecture:
- **Backend**: Modular monolith using clean architecture principles to ensure maintainability and scalability.
- **Frontend**: Simple, user-friendly UI, utilizing pre-built UI components from Material UI or Tailwind.
- **Integration**: RESTful services for frontend-backend communication, Keycloak integration for authentication and authorization.
- **Testing**: Comprehensive testing strategy including unit, acceptance, and E2E tests.

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







