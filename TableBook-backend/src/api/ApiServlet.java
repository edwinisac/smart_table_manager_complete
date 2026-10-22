package api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import exception.ApiException;
import exception.InvalidUserException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.DataStore;
import service.UserService;
import util.Json;

/**
 * Phase 4 HTTP API: REST endpoints, server-side auth, sessions, role protection
 * and JSON persistence.
 */
@WebServlet(urlPatterns = "/api/*", loadOnStartup = 1)
public class ApiServlet extends HttpServlet {

    private DataStore store;
    private final UserService users = new UserService();
    private final Map<String, String> sessions = new ConcurrentHashMap<>(); // token -> user id

    public ApiServlet() {
    }

    @Override
    public void init() throws ServletException {
        try {
            this.store = new DataStore(Path.of(System.getenv().getOrDefault("TABLEBOOK_DATA_DIR", "data")));
        } catch (Exception e) {
            throw new ServletException("Could not initialize TableBook data store", e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws IOException {
        addCors(req, res);
        res.setStatus(204);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(req, res);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(req, res);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            handle(req, res);
        } else {
            super.service(req, res);
    
        }}

    private void handle(HttpServletRequest ex, HttpServletResponse response) throws IOException {
        addCors(ex, response);
        try {
            String path = ex.getPathInfo();
            if (path == null) {
                path = "/";
            }
            String method = ex.getMethod();
            Map<String, Object> body = body(ex);
            Object result;
            if (path.equals("/health") && method.equals("GET")) {
                result = Map.of("ok", true, "service", "TableBook API");
            } else if (path.equals("/auth/register") && method.equals("POST")) {
                result = register(body);
            } else if (path.equals("/auth/login") && method.equals("POST")) {
                result = login(body);
            } else if (path.equals("/auth/me") && method.equals("GET")) {
                result = currentUser(ex);
            } else if (path.equals("/auth/logout") && method.equals("POST")) {
                String token = token(ex);
                sessions.remove(token);
                result = Map.of("message", "Logged out");
            } else if (path.equals("/tables") && method.equals("GET")) {
                result = publicTables();
            } else if (path.equals("/bookings") && method.equals("POST")) {
                result = createBooking(ex, body);
            } else if (path.equals("/bookings/mine") && method.equals("GET")) {
                result = myBookings(ex);
            } else if (path.startsWith("/bookings/") && method.equals("DELETE")) {
                result = cancelBooking(ex, path.substring("/bookings/".length()));
            } else if (path.equals("/admin/summary") && method.equals("GET")) {
                requireAdmin(ex);
                result = summary();
            } else if (path.equals("/admin/bookings") && method.equals("GET")) {
                requireAdmin(ex);
                result = adminBookings();
            } else if (path.startsWith("/admin/bookings/") && method.equals("PATCH")) {
                requireAdmin(ex);
                result = updateBooking(path.substring("/admin/bookings/".length()), body);
            } else if (path.equals("/admin/tables") && method.equals("GET")) {
                requireAdmin(ex);
                result = store.objects("tables.json");
            } else if (path.equals("/admin/tables") && method.equals("POST")) {
                requireAdmin(ex);
                result = addTable(body);
            } else if (path.startsWith("/admin/tables/") && method.equals("PATCH")) {
                requireAdmin(ex);
                result = editTable(path.substring("/admin/tables/".length()), body);
            } else if (path.equals("/admin/users") && method.equals("GET")) {
                requireAdmin(ex);
                result = adminUsers();
            } else if (path.startsWith("/admin/users/") && method.equals("PATCH")) {
                requireAdmin(ex);
                result = editUser(path.substring("/admin/users/".length()), body);
            } else if (path.equals("/admin/users") && method.equals("POST")) {
                requireAdmin(ex);
                result = adminCreateUser(body);
            } else {
                throw new ApiException(404, "Endpoint not found");
            }
            send(response, 200, result);
        } catch (ApiException e) {
            send(response, e.getStatus(), Map.of("error", e.getMessage()));
        } catch (InvalidUserException e) {
            send(response, 400, Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            send(response, 400, Map.of("error", e.getMessage() == null ? "Invalid request" : e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            send(response, 500, Map.of("error", "Server error: " + (e.getMessage() == null ? "unexpected failure" : e.getMessage())));
        }
    }

    private void addCors(HttpServletRequest e, HttpServletResponse r) {
        String origin = e.getHeader("Origin");
        if ("http://localhost:5173".equals(origin) || "http://127.0.0.1:5173".equals(origin)) {
            r.setHeader("Access-Control-Allow-Origin", origin);
        
        }r.setHeader("Vary", "Origin");
        r.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        r.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, DELETE, OPTIONS");
        r.setContentType("application/json");
        r.setCharacterEncoding("UTF-8");
    }

    private Map<String, Object> body(HttpServletRequest e) throws IOException {
        String s = new String(e.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (s.isBlank()) {
            return new LinkedHashMap<>();
        
        }return Json.object(Json.parse(s));
    }

    private void send(HttpServletResponse e, int status, Object data) throws IOException {
        e.setStatus(status);
        e.setContentType("application/json");
        e.setCharacterEncoding("UTF-8");
        e.getWriter().write(Json.stringify(data));
    }

    private String token(HttpServletRequest e) {
        String h = e.getHeader("Authorization");
        return h != null && h.startsWith("Bearer ") ? h.substring(7) : null;
    }

    private Map<String, Object> requireUser(HttpServletRequest e) throws Exception {
        String t = token(e), id = t == null ? null : sessions.get(t);
        if (id == null) {
            throw new ApiException(401, "Please log in to continue");
        
        }for (Map<String, Object> u : store.objects("users.json")) {
            if (id.equals(u.get("id")) && Boolean.TRUE.equals(u.get("active"))) {
                return u;
        
            }
        }throw new ApiException(401, "Session is no longer valid");
    }

    private Map<String, Object> requireAdmin(HttpServletRequest e) throws Exception {
        Map<String, Object> u = requireUser(e);
        if (!"ADMIN".equals(u.get("role"))) {
            throw new ApiException(403, "Administrator access required");
        
        }return u;
    }

    private Map<String, Object> publicUser(Map<String, Object> u) {
        Map<String, Object> m = new LinkedHashMap<>(u);
        m.remove("passwordHash");
        return m;
    }

    private Object register(Map<String, Object> b) throws Exception {
        String name = str(b, "name"), username = str(b, "username"), email = str(b, "email"), password = str(b, "password"), phone = opt(b, "phone");
        if (password.length() < 6) {
            throw new ApiException(400, "Password must be at least 6 characters");
        
        }List<Map<String, Object>> all = store.objects("users.json");
        for (Map<String, Object> u : all) {
            if (username.equalsIgnoreCase(String.valueOf(u.get("username"))) || email.equalsIgnoreCase(String.valueOf(u.get("email")))) {
                throw new ApiException(409, "Username or email already exists");
        
            }
        }User u = new User(UUID.randomUUID().toString(), name, username, email, UserService.hash(password), "USER", phone, true);
        users.validate(u);
        all.add(u.toMap(true));
        store.write("users.json", all);
        return Map.of("message", "Account created. You can now log in.");
    }

    private Object login(Map<String, Object> b) throws Exception {
        String username = str(b, "username"), password = str(b, "password");
        for (Map<String, Object> u : store.objects("users.json")) {
            if (username.equalsIgnoreCase(String.valueOf(u.get("username"))) && UserService.passwordMatches(password, String.valueOf(u.get("passwordHash"))) && Boolean.TRUE.equals(u.get("active"))) {
                String t = UUID.randomUUID().toString();
                sessions.put(t, String.valueOf(u.get("id")));
                return Map.of("token", t, "user", publicUser(u));
            }
        }
        throw new ApiException(401, "Invalid username or password");
    }

    private Object currentUser(HttpServletRequest e) throws Exception {
        return publicUser(requireUser(e));
    }

    private Object publicTables() throws Exception {
        List<Map<String, Object>> tables = store.objects("tables.json");
        List<Map<String, Object>> bookings = store.objects("bookings.json");
        LocalDate today = LocalDate.now();
        for (Map<String, Object> t : tables) {
            boolean booked = false;
            for (Map<String, Object> b : bookings) {
                if (String.valueOf(b.get("tableId")).equals(String.valueOf(t.get("id"))) && today.toString().equals(b.get("date")) && ("CONFIRMED".equals(b.get("status")) || "PENDING".equals(b.get("status")))) {
                    booked = true;
            
                }
            }t.put("available", Boolean.TRUE.equals(t.get("active")) && !booked);
        }
        return tables;
    }

    private Object createBooking(HttpServletRequest e, Map<String, Object> b) throws Exception {
        Map<String, Object> u = requireUser(e);
        String date = str(b, "date"), time = str(b, "time");
        int tableId = integer(b.get("tableId")), guests = integer(b.get("guests"));
        if (!LocalDate.parse(date).isAfter(LocalDate.now().minusDays(1))) {
            throw new ApiException(400, "Choose today or a future date");
        
        }Map<String, Object> table = store.byId("tables.json", String.valueOf(tableId));
        if (table == null || !Boolean.TRUE.equals(table.get("active"))) {
            throw new ApiException(400, "Table is not available");
        
        }if (guests < 1 || guests > integer(table.get("capacity"))) {
            throw new ApiException(400, "Guest count exceeds table capacity");
        
        }List<Map<String, Object>> all = store.objects("bookings.json");
        for (Map<String, Object> x : all) {
            if (String.valueOf(x.get("tableId")).equals(String.valueOf(tableId)) && date.equals(x.get("date")) && time.equals(x.get("time")) && ("CONFIRMED".equals(x.get("status")) || "PENDING".equals(x.get("status")))) {
                throw new ApiException(409, "This table is already booked at that time");
        
            }
        }Map<String, Object> n = new LinkedHashMap<>();
        n.put("id", "b-" + UUID.randomUUID().toString().substring(0, 8));
        n.put("userId", u.get("id"));
        n.put("customerName", str(b, "customerName"));
        n.put("phone", opt(b, "phone"));
        n.put("tableId", tableId);
        n.put("tableName", table.get("name"));
        n.put("guests", guests);
        n.put("date", date);
        n.put("time", time);
        n.put("notes", opt(b, "notes"));
        n.put("status", "CONFIRMED");
        n.put("createdAt", Instant.now().toString());
        all.add(n);
        store.write("bookings.json", all);
        return n;
    }

    private Object myBookings(HttpServletRequest e) throws Exception {
        Map<String, Object> u = requireUser(e);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map<String, Object> b : store.objects("bookings.json")) {
            if (String.valueOf(u.get("id")).equals(String.valueOf(b.get("userId")))) {
                out.add(b);
        
            }
        }out.sort(Comparator.comparing(x -> String.valueOf(x.get("date")) + String.valueOf(x.get("time"))));
        return out;
    }

    private Object cancelBooking(HttpServletRequest e, String id) throws Exception {
        Map<String, Object> u = requireUser(e);
        List<Map<String, Object>> all = store.objects("bookings.json");
        for (Map<String, Object> b : all) {
            if (id.equals(String.valueOf(b.get("id")))) {
                if (!"ADMIN".equals(u.get("role")) && !String.valueOf(u.get("id")).equals(String.valueOf(b.get("userId")))) {
                    throw new ApiException(403, "You cannot cancel another user's booking");
                
                }b.put("status", "CANCELLED");
                store.write("bookings.json", all);
                return b;
            }
        
        }throw new ApiException(404, "Booking not found");
    }

    private Object adminBookings() throws Exception {
        List<Map<String, Object>> all = store.objects("bookings.json");
        List<Map<String, Object>> users = store.objects("users.json");
        for (Map<String, Object> b : all) {
            for (Map<String, Object> u : users) {
                if (String.valueOf(u.get("id")).equals(String.valueOf(b.get("userId")))) {
                    b.put("username", u.get("username"));
                    b.put("customerEmail", u.get("email"));
                    break;
                }
        
            }}
        all.sort(Comparator.comparing((Map<String, Object> x) -> String.valueOf(x.get("createdAt"))).reversed());
        return all;
    }

    private Object updateBooking(String id, Map<String, Object> body) throws Exception {
        List<Map<String, Object>> all = store.objects("bookings.json");
        String status = str(body, "status").toUpperCase(Locale.ROOT);
        if (!Set.of("CONFIRMED", "PENDING", "CANCELLED", "COMPLETED").contains(status)) {
            throw new ApiException(400, "Invalid booking status");
        
        }for (Map<String, Object> b : all) {
            if (id.equals(String.valueOf(b.get("id")))) {
                b.put("status", status);
                store.write("bookings.json", all);
                return b;
            }
        
        }throw new ApiException(404, "Booking not found");
    }

    private Object summary() throws Exception {
        List<Map<String, Object>> bs = store.objects("bookings.json"), us = store.objects("users.json"), ts = store.objects("tables.json");
        long pending = bs.stream().filter(b -> "PENDING".equals(b.get("status"))).count();
        long confirmed = bs.stream().filter(b -> "CONFIRMED".equals(b.get("status"))).count();
        return Map.of("totalBookings", bs.size(), "totalUsers", us.stream().filter(u -> "USER".equals(u.get("role"))).count(), "totalTables", ts.size(), "availableTables", ts.stream().filter(t -> Boolean.TRUE.equals(t.get("active"))).count(), "confirmedBookings", confirmed, "pendingBookings", pending, "cancelledBookings", bs.stream().filter(b -> "CANCELLED".equals(b.get("status"))).count());
    }

    private Object adminUsers() throws Exception {
        List<Map<String, Object>> a = new ArrayList<>();
        for (Map<String, Object> u : store.objects("users.json")) {
            a.add(publicUser(u));
        
        }return a;
    }

    private Object editUser(String id, Map<String, Object> b) throws Exception {
        List<Map<String, Object>> all = store.objects("users.json");
        for (Map<String, Object> u : all) {
            if (id.equals(String.valueOf(u.get("id")))) {
                if (b.containsKey("active")) {
                    u.put("active", b.get("active"));
                
                }if (b.containsKey("role") && Set.of("USER", "ADMIN").contains(b.get("role"))) {
                    u.put("role", b.get("role"));
                
                }if (b.containsKey("name")) {
                    u.put("name", b.get("name"));
                
                }if (b.containsKey("email")) {
                    u.put("email", b.get("email"));
                
                }store.write("users.json", all);
                return publicUser(u);
            }
        
        }throw new ApiException(404, "User not found");
    }

    private Object adminCreateUser(Map<String, Object> b) throws Exception {
        String password = str(b, "password");
        if (password.length() < 6) {
            throw new ApiException(400, "Password must be at least 6 characters");
        
        }String role = opt(b, "role").isBlank() ? "USER" : str(b, "role").toUpperCase(Locale.ROOT);
        if (!Set.of("USER", "ADMIN").contains(role)) {
            throw new ApiException(400, "Invalid role");
        
        }String username = str(b, "username"), email = str(b, "email");
        List<Map<String, Object>> all = store.objects("users.json");
        for (Map<String, Object> u : all) {
            if (username.equalsIgnoreCase(String.valueOf(u.get("username"))) || email.equalsIgnoreCase(String.valueOf(u.get("email")))) {
                throw new ApiException(409, "Username or email already exists");
        
            }
        }User u = new User(UUID.randomUUID().toString(), str(b, "name"), username, email, UserService.hash(password), role, opt(b, "phone"), true);
        users.validate(u);
        all.add(u.toMap(true));
        store.write("users.json", all);
        return u.toMap(false);
    }

    private Object addTable(Map<String, Object> b) throws Exception {
        String name = str(b, "name");
        int cap = integer(b.get("capacity"));
        if (cap < 1 || cap > 20) {
            throw new ApiException(400, "Capacity must be between 1 and 20");
        
        }List<Map<String, Object>> all = store.objects("tables.json");
        int id = all.stream().mapToInt(x -> integer(x.get("id"))).max().orElse(0) + 1;
        Map<String, Object> t = new LinkedHashMap<>();
        t.put("id", id);
        t.put("name", name);
        t.put("capacity", cap);
        t.put("active", true);
        all.add(t);
        store.write("tables.json", all);
        return t;
    }

    private Object editTable(String id, Map<String, Object> b) throws Exception {
        List<Map<String, Object>> all = store.objects("tables.json");
        for (Map<String, Object> t : all) {
            if (id.equals(String.valueOf(t.get("id")))) {
                if (b.containsKey("name")) {
                    t.put("name", str(b, "name"));
                
                }if (b.containsKey("capacity")) {
                    int c = integer(b.get("capacity"));
                    if (c < 1 || c > 20) {
                        throw new ApiException(400, "Capacity must be between 1 and 20");
                    
                    }t.put("capacity", c);
                }
                if (b.containsKey("active")) {
                    t.put("active", b.get("active"));
                
                }store.write("tables.json", all);
                return t;
            }
        
        }throw new ApiException(404, "Table not found");
    }

    private String str(Map<String, Object> m, String k) {
        Object v = m.get(k);
        if (v == null || String.valueOf(v).trim().isEmpty()) {
            throw new IllegalArgumentException(k + " is required");
        
        }return String.valueOf(v).trim();
    }

    private String opt(Map<String, Object> m, String k) {
        Object v = m.get(k);
        return v == null ? "" : String.valueOf(v).trim();
    }

    private int integer(Object v) {
        if (v instanceof Number) {
            return ((Number) v).intValue();
        
        }try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            throw new IllegalArgumentException("Expected a number");
        }
    }
}
