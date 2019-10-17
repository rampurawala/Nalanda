package com.expedite.apps.nalanda.common;

import com.expedite.apps.nalanda.BuildConfig;

/*
 public class GetIp {
 private static String ip = "192.168.0.104";	
 public static String ip(){
 return "http://"+ip+"/Expedite/Service.asmx?wsdl";		
 }
 public static String imgStr(){
 return "http://"+ip+"/Expedite/Images/";
 }
 public static String resultStr(){
 return "http://"+ip+"/Expedite/results/";
 }
 }
 */
public class GetIp {
    private static String ip = "app.vayuna.com";
    private static String ip_test = "testapp.vayuna.com";
    private static String ip_vehicle = "testvts.vayuna.com";
    public static String NAMESPACE = "http://tempuri.org/";

    public static String ip() {
        //return "http://" + ip + "/Service.asmx";
        if (BuildConfig.DEBUG) {
            return "http://" + ip_test + "/Service.asmx";
        } else {
            return "http://" + ip + "/Service.asmx";
        }
    }

    public static String ipvehicle() {
        return "http://" + ip_vehicle + "/service.asmx";
    }

}
