package com.example.denis.loginregister;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class UserAreaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView etBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etMail = (EditText) findViewById(R.id.etMail);
        final EditText etGender = (EditText) findViewById(R.id.etGender);
        final EditText etZipcode = (EditText) findViewById(R.id.etZipcode);
        etBirthday = (TextView) findViewById(R.id.etBirthday);
        //final EditText etBirthday = (EditText) findViewById(R.id.etBirthday);
        final EditText etHeight = (EditText) findViewById(R.id.etHeight);
        final EditText etBodyweight = (EditText) findViewById(R.id.etBodyweight);
        final Button bSave = (Button) findViewById(R.id.bSave);

        //Übergebene Daten in Strings speichern
        Intent intent = getIntent();
        String mail = intent.getStringExtra("mail");
        String gender = intent.getStringExtra("gender");
        String zipcode = intent.getStringExtra("zipcode");
        final String birthday = intent.getStringExtra("birthday");
        String height = intent.getStringExtra("height");
        String bodyweight = intent.getStringExtra("bodyweight");

        //Felder befüllen
        etMail.setText(mail);
        etGender.setText(gender);
        if(zipcode.equals("null")){
            etZipcode.setHint("hinzufügen");
        }else{
            etZipcode.setText(zipcode);
        }
        if(birthday.equals("null")){
            etBirthday.setText("hinzufügen");
        }else{
            // Input in SQL Format
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date inputDate = null;
            try {
                inputDate = parser.parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Output in Standardformat
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            etBirthday.setText(formatter.format(inputDate));
        }
        if(height.equals("null")){
            etHeight.setHint("hinzufügen");
        }else{
            etHeight.setText(height);
        }
        if(bodyweight.equals("null")){
            etBodyweight.setHint("hinzufügen");
        }else{
            etBodyweight.setText(bodyweight);
        }

        //Was machen bei Click auf dem Button wird in der Klammer übergeben und in diesem Fall auch direkt beschrieben
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            //Also beim Click mache:
            public void onClick(View v) {

                //Volley-Warteschlange erzeugen
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);

                //URL definieren
                final String url = "http://10.0.2.2/UserArea.php";

                //StringRequest: HTTP Request, bei dem der Response als String geparsed wird
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>(){
                            //Wenn eine Antwort kommt, dann:
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Toast einToast = Toast.makeText(UserAreaActivity.this, "Gespeichert", Toast.LENGTH_SHORT);
                                        einToast.show();

                                        Intent intent = new Intent(UserAreaActivity.this, DashboardActivity.class);
                                        UserAreaActivity.this.startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                        builder.setMessage("Register Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },null
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {

                        // Input in Standardformat
                        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
                        Date inputDate = null;
                        try {
                            inputDate = fmt.parse(etBirthday.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Output in SQL Format
                        fmt = new SimpleDateFormat("yyyy-MM-dd");
                        String formatted_birthday = fmt.format(inputDate);

                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("mail", LoginActivity.MAIL);
                        params.put("gender", etGender.getText().toString());

                        if(etZipcode.getText().toString().equals("")){
                            params.put("zipcode", "");
                            System.out.println("jo");
                        } else {
                            params.put("zipcode", etZipcode.getText().toString());
                        }

                        params.put("birthday", formatted_birthday);
                        params.put("bodyweight", etBodyweight.getText().toString());
                        params.put("height", etHeight.getText().toString());

                        return params;
                    }
                };

                //String Request Objekt der Volley-Warteschlange hinzufügen
                queue.add(postRequest);

                /*
                //Warten auf die Antwort von Register.php
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
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

                //Erzeugen der verknüpften Klasse, die alles übergeben wird
                RegisterRequest registerRequest = new RegisterRequest(rB, rB2, mail, password, responseListener);
                //Volley-Element: Warteschlange für HTTP Requests erzeugen
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                //Volley-Element: HTTP Request Anfrage in die Warteschlange einreihen
                queue.add(registerRequest);
                */
            }
        });
    }

    //OnClick Methode
    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    //Datum anzeigen
    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        //etBirthday.setText(dateFormat.format(calendar.getTime()));

        //Datum konvertieren zur Anzeige
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = format.format(calendar.getTime());
        etBirthday.setText(strDate);

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
        }
    }

}
