package com.example.ass_api.Model;

import com.google.gson.annotations.SerializedName;

public class Product {

    private String id;
    private String image_pro,name_pro,createAt,updateAt;
    private int weight_pro,price_pro;

    public Product() {
    }

    public Product(String id, String image_pro, String name_pro, String createAt, String updateAt, int weight_pro, int price_pro) {
        this.id = id;
        this.image_pro = image_pro;
        this.name_pro = name_pro;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.weight_pro = weight_pro;
        this.price_pro = price_pro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_pro() {
        return image_pro;
    }

    public void setImage_pro(String image_pro) {
        this.image_pro = image_pro;
    }

    public String getName_pro() {
        return name_pro;
    }

    public void setName_pro(String name_pro) {
        this.name_pro = name_pro;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getWeight_pro() {
        return weight_pro;
    }

    public void setWeight_pro(int weight_pro) {
        this.weight_pro = weight_pro;
    }

    public int getPrice_pro() {
        return price_pro;
    }

    public void setPrice_pro(int price_pro) {
        this.price_pro = price_pro;
    }
}
