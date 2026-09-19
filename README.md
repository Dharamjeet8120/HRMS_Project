# HRMS – Human Resource Management System

A full-stack Human Resource Management System built with **Spring Boot** (backend) and **React.js** (frontend), designed to manage employee onboarding, attendance, and payroll through a secure, role-based interface.

---

## 🚀 Features

- **Employee Management** – Onboard, update, and retrieve employee records via full CRUD REST APIs
- **Attendance Tracking** – Manage and monitor employee attendance data
- **Payroll Management** – Handle payroll data tied to employee records
- **Authentication & Authorization** – Secured with Spring Security, enforcing role-based access to sensitive endpoints (e.g. payroll, employee data)
- **Interactive Dashboard** – React-based UI for onboarding, attendance, and payroll workflows
- **Centralized Error Handling** – Consistent, reliable API responses across all endpoints via centralized exception handling
- **Input Validation** – Server-side validation to ensure data integrity, mirrored on the client side

---

## 🛠️ Tech Stack

**Backend**
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Hibernate / JPA
- MySQL
- Maven

**Frontend**
- React.js
- Axios (API integration)

**Tools**
- Git & GitHub
- Postman (API testing)

---

## 🏗️ Architecture

The backend follows a clean **3-layer architecture**:

```
Controller  →  Service  →  Repository  →  Database
```

- **Controller Layer** – Exposes REST endpoints, handles HTTP requests/responses
- **Service Layer** – Contains business logic, keeps controllers thin and testable
- **Repository Layer** – Handles data persistence via Spring Data JPA / Hibernate

OOP principles are applied throughout to keep the service and controller layers modular and maintainable.

---

## 📦 Core Entities

| Entity      | Description                                   |
|-------------|------------------------------------------------|
| Employee    | Core employee profile and onboarding details   |
| Attendance  | Daily attendance records linked to employees   |
| Payroll     | Salary and payroll data linked to employees    |

---

## 🔐 Security

Spring Security is integrated to:
- Authenticate users before accessing protected resources
- Enforce **role-based authorization**, restricting sensitive endpoints (like payroll and employee data) based on user roles

---

## 📡 Sample API Endpoints

| Method | Endpoint                  | Description                     |
|--------|----------------------------|----------------------------------|
| POST   | `/api/employees`           | Onboard a new employee          |
| GET    | `/api/employees`           | Retrieve all employees          |
| GET    | `/api/employees/{id}`      | Retrieve a single employee      |
| PUT    | `/api/employees/{id}`      | Update employee details         |
| DELETE | `/api/employees/{id}`      | Remove an employee record       |
| GET    | `/api/attendance/{empId}`  | Get attendance for an employee  |
| GET    | `/api/payroll/{empId}`     | Get payroll details             |

> ℹ️ Update this table to match your actual endpoint paths before publishing.

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+ (or your configured JDK version)
- Maven
- MySQL
- Node.js & npm (for the React frontend)

### Backend Setup
```bash
# Clone the repository
git clone https://github.com/Dharamjeet8120/HRMS_Project.git
cd HRMS_Project

# Configure your database credentials in application.properties
# spring.datasource.url=jdbc:mysql://localhost:3306/hrms_db
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# Run the application
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

The backend runs by default on `http://localhost:8080` and the frontend on `http://localhost:3000` (adjust as per your configuration).

---

## 📸 Screenshots

working...............
---

## 👤 Author

**Dharamjeet Kushwaha**
- GitHub: [@Dharamjeet8120](https://github.com/Dharamjeet8120)
- LinkedIn: [Dharamjeet Kushwaha](https://www.linkedin.com/in/dharamjeetkushwaha-9592081ba)
- Portfolio: [dharamjeet8120.github.io/my_portfolio_website](https://dharamjeet8120.github.io/my_portfolio_website/)

---

## 📄 License

This project is open for educational and portfolio purposes.
