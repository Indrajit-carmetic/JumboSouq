package com.jumbosouq.com.Allurl;

public class Allurl {

    public static String baseUrl;
    public static String ForgotPassword;
    public static String Weeklydeals;
    public static String AdminLogin;
    public static String Productdetailsl;
    public static String Searh;
    public static String Category;





    static {
        baseUrl = "https://www.jumbosouq.com";
        ForgotPassword = baseUrl +"/rest/default/V1/customers/password";
        Weeklydeals = baseUrl+"/rest/default/V1/products?";
        AdminLogin = baseUrl+"/rest/default/V1/integration/admin/token";
        Productdetailsl = baseUrl+"/rest/default/V1/products/";
        Searh = baseUrl+"/rest/default/V1/products?";
        Category = baseUrl+"/rest/default/V1/categories";


    }

}
