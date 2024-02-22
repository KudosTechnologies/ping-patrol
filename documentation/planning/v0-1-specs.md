# PingPatrol API Documentation - v0.1

## Introduction

PingPatrol API v0.1 is the inaugural release of Kudos Technologies' solution for monitoring external services and websites. This RESTful web service leverages Spring Boot to offer robust and scalable monitor management capabilities, akin to services like uptimerobot.com.

## Features in v0.1

- **Monitor Management**: Users can create, update, pause, resume, reset, and delete monitors to keep tabs on various services and websites.
- **Monitor Runs**: The API provides the ability to fetch historical runs of each monitor, offering insights into their performance and availability over time.
- **Monitor Events**: Users can retrieve a log of events for each monitor, detailing specific occurrences and statuses encountered during monitoring activities.

## Functionalities

The PingPatrol API v0.1 supports a suite of operations centered around "Monitors," which are the core entities used to represent and manage the monitoring tasks:

- **Get a Monitor by ID**: Retrieve detailed information about a specific monitor.
- **Update a Monitor**: Modify the properties of a monitor, such as its name, URL, type, monitoring interval, and timeout.
- **Delete a Monitor**: Remove a monitor from the system.
- **Patch a Monitor**: Apply partial modifications to a monitor's properties.
- **List All Monitors**: Fetch a comprehensive list of all monitors configured in the system.
- **Create a New Monitor**: Add a new monitor with specified properties like name, type, URL, monitoring interval, and timeout.
- **Monitor Runs**: Retrieve all runs associated with a specific monitor, including their outcomes and performance metrics.
- **Monitor Events**: Access a detailed log of events for each monitor, useful for troubleshooting and analysis.

The API is designed to be accessed primarily through the endpoint base URL `http://localhost:8080/v1`. For a detailed exploration of each available operation, including request parameters and response models, users are encouraged to consult the Swagger UI, which provides an interactive documentation interface.

## Conclusion

PingPatrol API v0.1 lays the groundwork for comprehensive external service and website monitoring. Its development roadmap includes further enhancements and features that will continue to elevate its monitoring capabilities. For any questions or feedback, please contact the Kudos Technologies development team.
