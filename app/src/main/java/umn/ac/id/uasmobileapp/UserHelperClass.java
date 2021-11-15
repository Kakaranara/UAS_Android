package umn.ac.id.uasmobileapp;

public class UserHelperClass {
    String bName, email, password;

    public UserHelperClass() {}

    public UserHelperClass(String bName, String email, String password) {
        this.bName = bName;
        this.email = email;
        this.password = password;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
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
}
