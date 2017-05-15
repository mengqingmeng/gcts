package com.example.entity;

import java.util.List;

/**
 * Created by 陈成 on 2017/5/12.
 */
public class AmazonProduct {

    private int id;
    private String name;
    private String price;
    private String mark;
    private String countAnswers;
    private String countReviews;
    private String details;
    private String aboutThisProduct;
    private String description;
    private String saleWell;
    private String size;
   // private List<AmazonProductInfoMation> amazonProductInfoMations;
    private List<String> infoMations;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCountAnswers() {
        return countAnswers;
    }

    public void setCountAnswers(String countAnswers) {
        this.countAnswers = countAnswers;
    }

    public String getCountReviews() {
        return countReviews;
    }

    public void setCountReviews(String countReviews) {
        this.countReviews = countReviews;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAboutThisProduct() {
        return aboutThisProduct;
    }

    public void setAboutThisProduct(String aboutThisProduct) {
        this.aboutThisProduct = aboutThisProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSaleWell() {
        return saleWell;
    }

    public void setSaleWell(String saleWell) {
        this.saleWell = saleWell;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

  /*  public List<AmazonProductInfoMation> getAmazonProductInfoMations() {
        return amazonProductInfoMations;
    }

    public void setAmazonProductInfoMations(List<AmazonProductInfoMation> amazonProductInfoMations) {
        this.amazonProductInfoMations = amazonProductInfoMations;
    }*/

    public List<String> getInfoMations() {
        return infoMations;
    }

    public void setInfoMations(List<String> infoMations) {
        this.infoMations = infoMations;
    }
}
