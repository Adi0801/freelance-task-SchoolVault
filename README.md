# Athena Library Management System

Athena is a mini library management system built with **Spring Boot** (backend), **MySQL** (database), and **Angular + Bootstrap** (frontend). 

---

## Technical Stack
- **Backend:** Java 17, Spring Boot 3.5.14, Spring Data JPA, Hibernate, MySQL, Flyway Migrations, SpringDoc OpenAPI (Swagger)
- **Frontend:** Angular 18+, TypeScript, Bootstrap 5, Bootstrap Icons, Reactive Forms

---

## Folder Structure
```text
freelance-task/
├── library-api/          # Spring Boot Backend
│   ├── src/
│   ├── pom.xml
│   └── ...
├── library-ui/          # Angular Frontend
│   ├── src/
│   ├── package.json
│   └── ...
└── README.md            # Project guide
```

---

## Database Requirements
Please ensure that a local MySQL server is running on port `3306` with the following credentials:
- **Username:** `root`
- **Password:** `Aditya@2001`

The database named `library_db` will be **automatically created** upon starting the backend application if it does not already exist, and Flyway will execute all schema migration scripts.

---

## How to Run

### Step 1: Run the Backend
Navigate to the `library-api` folder and run it using Maven:

```bash
cd library-api
mvn spring-boot:run
```

Once the application starts, you can access:
- **API Base URL:** `http://localhost:8080/api/v1`
- **Swagger Documentation:** `http://localhost:8080/swagger-ui.html`

### Step 2: Run the Frontend
Navigate to the `library-ui` folder, install the dependencies, and start the development server:

```bash
cd library-ui
npm install
npm start
```

The frontend will run on:
- **Local URL:** `http://localhost:4200`

---

## Business Logic & Constraints

### 1. Book Management
- Full CRUD operations with soft delete. Deleted books are hidden from listings but historical transaction logs remain valid and visible.
- ISBN must be unique.
- Input validation: Title, Author, ISBN are required; total copies and available copies must be `>= 0` and `availableCopies <= totalCopies`.

### 2. Member Management
- Register new members, update profiles, and deactivate members.
- Deactivating a member sets status to `INACTIVE`. Inactive members are restricted from borrowing any new books.
- Email must be unique.

### 3. Borrowing Workflow
- **Issue Rules:**
  - Standard loan period is **14 days**.
  - A book can only be issued if its `availableCopies > 0`.
  - A member cannot borrow the same book twice simultaneously without returning it first.
  - Only `ACTIVE` members can borrow books.
  - Issuing a book decrements the book's `availableCopies` by 1.
- **Return Rules:**
  - Returning a book updates the transaction status to `RETURNED`, records the `returnedAt` timestamp, and increments the book's `availableCopies` by 1.
- Full transaction logs are viewable globally, filtered per book, or filtered per member.
