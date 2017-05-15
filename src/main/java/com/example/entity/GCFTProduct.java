package com.example.entity;

import java.util.List;

/**
 * 药监局 国产非特殊用途 产品
 * Created by mqm on 2017/5/15.
 */
public class GCFTProduct {
    private String name;//产品名
    private String orgName;//原产地
    private String applySN;//备案号
    private String applyState;//备案状态
    private String remark;//说明
    private String displayName;//显示名称
    private String provinceConfirm;//备案日期
    private String enterpriseAddress;//企业地址
    private String enterpriseName;//企业名称
    private String sjscqys;//实际生产企业，是个json数组
    private List<String> infoMations;//成分

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getApplySN() {
        return applySN;
    }

    public void setApplySN(String applySN) {
        this.applySN = applySN;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProvinceConfirm() {
        return provinceConfirm;
    }

    public void setProvinceConfirm(String provinceConfirm) {
        this.provinceConfirm = provinceConfirm;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getSjscqys() {
        return sjscqys;
    }

    public void setSjscqys(String sjscqys) {
        this.sjscqys = sjscqys;
    }

    public List<String> getInfoMations() {
        return infoMations;
    }

    public void setInfoMations(List<String> infoMations) {
        this.infoMations = infoMations;
    }
}
