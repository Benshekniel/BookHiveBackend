package model.dto;

public class OrgDTO {
    private String type;
    private String reg_no;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String phone;
    private int years;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String imageFileName;
    private String fileType;

    public OrgDTO(String type, String reg_no, String fname, String lname, String email, String password, String phone, int years, String address, String city, String state, String zip, String imageFileName, String fileType) {
        this.type = type;
        this.reg_no = reg_no;
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

    public OrgDTO(String type, String reg_no,  String fname, String lname, String email, String password, String phone, int years, String address, String city, String state, String zip) {
        this.type = type;
        this.reg_no = reg_no;
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

    public OrgDTO() {
    }

    public String getType() {
        return type;
    }

    public String getReg_no() {
        return reg_no;
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

    public String getPhone() {
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

    public void setType(String type) {
        this.type = type;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
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

    public void setPhone(String phone) {
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
        return "OrgDTO{" +
                "type='" + type + '\'' +
                ", reg_no='" + reg_no + '\'' +
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
