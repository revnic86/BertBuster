# BertBuster
A Spring Boot 4 / Java 25 API for managing a movie rental system.

### Features
* **Admin Controls:** Full CRUD operations for movies. Creating a movie automatically provisions 3 rentable DVD units.
* **Rental Logic:** Registered users can rent a single DVD. The system enforces a "one-at-a-time" policy, requiring returns before a new rental.
* **Authentication:** Role-based access control (Admin vs. Registered User).
* **Live Demo:** Access the app at [www.robscode.com](http://www.robscode.com)
* **Default Admin Credentials:** `admin` / `admin123`

### Technical Highlights
* **Stack:** Java 25, Spring Boot 4, MySQL (Production), H2 (Testing).
* **Security:** Integrated **Spring Security** for authentication and role-based authorization.
* **Persistence:** Leveraged **Spring Data JPA** with custom business rules for inventory management.
* **Quality Assurance:** Implemented **JUnit/MockMvc** integration tests to validate rental state logic and secure endpoints.
