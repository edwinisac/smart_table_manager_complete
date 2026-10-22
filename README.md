# TableBook — full-stack MCA project (Phases 1–4)

TableBook keeps its existing React/Vite frontend, Java model/service/data-store classes, JSON data files and folder layout. The backend request layer has been migrated from Java's built-in `HttpServer` to a **Jakarta Servlet** deployed on Apache Tomcat.

## Install these tools

1. **JDK 17 or newer** — Java compilation/runtime.
2. **Apache Maven 3.8+** — builds the backend WAR (`mvn -version` to check).
3. **Apache Tomcat 10.1+** — runs Jakarta Servlet 6 applications.
4. **Node.js 20.19+ or 22.12+ and npm** — runs the React/Vite frontend (`node -v`, `npm -v`).
5. **VS Code** (optional) — editor. Recommended extensions: Extension Pack for Java and a React/ESLint extension.

No separate database is required; this coursework version continues to persist data in JSON files.

## Run on Linux / Pop!_OS

### 1. Build the Java Servlet backend
```bash
cd TableBook/TableBook-backend
mvn clean package
```
The output should be `target/TableBook.war`.

### 2. Configure Tomcat data storage
Before starting Tomcat, set `TABLEBOOK_DATA_DIR` to the existing backend data directory so bookings/users are kept in the project:
```bash
export TABLEBOOK_DATA_DIR="$PWD/data"
```
Keep this variable in the same terminal/environment used to start Tomcat. The directory must be writable. If Tomcat is installed as a system service, configure this environment variable for that service or use an absolute writable data path.

Copy the WAR to Tomcat's `webapps/` folder, then start Tomcat. For a local Tomcat installation:
```bash
cp target/TableBook.war /path/to/apache-tomcat-10.1.x/webapps/
```
Start Tomcat with its `bin/startup.sh`. The API base URL becomes:
`http://localhost:8080/TableBook/api`

### 3. Configure and run the React frontend
Open a second terminal:
```bash
cd TableBook/table_reservation
cp .env.example .env
npm install
npm run dev
```
Open `http://localhost:5173`. If you change `.env`, restart Vite.

### 4. Test the backend
Open `http://localhost:8080/TableBook/api/health`. It should return JSON indicating the service is healthy.

## Demo accounts
- **Admin:** `admin` / `admin123`
- **Normal user:** `user` / `user123`

## What's included
- Registration and login; token-based authentication and logout
- User table browsing and reservations
- My Bookings and cancellation
- Admin dashboard, booking management, table management and user management
- JSON persistence in `TableBook-backend/data/`
- Phase mapping in `PHASE_MAPPING.md`

## UI and structure
The existing React routes/components and data model were retained. The styling is refined for consistent spacing, stronger contrast, hover/focus feedback and small-screen layouts rather than replacing the UI with a new application.

## Security note
This is an educational project. It currently uses SHA-256 password hashes and in-memory bearer sessions. For production, use a dedicated password hashing algorithm (Argon2/bcrypt), persistent sessions, HTTPS and a database.




how to run backend

go to the tomcat folder -> bin-> run startup.sh as a program

go to http://localhost:8080

# frontend

npm run frontend in the table_reservation folder



