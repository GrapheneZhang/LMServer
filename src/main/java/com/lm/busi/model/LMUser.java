package com.lm.busi.model;

public class LMUser {
    private Long id;

    private String userName;

    private String userMac;

    private String userProofRule;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserMac() {
        return userMac;
    }

    public void setUserMac(String userMac) {
        this.userMac = userMac == null ? null : userMac.trim();
    }

    public String getUserProofRule() {
        return userProofRule;
    }

    public void setUserProofRule(String userProofRule) {
        this.userProofRule = userProofRule == null ? null : userProofRule.trim();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}