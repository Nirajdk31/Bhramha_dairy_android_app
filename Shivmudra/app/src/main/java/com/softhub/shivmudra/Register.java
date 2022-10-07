package com.softhub.shivmudra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText regi_name;
    private EditText regi_email;
    private EditText regi_mobile;
    private EditText regi_password;
    private EditText reg_otp;

    private CardView submit;
    private Button verifyOtp;
    private Button resendOtp;

    private LinearLayout layoutOtp;
    ProgressDialog progressDialog;
    private String s_otp;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(Register.this);
        session = new Session(getApplicationContext());

        regi_name = (EditText) findViewById(R.id.regi_name);
        regi_email = (EditText) findViewById(R.id.regi_email);
        regi_mobile = (EditText) findViewById(R.id.regi_mobile);
        regi_password = (EditText) findViewById(R.id.regi_password);
        reg_otp = (EditText) findViewById(R.id.reg_otp);
        submit = (CardView) findViewById(R.id.submit);
        verifyOtp = (Button) findViewById(R.id.verifyOtp);
        resendOtp = (Button) findViewById(R.id.resendOtp);
        layoutOtp = (LinearLayout) findViewById(R.id.layoutOtp);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_otp = reg_otp.getText().toString();
                if(s_otp.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Otp", Toast.LENGTH_SHORT).show();
                }else{
                    verigyOtp();
                }
            }
        });
    }

    private void verigyOtp() {

        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        String uri = getResources().getString(R.string.base_url)+"regi_verify_otp.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject JO = new JSONObject(s);
                            String result = JO.getString("data_code");
                            if(result.equals("200")){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Registered successfully!", Toast.LENGTH_SHORT).show();
                                openApp();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), JO.getString("res"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //  Toast.makeText(getApplicationContext(), "Registration.Error!",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("otp", s_otp);
                params.put("mobile", regi_mobile.getText().toString());
                params.put("status", "Y");

                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void openApp() {

        session.setLoggedin(true);
        session.setMobile(regi_mobile.getText().toString());
        session.setUserName(regi_name.getText().toString());
        session.setEmailId(regi_email.getText().toString());
        Intent i = new Intent(getApplicationContext(), Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private void registerUser() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String uri = getResources().getString(R.string.base_url)+"registration.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject JO = new JSONObject(s);
                            String result = JO.getString("data_code");
                            if(result.equals("200")){
                                submit.setVisibility(View.INVISIBLE);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), JO.getString("Message"), Toast.LENGTH_LONG).show();
                                layoutOtp.setVisibility(View.VISIBLE);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), JO.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // Toast.makeText(getApplicationContext(), "Registration.Error!",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("first_name", regi_name.getText().toString());
                params.put("mobile", regi_mobile.getText().toString());
                params.put("email", regi_email.getText().toString());
                params.put("password", regi_password.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

    }

}
