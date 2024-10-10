# ChatBotIntegration

This project integrates a chatbot system using OpenAI APIs into a Play Framework-based Java application. It uses PostgreSQL as the database and integrates several libraries such as JPA, Hibernate, Lombok, and HikariCP for connection pooling.

## Prerequisites

Ensure you have the following installed:

- **Java 17 or higher** (Required to run Play Framework applications)
- **SBT (Scala Build Tool)** (Used to manage dependencies and run the project)
- **PostgreSQL** (Database used in the application)
- **Git** (For version control)
- **OpenAI API Key** (To integrate the chatbot functionality)

### Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/HabtamuTesfie/ChatBotIntegration.git  
cd ChatBotIntegration
```

### 2. Update the `application.conf` File

Ensure you have created the database `edutrieum` in PostgreSQL. Then, update the `application.conf` file with your PostgreSQL credentials if they differ from the default settings:

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

### 3. Update the `persistence.xml` File

Update the `persistence.xml` file in the project's `src/main/resources/META-INF/` directory with your database credentials.


### 4. Configure the OpenAI API

In the `application.conf` file, replace the `openai.api.key` with your actual OpenAI API key:

```properties
openai.api.key = "YOUR_OPENAI_API_KEY"
```

### 5. Build the Project

Use SBT to compile and resolve dependencies:

```bash
sbt clean compile
```

### 6. Run the Application

Start the Play application using SBT:

```bash
sbt run
```

The application should now be running on [http://localhost:9000](http://localhost:9000).


## Testing the UI

Once the application is running, you can test the UI by following these steps:

### Access the Login Page:

1. Open your web browser and navigate to [http://localhost:9000](http://localhost:9000).
2. You should see a login page with two input fields: one for the **Username** and one for the **Email**.

### Enter Credentials:

- **Username:** You can enter any value in the username field. Note that the username is just a placeholder for now and is not used in the backend. There is also minimal validation on this field.
- **Email:** Enter any valid email address. The email address is essential for session management and acts as the user identifier.

### Login:

1. After entering both the username and email, submit the form to log in.
2. If the email is valid, you will be successfully logged in, and the chatbot interface will be displayed.

### Chatbot Interaction:

- Once logged in, you should see the chatbot app displayed on the screen.
- You can interact with the chatbot and receive responses generated through the integrated OpenAI API.

### Notes:

- The **Username** field is currently only for display and does not affect the functionality of the backend.
- The **Email** is mandatory, as it is used to manage user sessions.
- Ensure you have a valid OpenAI API key configured in the `application.conf` file to receive responses from the chatbot.

## Future Enhancements

- Implement username validation and integration with the backend.
- Add an email verifier to check if the email is valid.
- Enhance the chatbot UI for a better user experience.

