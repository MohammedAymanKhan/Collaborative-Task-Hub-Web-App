# Collaborative Task Hub Web App

![Overview of Objects Which Handle Request](https://github.com/user-attachments/assets/4e771fba-c115-48eb-ad23-f5b8cff50fd8)

## Overview

The Collaborative Task Hub Web App enables users to manage projects and tasks, chat in real-time, and track task progress. Designed for efficient team collaboration, this app integrates real-time communication with comprehensive project management features.

## Features

- **Project Management**: Create projects, add tasks, set due dates, and track progress.
- **Real-Time Updates**: Utilize Spring WebSocket API for live updates on tasks and project details.
- **Chat Functionality**: Engage in real-time discussions about projects with team members.

## How It Works

### WebSocket Connection

- **`WebSocketConnection` Class**: Manages WebSocket connections by extending `AbstractWebSocketHandler`. It handles new connections and subscriptions.
- **`WebSocketMessageHandler` Class**: Handles messages for different projects. Each project has its own `WebSocketMessageHandler` instance, which stores WebSocket sessions in a set. It processes and forwards CRUD operations based on incoming messages (e.g., task updates, chat messages).

### Message Handling

- Messages are categorized into chat messages or project-related CRUD operations.
- Project related CRUD operations are managed by the `ProjectReportController` class.
- Chat related CRUD operations are handled by the `MessagesController` class.
- Messages are forwarded to all WebSocket sessions currently subscribed to the relevant project.

### HTTP Requests

- **`ProjectController`**: Handles HTTP GET requests to retrieve information about current projects and users working on them.
- **`SearchUsersController`**: Provides REST endpoints to search for users by tech stack, name, email.
- **`UserDetailsController`**: Manages personal details such as notifications and project invitations. Also handles login requests.

### Database Interaction

- **Spring JDBC**: Used for database interactions and CRUD operations.
- **DAO/Repository Classes**: Implemented to manage data access and interactions with the MySQL database.

### Security:

- **Spring Security**: Added for securing the application.
- **Custom Login Page**: Includes dynamic CSRF token validation and user authentication based on email and password.

## Technologies Used

- **Backend**: Spring Framework, Spring WebSocket API, Spring JDBC, Spring Security
- **Frontend**: HTML, CSS, JavaScript
- **Database**: MySQL

