# TableBook implementation mapped to the four syllabus phases

## Phase 1 — Core application setup and design
**Goal:** model the user entity, attributes, constructors, methods and basic string validation.

- `TableBook-backend/src/model/User.java`: User entity with id, name, username, email, password hash, role, phone and active state; constructor, getters, setters, `isValid()` and JSON map representation.
- `TableBook-backend/src/Main.java`: starts the application and supports a small entity demo with `java -cp out Main demo` after compilation.
- `TableBook-backend/src/service/UserService.java`: shared validation and password-hash helper used by the API.

**Presentation:** show the User model and explain why the password hash is stored instead of the raw password.

## Phase 2 — Structured programming and reliability
**Goal:** packages, interface-based operations and exception handling.

- `src/model/`: domain entity.
- `src/interfaces/UserOperations.java`: declares user validation/authentication operations.
- `src/service/UserService.java`: implements the interface, validates input and hashes passwords.
- `src/exception/InvalidUserException.java` and `ApiException.java`: meaningful validation/API exceptions with HTTP status codes.
- `src/util/Json.java`: dependency-free JSON parser/writer used for JSON persistence.
- `src/service/DataStore.java`: encapsulates reading/writing JSON data files.

**Presentation:** open the interface, its service implementation, exception classes and package structure. Show invalid registration/login inputs returning understandable errors.

## Phase 3 — Web interface and client interaction
**Goal:** forms, JavaScript validation, form interaction and JSON data representation.

- `table_reservation/src/App.jsx`: login, registration, reservation forms, user pages and admin screens.
- `src/auth/AuthContext.jsx`: client-side login/register/logout state.
- `src/api.js`: shared `fetch` client; adds the session bearer token to protected requests and reports API errors.
- `src/App.css` and `src/index.css`: responsive UI and visual states.
- `TableBook-backend/data/*.json`: JSON representation of users, tables and bookings. The React app uses JSON responses from the API rather than hard-coded booking arrays.

**Presentation:** register a user, show browser validation, sign in, reserve a table and show the JSON data files updating on disk.

## Phase 4 — Server-side integration and execution
**Goal:** server-side request processing, validation, sessions, access control, logout and deployment/run instructions.

- `TableBook-backend/src/api/ApiServer.java`: HTTP endpoints for auth, tables, bookings and admin operations; validates requests; issues and invalidates bearer sessions; enforces `USER`/`ADMIN` authorization; returns JSON responses and HTTP status codes.
- `src/service/DataStore.java`: persistent JSON-file storage used by the API.
- `table_reservation/src/auth/AuthContext.jsx`: keeps the issued token and restores the session via `/auth/me`.
- `src/App.jsx`: protected routes redirect guests to login and prevent normal users from opening admin pages; the API independently protects admin endpoints.
- `TableBook-backend/run.sh`, `run.bat`, and this README: build the servlet WAR with Maven for Apache Tomcat; frontend starts through Vite.

**Presentation:** demonstrate wrong credentials, user login, admin login, a direct attempt to access `/admin` as a normal user, an admin status update, logout, and inspect JSON files after changes.

## Main API routes
- Public: `GET /api/health`, `GET /api/tables`, `POST /api/auth/register`, `POST /api/auth/login`
- Signed-in user: `GET /api/auth/me`, `POST /api/auth/logout`, `POST /api/bookings`, `GET /api/bookings/mine`, `DELETE /api/bookings/{id}`
- Admin only: `GET /api/admin/summary`, `GET/PATCH /api/admin/bookings`, `GET/POST/PATCH /api/admin/tables`, `GET/POST/PATCH /api/admin/users`

## Data files
- `data/users.json`: account details, role, active flag and password hash.
- `data/tables.json`: table name, capacity and active flag.
- `data/bookings.json`: customer, table, date/time, guest count, status and owner user ID.
