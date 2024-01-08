# Project Plan Document: PingPatrol Uptime Monitoring Service

## Project Description:

PingPatrol is a web-based uptime monitoring service, designed to offer basic functionalities similar to UptimeRobot. It aims to provide users with the capability to monitor the availability of their websites and receive notifications in case of downtime. The project's goal is to deepen knowledge in Java Spring Boot, ReactJS, and cloud technologies, while creating a functional and user-friendly service.

## Project Objectives:

- Develop a simplified uptime monitoring tool.
- Learn and apply advanced techniques in Java Spring Boot and ReactJS.
- Explore integration with cloud services (AWS, Azure) and identity management using Keycloak.
- Implement the project incrementally, ensuring thorough testing and quality assurance.

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

## Development Phases:

1. Initial Setup and Backend Development
- Project repository setup.
- Spring Boot application with core uptime monitoring functionality.
- Basic Keycloak integration.

2. Frontend Development and Integration
- ReactJS application setup.
- Development of UI components.
- Integration with backend services.

3. Advanced Features and Security Enhancements
- Implementation of admin functionalities.
- Enhanced security and Keycloak integration.
- Additional monitoring features.

4. Testing and Quality Assurance
- Development of test cases and execution.
- Code quality checks and refinements.

5. Deployment and Beta Testing
- Setup and deployment on AWS/Azure.
- Beta testing and user feedback collection.

6. Iterative Improvements and Scaling
- Refinement based on feedback.
- Exploration of scalability options.

7. Ongoing Development and Expansion
- Continuous updates and feature additions.
- Regular maintenance and technological upgrades.


## Risk Management:
- Regular backups and version control to safeguard against data loss.
- Continuous monitoring of project progress and risk assessment.
- Contingency plans for potential technological or resource-related impediments.