package com.kosign.bizaddress.util;

import android.util.Log;

/**
 * Created by jung on 2016-02-23.
 */
public class StringUtil {
    final static String TAG = "StringUtil";

    public static String nvl(String s){
        return nvl(s,"");
    }

    public static String nvl(String s, String val){
        if(s == null || s.equals("null")||s.length()==0 ){
            if(val != null){
                return val;
            } else {
                return "";
            }
        } else{
            return s;
        }
    }


    public static String getTelNo(String clphNo){
        String strReturn = "";
        String temp = "";
        String tel1 = "";
        String tel2 = "";
        String tel3 = "";

        if("".equals(StringUtil.nvl(clphNo, ""))){
            return "";
        }

        temp = clphNo.replace("-", "");

        if(temp.length() > 1 && "02".equals(temp.substring(0, 2))){
            tel1 = temp.substring(0, 2);
            temp = temp.substring(2, temp.length());
        }else if(temp.length() > 3 && "0502,0503,0504,0505,0506,0507,0508".indexOf(temp.substring(0, 4)) > -1 ){
            tel1 = temp.substring(0, 4);
            temp = temp.substring(4, temp.length());
        }else if( temp.length() > 3 ){
            tel1 = temp.substring(0, 3);
            temp = temp.substring(3, temp.length());
        }else{
            return temp;
        }
        if( temp.length() <= 7){
            if( temp.length() <= 3){
                tel2 = temp.substring(0,temp.length());
                return tel1 + "-" + tel2;
            } else {
                tel2 = temp.substring(0,3);
                tel3 = temp.substring(3,temp.length());
                return tel1 + "-" + tel2 + "-" + tel3;
            }
        } else {
            tel2 = temp.substring(0,4);
            tel3 = temp.substring(4,temp.length());
            return tel1 + "-" + tel2 + "-" + tel3;
        }
    }


    //뷰에 보여지는 번호는 (국가코드) 형태로 한다.
    public static String getViewNum(String num, String code){
        try {
            if (num.substring(0, 1).equals("0")) {
                num = num.substring(1);
            }
            num = "(" + code.substring(3) + ")" + num;
        }catch (Exception e){
            Log.d(TAG,"국가코드 오류");
        }

        return num;
    }
    //전화 걸기나 연락처 저장에 사용되는 번호는 바로 붙인다.
    public static String getCallNum(String num, String code){
        try {
            if (num.substring(0, 1).equals("0")) {
                num = num.substring(1);
            }
            num = code.substring(3) + num;
        }catch (Exception e){
            Log.d(TAG,"국가코드 오류");
        }

        return num;
    }

}
