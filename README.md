# ChatBotIntegration

This project integrates a chatbot system using OpenAI APIs into a Play Framework-based Java application. It uses PostgreSQL as the database and integrates several libraries such as JPA, Hibernate, Lombok, and HikariCP for connection pooling.

## Prerequisites

Ensure you have the following installed:

- **Java 17 or higher** (Required to run Play Framework applications)
- **SBT (Scala Build Tool)** (Used to manage dependencies and run the project)
- **PostgreSQL** (Database used in the application)
- **Git** (For version control)
- **OpenAI API Key** (To integrate the chatbot functionality)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/HabtamuTesfie/ChatBotIntegration.git  
cd ChatBotIntegration
```

### Update the `application.conf` File

Update the `application.conf` file with your PostgreSQL credentials if they differ from the default settings:

### properties
```hocon
db {
  default {
    driver = org.postgresql.Driver
    url = "jdbc:postgresql://localhost:5870/edutrieum"
    username = "postgres"
    password = "atlas@1234"
  }
}
```

### 3. Configure the OpenAI API

In the `application.conf` file, replace the `openai.api.key` with your actual OpenAI API key:

```properties
openai.api.key = "YOUR_OPENAI_API_KEY"
```

### 4. Build the Project

Use SBT to compile and resolve dependencies:

```bash
sbt clean compile
```

### 5. Run the Application

Start the Play application using SBT:

```bash
sbt run
```

The application should now be running on [http://localhost:9000](http://localhost:9000).




