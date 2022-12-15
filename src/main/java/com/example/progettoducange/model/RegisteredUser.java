package com.example.progettoducange.model;

import java.util.Date;
import java.util.List;

public class RegisteredUser extends User{
        Fridge fridge;
        List<Review> reviews;

        // constructor
        public RegisteredUser(Integer id)
        {
            super.setId(id);
        }

        public RegisteredUser(Integer id, String username, String password, String email)
        {
            super.setId(id);
            super.setUsername(username);
            super.setPassword(password);
            super.setEmail(email);
        }

        public Fridge getFridge() {
            return fridge;
        }
        public void setFridge(Fridge fridge)
        {
            this.fridge = fridge;
        }

        public List<Review> getReviews() {
            return reviews;
        }
        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }
}

