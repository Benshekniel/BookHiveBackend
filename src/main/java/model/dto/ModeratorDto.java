package model.dto;

public class ModeratorDto {


    private int employeeid;

    private String employeename;

    private String email;

    private String password;

    private String address;

    public ModeratorDto(int employeeid, String employeename, String email, String password, String address) {
        this.employeeid = employeeid;
        this.employeename = employeename;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public ModeratorDto() {
    }

    @Override
    public String toString() {
        return "ModeratorDto{" +
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
