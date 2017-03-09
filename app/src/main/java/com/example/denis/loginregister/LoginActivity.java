package com.example.denis.loginregister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//Login Seite
public class LoginActivity extends AppCompatActivity {

    public static String MAIL;
    public static String PASSWORD;

    //----Wird ausgeführt wenn erzeugt wird
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //----Methoden erben
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //----Objekte für die View Elemente erzeugen und mit dem Layout verknüpfen
        final EditText etMail = (EditText) findViewById(R.id.etMail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        //----Wenn Button RegisterHere geklickt, dann öffne Register Seite
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        //----Wenn Button Login geklickt
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----Aus den Formularfeldern die Werte auslesen
                LoginActivity.MAIL = etMail.getText().toString();
                LoginActivity.PASSWORD = etPassword.getText().toString();

                //-----Warten bis folgendes eine Antwort liefert
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //----Werte aus Datenbank lesen
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            //----Wenn Datenbank success zurückgeliefert hat, dann rufe Dashboard auf und übergebe die Login Daten
                            if (success) {
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                //intent.putExtra("mail", LoginActivity.MAIL);
                                //intent.putExtra("password", LoginActivity.PASSWORD);
                                LoginActivity.this.startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        }
        );

    }
}