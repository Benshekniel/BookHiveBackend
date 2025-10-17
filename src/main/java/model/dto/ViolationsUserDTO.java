package model.dto;

public class ViolationsUserDTO {
    private String email;
    private String reason;


    public ViolationsUserDTO(String email, String reason) {
        this.email = email;
        this.reason = reason;
    }

    public ViolationsUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ViolationsUserDTO{" +
                "email='" + email + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
