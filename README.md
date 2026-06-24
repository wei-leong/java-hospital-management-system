# 🏥 Hospital Management System (Java Desktop Application)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

> **Note:** This system was developed as a core Object-Oriented Programming assignment. It demonstrates proficiency in Java GUI development, state management via File I/O, and the implementation of Role-Based Access Control (RBAC).

## 📱 Overview

The Hospital Management System is a comprehensive, role-based desktop application engineered to digitize clinic workflows. It handles the entire patient lifecycle, from appointment scheduling and medicine inventory to financial receipt generation and staff management. 

<div align="center">
  <!-- TODO: Replace with your actual app screenshots -->
  <img src="https://via.placeholder.com/400x300.png?text=Login+&+RBAC" alt="Login Interface" width="400"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://via.placeholder.com/400x300.png?text=Manager+Dashboard" alt="Manager Dashboard" width="400"/>
</div>

---

## 🏗️ Architecture & Design

This application was built utilizing core Object-Oriented principles, ensuring a modular and maintainable codebase.

*   **Role-Based Access Control (RBAC):** The system isolates functionalities by routing users to dedicated modules based on their login credentials (Manager, Doctor, Staff, and Customer).
*   **Object-Oriented Design:** Strong use of encapsulation and inheritance with core entity classes such as `Person`, `Doctor`, and `Staff`.
*   **File-Based Persistence:** Instead of a relational database, the system relies on custom flat-file storage (`FileActions.java`), persisting state across application restarts using structured `.txt` files.

## 🛠️ Tech Stack

*   **Core Language:** Java
*   **GUI Framework:** Java Swing / AWT
*   **Libraries:** JCalendar (Date picking), JGoodies
*   **Data Storage:** File I/O (`appointment.txt`, `medicine.txt`, `receipt.txt`, etc.)

---

## ✨ Key Features

### 1. Manager Dashboard
*   Oversee total clinic operations and track revenue generation (`RevenueActions.java`).
*   Add, edit, and manage staff records (`StaffManagement.java`).
*   Review aggregated patient feedback (`Feedback.java`).

### 2. Staff / Receptionist Portal
*   Handle administrative tasks such as customer record management (`CustomerManagement.java`).
*   Manage appointment bookings and schedules (`AppointmentsManagement.java`).
*   Generate detailed financial reports and patient payment receipts (`GenerateReceipts.java`, `FinanceReport.java`).

### 3. Doctor Workspace
*   Set and manage individual working hours (`DoctorWorkTime.java`).
*   Review incoming patient appointments (`CheckAppointment.java`).
*   Monitor and update the clinic's medicine inventory (`ManageMedicine.java`).

### 4. Patient Interface
*   Check available appointments securely (`CheckAppointments.java`).
*   View personal medical history (`ViewHistory.java`).
*   Submit facility feedback and comments directly to management (`ProvideComments.java`).

---

## 🚀 Installation & Setup

To run this project locally, ensure you have the Java Development Kit (JDK) installed. 

**1. Clone the repository:**
```bash
git clone https://github.com/wei-leong/java-hospital-management-system.git
cd java-hospital-management-system
```

**2. Open in your IDE:**
* This project contains nbproject files, making it seamlessly compatible with Apache NetBeans.
* It can also be imported into IntelliJ IDEA or Eclipse.

**3. Compile and Run:**
* Ensure the Libraries folder (containing jcalendar-1.4.jar) is added to your project's build path.
* Run the main application file located at: `src/apumedicalcentre/ApuMedicalCentre.java`
