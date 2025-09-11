package model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id") // Ensures DB column is user_id
    private Long userId;

    // Temporarily allow nulls for schema migration
    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "fname", nullable = true)
    private String fname;

    @Column(name = "lname", nullable = true)
    private String lname;

    @Column(name="phone", length = 255)
    private int phone;

    @Column(name="dob", length = 255)
    private LocalDate dob;

    @Column(name="idType", length = 255)
    private String idType;

    @Column(name="idFront", length = 255)
    private String idFront;

    @Column(name="idBack", length = 255)
    private String idBack;

    @Column(name="gender", length = 255)
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name="city", length = 255)
    private String city;

    @Column(name="state", length = 255)
    private String state;

    @Column(name="zip", length = 255)
    private String zip;

    @Column(name="billImage", length = 255)
    private String billImage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Users(String email, String password, String name, String fname, String lname, int phone, LocalDate dob, String idType, String idFront, String idBack, String gender, String address, String city, String state, String zip, String billImage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.dob = dob;
        this.idType = idType;
        this.idFront = idFront;
        this.idBack = idBack;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.billImage = billImage;
    }

    public Users() {
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getPhone() {
        return phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getIdType() {
        return idType;
    }

    public String getIdFront() {
        return idFront;
    }

    public String getIdBack() {
        return idBack;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getBillImage() {
        return billImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setBillImage(String billImage) {
        this.billImage = billImage;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone=" + phone +
                ", dob='" + dob + '\'' +
                ", idType='" + idType + '\'' +
                ", idFront='" + idFront + '\'' +
                ", idBack='" + idBack + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", billImage='" + billImage + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
