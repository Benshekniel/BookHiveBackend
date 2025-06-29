package model.entity;


import jakarta.persistence.*;


@Entity
@Table(name="moderator")
public class Moderator {


    @Id
    @Column(name="id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int employeeid;
    @Column(name="name", length = 255)
    private String employeename;
    @Column(name="email", length = 255)
    private String email;
    @Column(name="password", length = 255)
    private String password;
    @Column(name="address", length = 255)
    private String address;


    public Moderator(int employeeid, String employeename, String email, String password, String address) {
        this.employeeid = employeeid;
        this.employeename = employeename;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public Moderator() {

    }

    @Override
    public String toString() {
        return "Moderator{" +
                "employeeid=" + employeeid +
                ", employeename='" + employeename + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public String getEmployeename() {
        return employeename;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public void setEmployeeid(int employeeid) {
        this.employeeid = employeeid;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
