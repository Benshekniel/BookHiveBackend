package model.dto;

import model.entity.AllUsers;

public class AllUsersDTO {

    private int user_id;
    private String email;
    private String password;
    private String role;
    private String name;
    private AllUsers.Status status;

    public AllUsersDTO(String email, String password, String role, String name) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public AllUsersDTO(String email, String password, String role, String name, AllUsers.Status status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.status = status;
    }

    public AllUsersDTO() {
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public AllUsers.Status getStatus() {
        return status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(AllUsers.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AllUsersDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
