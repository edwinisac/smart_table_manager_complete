# TableBook Java Servlet API

This backend uses **Jakarta Servlets** (not `com.sun.net.httpserver.HttpServer`) and is packaged as a WAR for Apache Tomcat 10.1+.

## Requirements
- JDK 17 or newer
- Apache Maven 3.8+
- Apache Tomcat 10.1+
- Node.js 20.19+ (or 22.12+) and npm for the React frontend

## Build and deploy
From this folder:
```bash
mvn clean package
```
Copy `target/TableBook.war` into Tomcat's `webapps/` directory, then start Tomcat. The API base URL is `http://localhost:8080/TableBook/api`.

The servlet stores JSON files under `data/` relative to Tomcat's working directory by default. To choose a stable location, set `TABLEBOOK_DATA_DIR` in the environment before starting Tomcat, for example:
```bash
export TABLEBOOK_DATA_DIR="$PWD/data"
```
Run that export in the same shell before starting Tomcat. Ensure the directory is writable.

## Demo credentials
- Admin: `admin` / `admin123`
- User: `user` / `user123`

## API overview
- `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`, `POST /api/auth/logout`
- `GET /api/tables`
- `POST /api/bookings`, `GET /api/bookings/mine`, `DELETE /api/bookings/{id}`
- Admin-only: `GET /api/admin/summary`, `/bookings`, `/tables`, `/users`; `PATCH /api/admin/bookings/{id}`, `/tables/{id}`, `/users/{id}`; `POST /api/admin/tables`, `/users`

Educational-project note: the app uses SHA-256 password hashes and in-memory bearer sessions. Use a proper password hashing algorithm, persistent sessions, HTTPS and a database before production use.
