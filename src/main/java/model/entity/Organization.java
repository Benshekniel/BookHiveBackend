package model.entity;


import jakarta.persistence.*;

@Entity
@Table(name="organizations")
public class Organization {

    @Id
    @Column(name="org_id", length = 15)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orgId;

    @Column(name="type", length = 255)
    private String type;

    @Column(name="reg_no", length = 255)
    private String regNo;

    @Column(name="status", length = 255)
    private String status;

    @Column(name="fname", length = 255)
    private String fname;

    @Column(name="lname", length = 255)
    private String lname;

    @Column(name="email", length = 255)
    private String email;

    @Column(name="password", length = 255)
    private String password;

    @Column(name="phone", length = 255)
    private int phone;

    @Column(name="years", length = 255)
    private int years;

    @Column(name="address", length = 255)
    private String address;

    @Column(name="city", length = 255)
    private String city;

    @Column(name="state", length = 255)
    private String state;

    @Column(name="zip", length = 255)
    private String zip;


    public Organization( String type, String reg_no, String status, String fname, String lname, String email, String password, int phone, int years, String address, String city, String state, String zip) {
        this.type = type;
        this.regNo = reg_no;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.years = years;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Organization() {
    }

    public int getOrg_id() {
        return orgId;
    }

    public String getType() {
        return type;
    }

    public String getReg_no() {
        return regNo;
    }

    public String getStatus() {
        return status;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
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

    public int getYears() {
        return years;
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


    public void setOrg_id(int org_id) {
        this.orgId = org_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReg_no(String reg_no) {
        this.regNo = reg_no;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public void setYears(int years) {
        this.years = years;
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

    @Override
    public String toString() {
        return "Organization{" +
                "org_id=" + orgId +
                ", type='" + type + '\'' +
                ", reg_no='" + regNo + '\'' +
                ", status='" + status + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", years=" + years +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
