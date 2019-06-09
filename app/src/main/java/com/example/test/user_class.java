package com.example.test;

public class user_class
{
    private String user_username;
    private String user_fullname;
    private String user_userAddress;
    private double user_lat;
    private double user_lang;
    private int user_type;
    private int followersCount;
    private String ImageUrl;
    private String  email;
    private String uid;
    private String status;
    private String isblocked;
    private String area;
    private String phone ;
    private String address;


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public user_class() {
    }

    public user_class(String user_username, String user_fullname, String user_userAddress, int user_type,String isblocked)
    {
        this.user_username = user_username;
        this.user_fullname = user_fullname;
        this.user_userAddress = user_userAddress;
        this.user_type=user_type;
        this.isblocked = isblocked;
    }
    public user_class(String user_username, String user_fullname, String user_userAddress)
    {
        this.user_username = user_username;
        this.user_fullname = user_fullname;
        this.user_userAddress = user_userAddress;

    }

    public user_class(double user_lat, double user_lang)
    {
        this.user_lat = user_lat;
        this.user_lang = user_lang;
    }

    public user_class(String user_username, String email, String area, String phone, String address) {
        this.user_username = user_username;
        this.email = email;
        this.area = area;
        this.phone = phone;
        this.address = address;
    }

    public double getUser_lat()
    {
        return user_lat;
    }

    public void setUser_lat(double user_lat)
    {
        this.user_lat = user_lat;
    }

    public double getUser_lang()
    {
        return user_lang;
    }

    public void setUser_lang(double user_lang) {
        this.user_lang = user_lang;
    }

    public String getUser_username()
    {
        return user_username;
    }

    public void setUser_username(String user_username)
    {
        this.user_username = user_username;
    }

    public String getUser_fullname()
    {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname)
    {
        this.user_fullname = user_fullname;
    }

    public String getUser_userAddress()
    {
        return user_userAddress;
    }

    public void setUser_userAddress(String user_userAddress)
    {
        this.user_userAddress = user_userAddress;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
    public String getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(String isblocked) {
        this.isblocked = isblocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
