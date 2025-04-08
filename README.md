# Birthday-Reminder-Notification-API
This project is a Spring Boot-based Birthday Reminder Notification API that automates the process of sending birthday reminders through Firebase Push Notifications, Email, and SMS.

**Birthday Reminder Notification API** is a backend application built using **Spring Boot** that automates the process of sending birthday wishes and reminders via **Firebase push notifications**, **email**, and **SMS**. The application is designed to help users remember birthdays of friends, family members, colleagues, or clients by providing timely and multi-channel reminders.

The application uses an **Excel file** as the primary data source for storing user and birthday information. The Excel structure is parsed and processed by the API to extract necessary details such as name, birthdate, email ID, phone number, and Firebase token.

---

## 🔑 Key Features

- **Spring Boot Backend**  
  Lightweight, robust, and scalable backend using Spring Boot, following clean architecture and RESTful principles.

- **Excel Data Source**  
  Birthday data is maintained in an Excel file. The application parses this Excel to fetch and validate user and birthday details. This approach makes it lightweight and easy to update manually.

- **Firebase Push Notifications**  
  Integration with Firebase Cloud Messaging (FCM) allows sending personalized birthday wishes directly to user devices as push notifications.

- **Email Notifications**  
  Birthday wishes and reminders are sent via email using Spring Boot’s JavaMailSender. The emails can be personalized and formatted with HTML templates.

- **SMS Notifications**  
  Integration with an SMS service provider (e.g., Twilio or any gateway of choice) to send SMS birthday wishes or reminders, ensuring reach even when internet access is unavailable.

- **Scheduled Task Execution**  
  The API uses Spring’s scheduling capabilities to run periodic checks (e.g., daily at midnight) for upcoming birthdays and triggers notifications accordingly.

- **Modular and Extensible**  
  Easily configurable to support new notification channels or replace the Excel input with database support in the future.

- **Error Handling & Logging**  
  Centralized exception handling and logging help monitor failures in notification delivery or Excel parsing.

---

## 💼 Use Cases

- Corporate environments to send birthday greetings to employees.  
- Customer relationship management for small businesses.  
- Personal use to keep track of loved ones' birthdays.

---

## 🛠️ Technologies Used

- **Spring Boot**
- **Java**
- **Excel**
- **Firebase Cloud Messaging**
- **JavaMailSender (Email API)**
- **SMS Gateway (like Twilio, configurable)**
- **Spring Scheduler**
- **Maven**

---

## 🚀 Getting Started

### ✅ Prerequisites
- Java 17+
- Maven
- MySQL running with the correct schema (e.g., `birthday_notification`)
- Excel file: `birthdays.xlsx` placed in `src/main/resources/`

---

### ▶️ Running the Application

To run the Spring Boot application:
```bash
mvn spring-boot:run
```

```bash
mvn clean install
java -jar target/your-app-name.jar
```
---

### Excel File Instructions
Place birthdays.xlsx inside src/main/resources/. The required columns are:

    -S.No
    -Name
    -Birth Date (MM/dd/yyyy)
    -Email
    -Contact Number
    -Device Token
    -Message Type (Birthday, Anniversary, Festival)
    -Event Name (only for festivals like Diwali, Holi, etc.)

---

## 👨‍💻 Author

**Deendayal Kumawat**  
Java Fullstack Developer

---

