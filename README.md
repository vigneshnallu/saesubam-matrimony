# 💍 SaeSubam Matrimony — Sacred Matchmaking Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![Database](https://img.shields.io/badge/Database-H2%20%2F%20MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.h2database.com/)

**SaeSubam Matrimony** is a full-featured, secure, and modern web application built with **Spring Boot 3**, **Thymeleaf**, and **Bootstrap 5**. Designed specifically for Tamil and multi-community sacred matchmaking, it combines traditional matrimony values with modern privacy controls, horoscope matching, live chat, and tiered subscription plans.

---

## ✨ Key Features

### 🛡️ Candidate Profile & Verification
- **Verified Profiles**: Identity & phone verification badge system (`Verified` vs `Pending`).
- **Rich Profiles**: Photo gallery (primary & secondary photos), education, profession, family background, and location.
- **Horoscope & Jathagam**: PDF Jathagam upload, Star (Natchathiram), Rasi, and astrological compatibility matching.

### 🔒 Privacy & Access Control
- **Strict Profile Locks**: Free users can browse public lists but must upgrade to paid membership to view full contact numbers, email addresses, and detailed Jathagam.
- **Photo Privacy**: Users can choose who views their secondary self photos and contact info.

### 💳 Tiered Subscriptions & View Quotas
- **FREE**: Profile creation, basic search, and interest receiving.
- **GOLD (₹999 / 3 Months)**: 100 candidate profile views, contact details unlock, Jathagam view.
- **PREMIUM (₹1,999 / 6 Months)**: 250 candidate profile views, express interest proposal sending, live chat, priority support.
- **PLATINUM (₹2,999 / 1 Year)**: 250 candidate profile views, unlimited messaging, top search placement.

### 💬 Express Interest & Live Chat
- **Proposal Sending**: Express interest to potential matches with accept/decline workflows.
- **Connected Matches**: Dedicated tab for mutually accepted matches.
- **Real-Time Live Chat**: Direct messaging between connected matches.

### 🛠️ Admin Dashboard & Control Panel
- User account management, subscription overrides, profile verification approval, and system analytics.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend Framework** | Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security |
| **Frontend / Templates** | Thymeleaf 3.x, HTML5, CSS3, JavaScript (ES6+) |
| **UI Components & Icons** | Bootstrap 5.3, Bootstrap Icons, Google Fonts (*Plus Jakarta Sans*, *Playfair Display*) |
| **Database & Persistence** | H2 Embedded Database / MySQL (Hibernate ORM) |
| **Build System** | Apache Maven |

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java JDK 17** or higher
- **Maven 3.8+** (or included `./mvnw` wrapper)

### Running Locally

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/vigneshnallu/saesubam-matrimony.git
   cd saesubam-matrimony
   ```

2. **Build and Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Access the Website**:
   Open your browser and navigate to:
   ```text
   http://localhost:8080
   ```

---

## 📂 Project Structure

```text
spring-boot-starter-web/
├── src/main/java/com/saesubam/
│   ├── controller/         # PageController, AdminController, UserRestController, ChatRestController
│   ├── model/              # Users, CandidateProfile, UserProfileView, Interest, MembershipType
│   ├── repository/         # UserRepository, CandidateProfileRepository, InterestRepository
│   ├── service/            # UserService, EmailService, OTPVerificationService
│   └── config/             # SecurityConfig, WebConfig
├── src/main/resources/
│   ├── templates/          # index.html, login.html, profiles.html, profile-detail.html, interests.html
│   ├── static/             # CSS, JS, Images, Fonts
│   └── application.properties # App & Database Configurations
└── pom.xml                 # Maven Dependencies
```

---

## 📄 License

Copyright © 2026 **SaeSubam Matrimony**. All rights reserved.
