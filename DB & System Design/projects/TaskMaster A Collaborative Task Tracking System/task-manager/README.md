Here’s your README converted into **Markdown (.md)** format as plain text:


# TASK MANAGER APPLICATION

A Spring Boot-based Task Manager application supporting tasks, comments, attachments, users, and teams.

---

## Table of Contents

1. [Features](#features)  
2. [Tech Stack](#tech-stack)  
3. [Getting Started](#getting-started)  
4. [Configuration](#configuration)  
5. [API Endpoints](#api-endpoints)  
   - Tasks  
   - Attachments  
   - Comments  
6. [Testing](#testing)  
7. [License](#license)  

---

## Features

- Create, update, and delete tasks  
- Assign tasks to users  
- Add attachments to tasks  
- Add comments to tasks  
- Manage users and teams  
- JWT-based authentication and authorization  

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Data JPA / Hibernate  
- Spring Security (JWT)  
- MySQL database  
- Lombok for boilerplate code  
- Maven for dependency management  

---

## Getting Started

**Prerequisites:**  
- JDK 17+  
- Maven  
- MySQL database  

**Setup:**  
1. Clone the repository:  
   ```bash
   git clone <repository_url>
   cd task-manager


2. Create a MySQL database:

   ```sql
   CREATE DATABASE task_manager;
   ```
3. Configure the database in `application.yml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/task_manager
       username: root
       password: <your_password>
     jpa:
       hibernate:
         ddl-auto: update
   ```
4. Build and run the application:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## Configuration

* JWT Secret: `jwt.secret` in `application.yml`
* Upload directory: `attachments.upload-dir` (default: `uploads`)
* File upload limit: 5 MB

---

## API Endpoints

### Tasks

* `POST   /api/tasks`                : Create a new task
* `GET    /api/tasks`                : Get all tasks
* `GET    /api/tasks/{id}`           : Get task by ID
* `PUT    /api/tasks/{id}`           : Update task
* `DELETE /api/tasks/{id}`           : Delete task

### Attachments

* `POST   /api/tasks/{taskId}/attachments`               : Upload a file to a task
* `GET    /api/tasks/{taskId}/attachments`               : Get all attachments for a task
* `GET    /api/tasks/attachments/download/{attachmentId}`: Download a specific attachment

### Comments

* `POST   /api/tasks/comments`       : Add a comment to a task
* `GET    /api/tasks/comments/{taskId}` : Get all comments for a task

---
