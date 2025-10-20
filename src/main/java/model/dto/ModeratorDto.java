package model.dto;

import java.time.LocalDate;

public class ModeratorDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private int phone;
    private LocalDate dob;
    private String city;
    private int experience;
    private String address;

    // Default constructor
    public ModeratorDto() {
    }

    // Constructor without ID (for creation requests)
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

    // Constructor with ID (for responses after create/update)
    public ModeratorDto(Long id, String name, String email, String password, int phone, LocalDate dob, String city, int experience, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.city = city;
        this.experience = experience;
        this.address = address;
    }

    // Getters
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

    public int getExperience() {
        return experience;
    }

    public String getAddress() {
        return address;
    }

    // Setters
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

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Validation helper methods
    public boolean isValidForCreation() {
        return name != null && !name.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                phone > 0 &&
                dob != null &&
                city != null && !city.trim().isEmpty() &&
                experience >= 0 &&
                address != null && !address.trim().isEmpty();
    }

    public boolean isValidForUpdate() {
        // For updates, we allow partial data
        return (name == null || !name.trim().isEmpty()) &&
                (email == null || !email.trim().isEmpty()) &&
                (city == null || !city.trim().isEmpty()) &&
                (address == null || !address.trim().isEmpty()) &&
                experience >= 0;
    }

    // Helper method to trim all string fields
    public void trimStringFields() {
        if (name != null) {
            name = name.trim();
        }
        if (email != null) {
            email = email.trim();
        }
        if (city != null) {
            city = city.trim();
        }
        if (address != null) {
            address = address.trim();
        }
    }

    @Override
    public String toString() {
        return "ModeratorDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password != null ? "[PROTECTED]" : "null") + '\'' +
                ", phone=" + phone +
                ", dob=" + dob +
                ", city='" + city + '\'' +
                ", experience=" + experience +
                ", address='" + address + '\'' +
                '}';
    }

    // Override equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModeratorDto that = (ModeratorDto) o;

        if (phone != that.phone) return false;
        if (experience != that.experience) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + phone;
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + experience;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}