package Login.entity;

import jakarta.persistence.*;

@Entity
@Table(name="All_Users")
public class AllUsers {

    @Id
    @Column(name="user_id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;
    @Column(name="email", length = 255)
    private String email;
    @Column(name="password", length = 255)
    private String password;
    @Column(name="role", length = 255)
    private String role;

    public AllUsers(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AllUsers() {
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
        return "AllUsers{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
