package Login.dto;

import jakarta.persistence.Column;

public class AllUsersDTO {

    private int user_id;
    private String email;
    private String password;
    private String role;

    public AllUsersDTO(String email, String password, String role) {

        this.email = email;
        this.password = password;
        this.role = role;
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

    @Override
    public String toString() {
        return "AllUsersDTO{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
