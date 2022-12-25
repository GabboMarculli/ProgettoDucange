package com.example.progettoducange.DTO;

public class ReviewsDTO {
    String profile;
    int rate;
    String comment;

    public ReviewsDTO(String profile, int rate, String comment) {
        this.profile = profile;
        this.rate = rate;
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
