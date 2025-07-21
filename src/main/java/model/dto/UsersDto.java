package model.dto;

import java.time.LocalDate;

public class UsersDto {

    private String email;
    private String password;
    private String name;
    private String fname;
    private String lname;
    private int phone;
    private LocalDate dob;
    private String idType;
    private String idFront;
    private String idBack;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String billImage;

    //Without name,idFront,idBack,billImage
    public UsersDto(String email, String password, String fname, String lname, int phone, LocalDate dob, String idType, String gender, String address, String city, String state, String zip) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.dob = dob;
        this.idType = idType;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    //With name
    public UsersDto(String email, String password, String name, String fname, String lname, int phone, LocalDate dob, String idType, String idFront, String idBack, String gender, String address, String city, String state, String zip, String billImage) {
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

    public UsersDto() {
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

    @Override
    public String toString() {
        return "UsersDto{" +
                "email='" + email + '\'' +
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
                '}';
    }
}
