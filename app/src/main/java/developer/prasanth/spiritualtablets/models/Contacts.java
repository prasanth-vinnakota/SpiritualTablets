package developer.prasanth.spiritualtablets.models;

public class Contacts {

    private String full_name;
    private String profile_status;
    private String image;
    private String email;
    private String country;
    private String state;
    private String city;
    private String date_of_birth;
    private String mobile_no;
    private String device_token;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




    public Contacts(){}

    public Contacts(String full_name, String profile_status, String image, String email, String country, String state, String city, String date_of_birth, String mobile_no, String device_token) {
        this.full_name = full_name;
        this.profile_status = profile_status;
        this.image = image;
        this.email = email;
        this.country = country;
        this.state = state;
        this.city = city;
        this.date_of_birth = date_of_birth;
        this.mobile_no = mobile_no;
        this.device_token = device_token;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(String profile_status) {
        this.profile_status = profile_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
