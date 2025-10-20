package model.dto.organizationNew;

public class OrganizationNewDTO {

    private String regNo;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String type;

    private int phone;
    private int years;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String imageFileName;


    private String organizationName;

    // Stored filename (you'll generate and save it)


    public OrganizationNewDTO(String regNo, String fname, String lname, String email, String password, String type, int phone, int years, String address, String city, String state, String zip, String imageFileName, String organizationName) {
        this.regNo = regNo;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.type = type;
        this.phone = phone;
        this.years = years;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.imageFileName = imageFileName;
        this.organizationName = organizationName;
    }

    public OrganizationNewDTO() {
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
