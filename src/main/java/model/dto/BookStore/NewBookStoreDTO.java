package model.dto.BookStore;

public class NewBookStoreDTO {
    private String fName;
    private String lName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String district;//STATE
    private String postalCode;//zip

    private String storeName;
    private String businessRegistrationNumber;
    private Integer esblishedYears;

    private String password;

    private String registryImage;
    private String registryImageType;

    public NewBookStoreDTO() {
    }

    public NewBookStoreDTO(String fName, String lName, String email, String phoneNumber, String address, String city, String district, String postalCode, String storeName, String businessRegistrationNumber, Integer esblishedYears, String password) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.district = district;
        this.postalCode = postalCode;
        this.storeName = storeName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.esblishedYears = esblishedYears;
        this.password = password;
    }

    public NewBookStoreDTO(String fName, String lName, String email, String phoneNumber, String address, String city, String district, String postalCode, String storeName, String businessRegistrationNumber, Integer esblishedYears, String password, String registryImage, String registryImageType) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.district = district;
        this.postalCode = postalCode;
        this.storeName = storeName;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.esblishedYears = esblishedYears;
        this.password = password;
        this.registryImage = registryImage;
        this.registryImageType = registryImageType;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public Integer getEsblishedYears() {
        return esblishedYears;
    }

    public void setEsblishedYears(Integer esblishedYears) {
        this.esblishedYears = esblishedYears;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistryImage() {
        return registryImage;
    }

    public void setRegistryImage(String registryImage) {
        this.registryImage = registryImage;
    }

    public String getRegistryImageType() {
        return registryImageType;
    }

    public void setRegistryImageType(String registryImageType) {
        this.registryImageType = registryImageType;
    }

    @Override
    public String toString() {
        return "NewBookStoreDTO{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", storeName='" + storeName + '\'' +
                ", businessRegistrationNumber='" + businessRegistrationNumber + '\'' +
                ", esblishedYears=" + esblishedYears +
                ", password='" + password + '\'' +
                ", registryImage='" + registryImage + '\'' +
                ", registryImageType='" + registryImageType + '\'' +
                '}';
    }
}
