# PingPatrol Web Application

## Overview

The PingPatrol Web Application is the frontend component of the PingPatrol uptime monitoring tool. It provides a user-friendly interface for users to monitor the availability of their websites and receive alerts in case of downtime. Built with ReactJS and integrated with Keycloak for authentication, this web app is designed for simplicity, efficiency, and ease of use.

## Features
- **Website Monitoring**: Users can add websites to their monitoring list and view their current status.
- **Alert Notifications**: The app notifies users when a monitored website goes down or becomes unreachable.
- **User Dashboard**: A centralized dashboard for users to manage their monitored websites.
- **Secure Login**: Integrated with Keycloak to provide secure user authentication.

## Technology Stack
- **Frontend Framework**: ReactJS
- **Authentication**: Keycloak
- **Styling**: Material UI

## Project Structure

- `src/components/`: Reusable UI components.
- `src/pages/`: Individual pages like Dashboard, Login, etc.
- `src/services/`: Services for API calls and external interactions.
- `src/assets/`: Static assets such as images and global styles.

## Getting Started

### Prerequisites
- Node.js (latest stable version)
- Keycloak server running (for authentication)

### Installation
1. Install the dependencies:
```bash
npm install
```

### Running the Application
1. Start the development server:
```bash
npm start
```
2. Open http://localhost:3000 (or your configured port) in your browser to view the app.