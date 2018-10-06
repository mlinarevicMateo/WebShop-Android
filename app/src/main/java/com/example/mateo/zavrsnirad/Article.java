package com.example.mateo.zavrsnirad;

import java.io.Serializable;

/**
 * Created by mlinar on 5/12/2018.
 */

public class Article implements Serializable {
    String name, price, quantity, description, image, category;

    public Article(String name, String price, String quantity, String description, String image, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {

        return price;
    }

    public void setPrice(String price) {

        this.price = price;
    }

    public String getQuantity() {

        return quantity;
    }

    public void setQuantity(String quantity) {

        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
