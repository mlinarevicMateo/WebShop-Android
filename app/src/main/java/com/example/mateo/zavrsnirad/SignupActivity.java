package com.example.mateo.zavrsnirad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SignupActivity extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText email;
    EditText password;
    Button registerButton;
    TextView registerToLogin;

    RequestQueue requestQueue;
    private String RegisterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        RegisterUrl = getResources().getString(R.string.API_URL) + getResources().getString(R.string.REGISTER_URL);

        firstname = findViewById(R.id.registerFirstName);
        lastname = findViewById(R.id.registerLastName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        registerToLogin = findViewById(R.id.registerToLogin);

        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstname.getText().toString().isEmpty()
                        || lastname.getText().toString().isEmpty()
                        || email.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty()) {

                    firstname.setError(getString(R.string.emptyStringError));
                    lastname.setError(getString(R.string.emptyStringError));
                    email.setError(getString(R.string.emptyStringError));
                    password.setError(getString(R.string.emptyStringError));

                } else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("name", (firstname.getText().toString() + " " + lastname.getText().toString()));
                        json.put("email", email.getText());
                        json.put("password", password.getText());
                        json.put("password_confirmation", password.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.progressDialogSigningUp));
                    progressDialog.show();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RegisterUrl, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response:", response.toString());

                                    try {
                                        String message = response.getString("message");

                                        if (message.equals(getString(R.string.registerSuccessMessageFromApi))) {
                                            progressDialog.dismiss();

                                            Toast.makeText(SignupActivity.this, R.string.signupSuccessToastMessage, Toast.LENGTH_LONG).show();

                                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        Log.d("ERROR", e.getMessage());
                                        progressDialog.dismiss();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignupActivity.this, getString(R.string.badCredentialsError), Toast.LENGTH_LONG).show();
                            firstname.setError(getString(R.string.badCredentialsError));
                            lastname.setError(getString(R.string.badCredentialsError));
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

        registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
