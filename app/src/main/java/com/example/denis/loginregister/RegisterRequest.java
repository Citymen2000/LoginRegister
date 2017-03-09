package com.example.denis.loginregister;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2/Register.php"; //http://cracking-tugs.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String rB, String rB2, String mail, String password, Response.Listener<String> listener){
        //Konstruktor, Übergibt folgende Parameter an Register.php
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("gender", rB);
        params.put("search_gender", rB2);
        params.put("mail", mail);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
