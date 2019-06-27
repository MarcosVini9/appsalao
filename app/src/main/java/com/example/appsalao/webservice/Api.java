package com.example.appsalao.webservice;

public class Api {
    private static final String ROOT_URL = "https://primeirosite888.000webhostapp.com/appsalaoapi/v1/Api.php?apicall=";

    public static String URL_CREATE_APPSALAO = ROOT_URL + "createsalao";
    public static String URL_READ_APPSALAO = ROOT_URL + "getsalao";
    public static String URL_UPDATE_APPSALAO = ROOT_URL + "updatesalao";
    public static String URL_DELETE_APPSALAO = ROOT_URL + "deletesalao&id=";

}
