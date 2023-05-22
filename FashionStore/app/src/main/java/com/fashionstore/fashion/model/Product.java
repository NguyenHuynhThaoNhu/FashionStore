package com.fashionstore.fashion.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "product")
public class Product implements Serializable {
    @PrimaryKey
    private int id;
    private String name;
    private int price;
    private int sale;
    private String image;
    private List<String> color;
    private List<String> size;
    private String description;
    private int count;
    private String saveColor;
    private String saveSize;
    private int totalPrice;
    private String banner;

    private boolean popular;


    @Ignore
    private List<Image> images;

    public Product() {
    }

    public int getRealPrice() {
        if (sale <= 0) {
            return price;
        }
        return price - (price * sale / 100);
    }

    public Product(int id, String name, int price, int sale, String image, List<String> color, List<String> size, String description, int count, String saveColor, String saveSize, int totalPrice, String banner, boolean popular, List<Image> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.image = image;
        this.color = color;
        this.size = size;
        this.description = description;
        this.count = count;
        this.saveColor = saveColor;
        this.saveSize = saveSize;
        this.totalPrice = totalPrice;
        this.banner = banner;
        this.popular = popular;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSaveColor() {
        return saveColor;
    }

    public void setSaveColor(String saveColor) {
        this.saveColor = saveColor;
    }

    public String getSaveSize() {
        return saveSize;
    }

    public void setSaveSize(String saveSize) {
        this.saveSize = saveSize;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}