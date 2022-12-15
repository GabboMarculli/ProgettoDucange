package com.example.progettoducange.model;

import java.util.Date;

public abstract class User {
    public Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String profilePic;
    private Date createdDate;
    private Date updatedDate;

    private String email;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicUrl() {
        return profilePic;
    }
    public void setProfilePicUrl(String profilePic) {
        this.profilePic = profilePic;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail()
    {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", email='" + email + '\'' +
                '}';
    }
}
