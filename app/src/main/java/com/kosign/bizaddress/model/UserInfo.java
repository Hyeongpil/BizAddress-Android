package com.kosign.bizaddress.model;

import java.io.Serializable;

/**
 * Created by Hyeongpil on 2016-02-23.
 */
public class UserInfo implements Serializable{

    String strName = ""; // 이름
    String strPhone = ""; // 핸드폰 번호
    String strCompany = ""; // 사업장명
    String strDivision = ""; // 부서명
    String strPosition = ""; // 직챙
    String strEmail = ""; // 이메일
    String strInnerPhone = ""; // 내선번호
    String strProfileImg = ""; // 프로필 사진
    String strPhone_contryCode = ""; // 핸드폰 국가 코드
    String strInnerPhone_contryCode = ""; // 내선번호 국가 코드

    public String getStrName() {return strName;}
    public void setStrName(String strName) {this.strName = strName;}

    public String getStrPhone() {return strPhone;}
    public void setStrPhone(String strPhone) {this.strPhone = strPhone;}

    public String getStrCompany() {return strCompany;}
    public void setStrCompany(String strCompany) {this.strCompany = strCompany;}

    public String getStrDivision() {return strDivision;}
    public void setStrDivision(String strDivision) {this.strDivision = strDivision;}

    public String getStrPosition() {return strPosition;}
    public void setStrPosition(String strPosition) {this.strPosition = strPosition;}

    public String getStrEmail() {return strEmail;}
    public void setStrEmail(String strEmail) {this.strEmail = strEmail;}

    public String getStrInnerPhone() {return strInnerPhone;}
    public void setStrInnerPhone(String strInnerPhone) {this.strInnerPhone = strInnerPhone;}

    public String getStrProfileImg() {return strProfileImg;}
    public void setStrProfileImg(String strProfileImg) {this.strProfileImg = strProfileImg;}

    public String getStrPhone_contryCode() {return strPhone_contryCode;}
    public void setStrPhone_contryCode(String strPhone_contryCode) {this.strPhone_contryCode = strPhone_contryCode;}

    public String getStrInnerPhone_contryCode() {return strInnerPhone_contryCode;}
    public void setStrInnerPhone_contryCode(String strInnerPhone_contryCode) {this.strInnerPhone_contryCode = strInnerPhone_contryCode;}
}
