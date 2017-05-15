package com.example.entity;

import java.util.List;

/**
 * Created by 陈成 on 2017/5/12.
 */
public class AmazonProductInfoMation {
    private int id;
    private String name;
    private List<AmazonProduct> amazonProducts;

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

    public List<AmazonProduct> getAmazonProducts() {
        return amazonProducts;
    }

    public void setAmazonProducts(List<AmazonProduct> amazonProducts) {
        this.amazonProducts = amazonProducts;
    }

    @Override
    public String toString() {
        return name +"<:>" ;

    }
}
