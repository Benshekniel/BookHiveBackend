package model.messageResponse;

public class LoginResponse {
    String message;
    Boolean status;
    String role;

    public LoginResponse(String message, Boolean status ,String role) {
        this.message = message;
        this.status = status;
        this.role = role;
    }

    public LoginResponse(String message, Boolean status ) {
        this.message = message;
        this.status = status;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }
}