package com.example.mateo.zavrsnirad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    CheckBox rememberMe;
    Button loginButton;
    TextView loginToRegister;

    RequestQueue requestQueue;
    private String LoginUrl;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginUrl = getResources().getString(R.string.API_URL) + getResources().getString(R.string.LOGIN_URL);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        rememberMe = findViewById(R.id.loginRememberMe);
        loginButton = findViewById(R.id.loginButton);
        loginToRegister = findViewById(R.id.loginToRegister);

        requestQueue = Volley.newRequestQueue(this);

        final String USER_LOGGED_IN_SP = getResources().getString(R.string.USER_LOGGED_IN_SP);
        final String USER_ACCESS_TOKEN = getResources().getString(R.string.USER_ACCESS_TOKEN);
        final String USER_ACCESS_TOKEN_TYPE = getResources().getString(R.string.USER_ACCESS_TOKEN_TYPE);
        final String USER_EMAIL = getResources().getString(R.string.USER_EMAIL);
        final String USER_NAME = getResources().getString(R.string.USER_NAME);
        final String USER_IMAGE = getResources().getString(R.string.USER_IMAGE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
                    email.setError(getString(R.string.emptyStringError));
                    password.setError(getString(R.string.emptyStringError));
                } else if (email.getText().toString().isEmpty()) {
                    email.setError(getString(R.string.emptyStringError));
                } else if (password.getText().toString().isEmpty()) {
                    password.setError(getString(R.string.emptyStringError));
                } else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("email", email.getText());
                        json.put("password", password.getText());
                        json.put("remember_me", rememberMe.isChecked());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.progressDialogLoggingIn));
                    progressDialog.show();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LoginUrl, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response:", response.toString());

                                    try {
                                        String token = response.getString("access_token");
                                        String token_type = response.getString("token_type");
                                        String user_email = response.getString("email");
                                        String user_name = response.getString("name");
                                        String user_image = response.getString("image");

                                        SharedPreferences sharedPrefs = getSharedPreferences(USER_LOGGED_IN_SP, MODE_PRIVATE);
                                        SharedPreferences.Editor ed;
                                        ed = sharedPrefs.edit();
                                        ed.putBoolean(USER_LOGGED_IN_SP, true);
                                        ed.putString(USER_ACCESS_TOKEN, token);
                                        ed.putString(USER_ACCESS_TOKEN_TYPE, token_type);
                                        ed.putString(USER_EMAIL, user_email);
                                        ed.putString(USER_NAME, user_name);
                                        ed.putString(USER_IMAGE, user_image);

                                        ed.commit();

                                        progressDialog.dismiss();

                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    } catch (JSONException e) {
                                        Log.d("ERROR", e.getMessage());
                                        progressDialog.dismiss();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Your credentials are bad!", Toast.LENGTH_LONG).show();
                            email.setError(getString(R.string.badCredentialsError));
                            password.setError(getString(R.string.badCredentialsError));
                            progressDialog.dismiss();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json; charset=utf-8");
                            return params;
                        }
                    };

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
