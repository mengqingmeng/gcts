package com.example.entity;

import java.util.List;

/**
 * Created by mqm on 2017/5/17.
 */
public class EWGProduct {
    private String productName;
    private String overallHazard;//总体危害
    private String cancer;//致癌
    private String DevelopmentalAndReproductiveToxicity;//发展＆ 生殖毒性
    private String AllergiesAndImmunotoxicity;//过敏和免疫毒性
    private String UseRestrictions;//使用限制
    private List<String> infoMations;//成分

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOverallHazard() {
        return overallHazard;
    }

    public void setOverallHazard(String overallHazard) {
        this.overallHazard = overallHazard;
    }

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getDevelopmentalAndReproductiveToxicity() {
        return DevelopmentalAndReproductiveToxicity;
    }

    public void setDevelopmentalAndReproductiveToxicity(String developmentalAndReproductiveToxicity) {
        DevelopmentalAndReproductiveToxicity = developmentalAndReproductiveToxicity;
    }

    public String getAllergiesAndImmunotoxicity() {
        return AllergiesAndImmunotoxicity;
    }

    public void setAllergiesAndImmunotoxicity(String allergiesAndImmunotoxicity) {
        AllergiesAndImmunotoxicity = allergiesAndImmunotoxicity;
    }

    public String getUseRestrictions() {
        return UseRestrictions;
    }

    public void setUseRestrictions(String useRestrictions) {
        UseRestrictions = useRestrictions;
    }

    public List<String> getInfoMations() {
        return infoMations;
    }

    public void setInfoMations(List<String> infoMations) {
        this.infoMations = infoMations;
    }
}
