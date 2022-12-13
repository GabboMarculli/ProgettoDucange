package com.example.progettoducange.model;

import java.util.List;

public class RegisteredUser extends User{
        Fridge fridge;
        List<Review> reviews;

        // constructor
        public RegisteredUser(Long id)
        {
            super.setId(id);
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

