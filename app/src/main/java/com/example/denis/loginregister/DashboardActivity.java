package com.example.denis.loginregister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final TextView tvProfile = (TextView) findViewById(R.id.tvProfile);

        //----Speichere die übergebenen Login Daten
        //Intent intent = getIntent();
        //final String mail = intent.getStringExtra("mail");
        //final String password = intent.getStringExtra("password");

        //----Wenn Mein Profil angeklickt wurde
        tvProfile.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {

                         Response.Listener<String> responseListener = new Response.Listener<String>() {
                             @Override
                             public void onResponse(String response) {
                                 try {
                                     //----JSON Datei aus Datenbank lesen
                                     JSONObject jsonResponse = new JSONObject(response);
                                     boolean success = jsonResponse.getBoolean("success");

                                     if (success) {
                                         //----Einzelne Werte aus JSON Datei lesen
                                         String gender = jsonResponse.getString("gender");
                                         String search_gender = jsonResponse.getString("search_gender");
                                         String zipcode = jsonResponse.getString("zipcode");
                                         String birthday = jsonResponse.getString("birthday");
                                         String height = jsonResponse.getString("height");
                                         String bodyweight = jsonResponse.getString("bodyweight");

                                         //----User Area aufrufen und Daten übergeben
                                         Intent intent = new Intent(DashboardActivity.this, UserAreaActivity.class);
                                         intent.putExtra("mail", LoginActivity.MAIL);
                                         intent.putExtra("password", LoginActivity.PASSWORD);
                                         intent.putExtra("gender", gender);
                                         intent.putExtra("search_gender", search_gender);
                                         intent.putExtra("zipcode", zipcode);
                                         intent.putExtra("birthday", birthday);
                                         intent.putExtra("height", height);
                                         intent.putExtra("bodyweight", bodyweight);

                                         DashboardActivity.this.startActivity(intent);

                                     } else {
                                         AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                                         builder.setMessage("Login Failed")
                                                 .setNegativeButton("Retry", null)
                                                 .create()
                                                 .show();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }


                             }
                         };

                         LoginRequest loginRequest = new LoginRequest(LoginActivity.MAIL, LoginActivity.PASSWORD, responseListener);
                         RequestQueue queue = Volley.newRequestQueue(DashboardActivity.this);
                         queue.add(loginRequest);
                     }
                 }
        );
    }
}
