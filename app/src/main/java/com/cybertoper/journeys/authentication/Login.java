package com.cybertoper.journeys.authentication;

/**
 * Created by DRX on 8/6/2015.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.activity.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends ActionBarActivity {

    private ProgressDialog diag;
    public ProgressDialog diag2;

    private static String link2 = "http://www.cybertoper.com/forgotpass.php";

    EditText mEmail;
    EditText mPassword;
    TextView mforgotButton;

    JSONParser jsonparser = new JSONParser();

    SessionManager session;

    private static String link = "http://www.cybertoper.com/verify.php";

    private static final String TAG_SUCCESS = "success";
    int success;
    private Toolbar mTool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        session = new SessionManager(getApplicationContext());

        mTool = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");

        mEmail = (EditText) findViewById(R.id.log_mail);
        mPassword = (EditText) findViewById(R.id.log_pass);

        Button next = (Button) findViewById(R.id.btnLogin);
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(email.equals("")||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("enter a valid email address");
                }
                else if(password.equals("")){
                    mPassword.setError("enter valid password");
                } else {
                    new AttemptLogin().execute();
                }
            }
        });

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });

        mforgotButton = (TextView) findViewById(R.id.link_to_forget);
        mforgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //forgot password code here
                String email = mEmail.getText().toString();
                if (email.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("enter a valid email address");
                }
                else{
                    new sendmail().execute();
                }
            }
        });
    }


    class AttemptLogin extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(Login.this);
            diag.setMessage("Attempting Login...");
            diag.setIndeterminate(false);
            diag.setCancelable(true);
            diag.show();
        }

        protected String doInBackground(String... args) {

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email",email));
            params.add(new BasicNameValuePair("password", password));


            JSONObject json = jsonparser.makeHttpRequest(link, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    //Storing user data in preferences
                    session.createLoginSession(email, password);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Log.d("failed to create user", json.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(success == 0) {
                Toast.makeText(Login.this, "Wrong Credentials!", Toast.LENGTH_SHORT).show();
            }
            diag.dismiss();
        }

    }

    class sendmail extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            diag2 = new ProgressDialog(Login.this);
            diag2.setMessage("Sending New Password...");
            diag2.setIndeterminate(false);
            diag2.setCancelable(true);
            diag2.show();
        }

        protected String doInBackground(String... args) {

            String email = mEmail.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));

            JSONObject json2 = jsonparser.makeHttpRequest(link2, "POST", params);

            try {
                success = json2.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(success == 1){
                Toast.makeText(Login.this,"Password sent to Email address!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Login.this,"Error occured!",Toast.LENGTH_SHORT).show();
            }
            diag2.dismiss();
        }

    }

}

