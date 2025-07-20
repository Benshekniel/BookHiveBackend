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

    @Column(name="imageFileName", length = 255)
    private String imageFileName;     // Stored filename (you'll generate and save it)

    @Column(name="fileType", length = 255)
    private String fileType;

    public Organization(String type, String regNo, String status, String fname, String lname, String email, String password, int phone, int years, String address, String city, String state, String zip, String imageFileName, String fileType) {
        this.type = type;
        this.regNo = regNo;
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
        this.imageFileName = imageFileName;
        this.fileType = fileType;
    }

    public Organization() {
    }

    public int getOrgId() {
        return orgId;
    }

    public String getType() {
        return type;
    }

    public String getRegNo() {
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

    public String getImageFileName() {
        return imageFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
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

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "orgId=" + orgId +
                ", type='" + type + '\'' +
                ", regNo='" + regNo + '\'' +
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
                ", imageFileName='" + imageFileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
