package com.kosign.bizaddress.model;

/**
 * Created by Hyeongpil on 2016. 8. 5..
 */
public class Division {
    String division_name;
    String division_code;

    public Division(String division_name, String division_code) {
        this.division_name = division_name;
        this.division_code = division_code;
    }

    public String getDivision_name() {return division_name;}
    public String getDivision_code() {return division_code;}
}