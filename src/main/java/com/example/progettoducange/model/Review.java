package com.example.progettoducange.model;

import java.util.Date;
import java.util.List;

public class Review {
    private Long idUser;
    private List<String> posts;
    private Date lastUpdate;

    public Date getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getPosts() {
        return posts;
    }
    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public Long getIdUser() {
        return idUser;
    }
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Post{" +
                "idUser=" + idUser +
                ", posts=" + posts +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public Review(Long id, List<String> post)
    {
        this.idUser = id;
        this.posts = post;

        // put in current timestamp
        this.lastUpdate = new Date();
    }
}
