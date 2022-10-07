package com.softhub.shivmudra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softhub.shivmudra.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextInputLayout mobile;
    private TextInputLayout password;
    private EditText m;
    private EditText p;
    private TextView signup;
    private TextView forgot_password;
    private CardView login;
    ProgressDialog progressDialog;
    private Session session;
    private String fName, eMail, regi_mobile, user_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mobile = (TextInputLayout) findViewById(R.id.mobile);
        mobile.setHint("Mobile Number");
        password = (TextInputLayout) findViewById(R.id.password);
        password.setHint("Password");
        signup = (TextView) findViewById(R.id.signup);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        login = (CardView) findViewById(R.id.login);

        m = (EditText) findViewById(R.id.m);
        p = (EditText) findViewById(R.id.p);


        session = new Session(getApplicationContext());
        progressDialog = new ProgressDialog(Login.this);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    private void loginUser() {

        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        String uri = getResources().getString(R.string.base_url)+"login.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject JO = new JSONObject(s);
                            String code = JO.getString("data_code");
                            if(code.equals("200")){
                                JSONArray JA = JO.getJSONArray("item_list");
                                for(int i=0; i<JA.length(); i++){
                                    JSONObject JO1 = JA.getJSONObject(i);
                                    fName = JO1.getString("first_name");
                                    eMail = JO1.getString("email");
                                    regi_mobile = JO1.getString("mobile");
                                    user_type = JO1.getString("user_type");
                                }
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                                openApp();
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
                        Toast.makeText(getApplicationContext(), "Registration.Error!",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("mobile", m.getText().toString());
                params.put("password", p.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void openApp() {

        session.setLoggedin(true);
        session.setMobile(regi_mobile);
        session.setUserName(fName);
        session.setEmailId(eMail);
        session.setUserType(user_type);
        Intent i = new Intent(getApplicationContext(), Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
}
