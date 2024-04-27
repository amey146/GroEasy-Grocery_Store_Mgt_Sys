package com.example.groeasy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlinx.serialization.Serializable;

@Serializable
@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String desc;
    private String brand;
    private String category;
    private Float price;
    private Float quantity;
    private Integer unit;
    private Long expiry;

    public Product(int id, String name, String desc, String brand, String category, Float price, Float quantity, Integer unit, Long expiry) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}



