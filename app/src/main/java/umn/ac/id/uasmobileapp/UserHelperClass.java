package umn.ac.id.uasmobileapp;

public class UserHelperClass {
    String business_name, email, address, phone_number;

    public UserHelperClass() {}

    public UserHelperClass(String business_name, String email, String address, String phone_number) {
        this.business_name = business_name;
        this.email = email;
        this.address = address;
        this.phone_number = phone_number;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
