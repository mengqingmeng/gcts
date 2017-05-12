package com.example.entity;

public class BeautyProduct {

	private String name;                //��Ʒ���
	private float price;               //��Ʒ�۸�
	private Integer CommunityRating;   //��������
	private Integer ExpertRating;      //ר������
	private String EXPERTREVIEWS;      //ר������
	private String COMMUNITYREVIEWS;   //�������
	private String CLAIMS;            //Ҫ��
	private String INGREDIENTS;       //ԭ��
	private String BRANDOVERVIEW;     //Ʒ�Ƹſ�
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getEXPERTREVIEWS() {
		return EXPERTREVIEWS;
	}
	public void setEXPERTREVIEWS(String eXPERTREVIEWS) {
		EXPERTREVIEWS = eXPERTREVIEWS;
	}
	public String getCOMMUNITYREVIEWS() {
		return COMMUNITYREVIEWS;
	}
	public void setCOMMUNITYREVIEWS(String cOMMUNITYREVIEWS) {
		COMMUNITYREVIEWS = cOMMUNITYREVIEWS;
	}
	public String getCLAIMS() {
		return CLAIMS;
	}
	public void setCLAIMS(String cLAIMS) {
		CLAIMS = cLAIMS;
	}
	public String getINGREDIENTS() {
		return INGREDIENTS;
	}
	public void setINGREDIENTS(String iNGREDIENTS) {
		INGREDIENTS = iNGREDIENTS;
	}
	public String getBRANDOVERVIEW() {
		return BRANDOVERVIEW;
	}
	public void setBRANDOVERVIEW(String bRANDOVERVIEW) {
		BRANDOVERVIEW = bRANDOVERVIEW;
	}
	public BeautyProduct() {
		super();
	}
	public Integer getCommunityRating() {
		return CommunityRating;
	}
	public void setCommunityRating(Integer communityRating) {
		CommunityRating = communityRating;
	}
	public Integer getExpertRating() {
		return ExpertRating;
	}
	public void setExpertRating(Integer expertRating) {
		ExpertRating = expertRating;
	}
	public BeautyProduct(String name, float price, Integer communityRating,
			Integer expertRating, String eXPERTREVIEWS,
			String cOMMUNITYREVIEWS, String cLAIMS, String iNGREDIENTS,
			String bRANDOVERVIEW) {
		super();
		this.name = name;
		this.price = price;
		CommunityRating = communityRating;
		ExpertRating = expertRating;
		EXPERTREVIEWS = eXPERTREVIEWS;
		COMMUNITYREVIEWS = cOMMUNITYREVIEWS;
		CLAIMS = cLAIMS;
		INGREDIENTS = iNGREDIENTS;
		BRANDOVERVIEW = bRANDOVERVIEW;
	}	
}
