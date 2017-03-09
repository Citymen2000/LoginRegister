package com.example.denis.loginregister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
//Registrierseite
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mit welchem XML-Layout verknüpft werden soll
        setContentView(R.layout.activity_register);

        //Variablen deklarieren = sucht in der XML nach dem Namen
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.RadioGroup2);
        final EditText etMail = (EditText) findViewById(R.id.etMail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        //Wenn Button geklickt, dann Daten an RegisterRequest übergeben und damit an Register.php
        bRegister.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                 RadioButton radioButton2 = (RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId());
                 final String rB = radioButton.getText().toString();
                 final String rB2 = radioButton2.getText().toString();
                 //final int rB = Integer.parseInt(radioButton.getText().toString());
                 //final int rB2 = Integer.parseInt(radioButton2.getText().toString());
                 final String mail = etMail.getText().toString();
                 final String password = etPassword.getText().toString();

                 //Warten auf die Antwort von Register.php
                 Response.Listener<String> responseListener = new Response.Listener<String>(){
                     @Override
                     public void onResponse(String response){
                         try {
                             JSONObject jsonResponse = new JSONObject(response);
                             boolean success = jsonResponse.getBoolean("success");

                             if(success){
                                 Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                 RegisterActivity.this.startActivity(intent);
                             } else {
                                 AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                 builder.setMessage("Register Failed")
                                         .setNegativeButton("Retry", null)
                                         .create()
                                         .show();
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 };

                 RegisterRequest registerRequest = new RegisterRequest(rB, rB2, mail, password, responseListener);
                 RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                 queue.add(registerRequest);
             }
         }
        );

    }
}
