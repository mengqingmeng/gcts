package com.example.entity;


public class BeautyCategory {
	
	public static enum Type{
		SKINCARE,      //护肤品
		MAKEUP,        //化妆品
		BESTPRODUCTS   //最佳产品
	}
	private Integer CategoryId;
	private String CategoryName;
	private String url;
	private Type type = Type.SKINCARE;
	public Integer getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(Integer categoryId) {
		CategoryId = categoryId;
	}
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public BeautyCategory(Integer categoryId, String categoryName, String url,
			Type type) {
		super();
		CategoryId = categoryId;
		CategoryName = categoryName;
		this.url = url;
		this.type = type;
	}
	public BeautyCategory() {
		super();
	}


    
}
