package model.entity;


import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name="moderator")
public class Moderator {


    @Id
    @Column(name="id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name", length = 255)
    private String name;
    @Column(name="email", length = 255)
    private String email;
    @Column(name="password", length = 255)
    private String password;
    @Column(name="phone", length = 255)
    private int phone;
    @Column(name="dob", length = 255)
    private LocalDate dob;
    @Column(name="city", length = 255)
    private String city;
    @Column(name="experience", length = 255)
    private Integer experience;
    @Column(name="address", length = 255)
    private String address;

    public Moderator(String name, String email, String password, int phone, LocalDate dob, String city, Integer experience, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.city = city;
        this.experience = experience;
        this.address = address;
    }

    public Moderator() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPhone() {
        return phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getCity() {
        return city;
    }

    public Integer getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Moderator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", dob=" + dob +
                ", city=" + city +
                ", experience=" + experience +
                ", address='" + address + '\'' +
                '}';
    }
}
