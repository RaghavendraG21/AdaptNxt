//package com.ecommerce.api.dto;
//
//public class SignUpRequest {
//    private String username;
//    private String password;
//    private String email;
//
//    // Getters and Setters
//    public String getUsername() { return username; }
//    public void setUsername(String username) { this.username = username; }
//
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//}


package com.ecommerce.api.dto;

public class SignUpRequest {
    private String username;
    private String password;
    private String email;

    // ✅ Add this field
    private String role;

    // ✅ Getters and setters for all fields
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // ✅ Getter and setter for role
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}

