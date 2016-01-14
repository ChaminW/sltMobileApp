package com.example.oshaniperera.slt;

/**
 * Created by Oshani Perera on 3/6/2017.
 */
public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://100.68.254.81:8080/login.php";
    //public static final String MATERIAL_URL = "http://10.10.14.125:8080/getMaterial.php";


    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";



    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
