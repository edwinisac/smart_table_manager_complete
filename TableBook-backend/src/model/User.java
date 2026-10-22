package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Phase 1 entity: user attributes, constructor, accessors and validation.
 */
public class User {

    private String id, name, username, email, passwordHash, role, phone;
    private boolean active;

    public User(String id, String name, String username, String email, String passwordHash, String role, String phone, boolean active) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.phone = phone;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String v) {
        name = v;
    }

    public void setEmail(String v) {
        email = v;
    }

    public void setPhone(String v) {
        phone = v;
    }

    public void setRole(String v) {
        role = v;
    }

    public void setActive(boolean v) {
        active = v;
    }

    public boolean isValid() {
        return username != null && !username.trim().isEmpty() && passwordHash != null && !passwordHash.isBlank() && name != null && !name.trim().isEmpty() && email != null && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") && (role != null && (role.equals("USER") || role.equals("ADMIN")));
    }

    public Map<String, Object> toMap(boolean includeHash) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("name", name);
        m.put("username", username);
        m.put("email", email);
        if (includeHash) {
            m.put("passwordHash", passwordHash);
        
        }m.put("role", role);
        m.put("phone", phone);
        m.put("active", active);
        return m;
    }
}
