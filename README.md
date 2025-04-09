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

Place the `birthdays.xlsx` file inside the `src/main/resources/` directory. The required columns in the Excel file are:

- **S.No**: Serial number of the entry.
- **Name**: Name of the individual.
- **Birth Date**: Date of birth in the format MM/dd/yyyy.
- **Email**: Email address of the individual.
- **Contact Number**: Phone number of the individual.
- **Device Token**: Firebase device token for push notifications.
- **Message Type**: Type of message (e.g., Birthday, Anniversary, Festival).
- **Event Name**: Name of the event (only for festivals like Diwali, Holi, etc.).
---
### ⏰ When Does the Batch Job Run?

The batch job reads the Excel file and processes users based on today's date. You can schedule the job using `@Scheduled` (e.g., every morning at 9 AM). The job loads data from Excel at runtime, so any file updates should be saved before the job starts.

---

### 📩 When Are Notifications Sent?

Once the batch job runs, notifications are sent for users with today's event (birthday, anniversary, festival):

- 🎉 A random message is picked based on `messageType` and `eventName`.
- ✉️ Email is sent if an email is present.
- 📱 SMS is sent if a contact number is present.
- 🔔 Push notification is sent if a device token is present.

---

### 📡 Sample API Call (Paginated)

To view all birthdays (with pagination):

```bash
GET /birthdays?page=0&size=10
```
---
### Sample Response
```bash
{
  "content": [
    {
      "name": "John Doe",
      "birthDate": "1990-04-07",
      "email": "john@example.com",
      "contactNumber": "9876543210",
      "messageType": "Birthday"
    }
  ],
  "pageable": { ... },
  "totalElements": 10,
  "totalPages": 1,
  "number": 0,
  "size": 10
}
```


### For Docker


[![View PDF](https://img.icons8.com/ios/50/000000/pdf.png)](Birthday_Notification_App__Dockerized_Setup_Guide.pdf)

### Global Url

```
http://localhost:8080/
```

### Optional
```
http://localhost:8080/api/v1/birthday/list
```

## 👨‍💻 Author

**Deendayal Kumawat**  
Java Fullstack Developer

---

