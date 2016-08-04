package com.kosign.bizaddress.model;

import java.io.Serializable;

/**
 * Created by Hyeongpil on 2016-02-23.
 */
public class UserInfo implements Serializable{

    String strName; // 이름
    String strPhoneNum; // 핸드폰 번호
    String strCompany; // 사업장명
    String strDivision; // 부서명
    String strPosition; // 직챙
    String strEmail; // 이메일
    String strInnerPhoneNum; // 내선번호
    String strProfileImg; // 프로필 사진

    public String getStrName() {return strName;}
    public void setStrName(String strName) {this.strName = strName;}

    public String getStrPhoneNum() {return strPhoneNum;}
    public void setStrPhoneNum(String strPhoneNum) {this.strPhoneNum = strPhoneNum;}

    public String getStrCompany() {return strCompany;}
    public void setStrCompany(String strCompany) {this.strCompany = strCompany;}

    public String getStrDivision() {return strDivision;}
    public void setStrDivision(String strDivision) {this.strDivision = strDivision;}

    public String getStrPosition() {return strPosition;}
    public void setStrPosition(String strPosition) {this.strPosition = strPosition;}

    public String getStrEmail() {return strEmail;}
    public void setStrEmail(String strEmail) {this.strEmail = strEmail;}

    public String getStrInnerPhoneNum() {return strInnerPhoneNum;}
    public void setStrInnerPhoneNum(String strInnerPhoneNum) {this.strInnerPhoneNum = strInnerPhoneNum;}

    public String getStrProfileImg() {return strProfileImg;}
    public void setStrProfileImg(String strProfileImg) {this.strProfileImg = strProfileImg;}
}
