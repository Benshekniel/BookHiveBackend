package model.dto;

import java.time.LocalDate;

public class ModeratorDto {


    private String name;
    private String email;
    private String password;
    private int phone;
    private LocalDate dob;
    private String city;
    private int experience;
    private String address;

    public ModeratorDto(String name, String email, String password, int phone, LocalDate dob, String city, int experience, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.city = city;
        this.experience = experience;
        this.address = address;
    }

    public ModeratorDto() {
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

    public int getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
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

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ModeratorDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", dob=" + dob +
                ", city='" + city + '\'' +
                ", experience=" + experience +
                ", address='" + address + '\'' +
                '}';
    }
}
