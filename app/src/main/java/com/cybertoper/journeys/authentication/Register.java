package com.cybertoper.journeys.authentication;

/**
 * Created by DRX on 8/6/2015.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.cybertoper.journeys.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Register extends ActionBarActivity{
    private Toolbar mTool;
    private ProgressDialog diag;

    JSONParser jsonparser = new JSONParser();
    private static String link = "http://www.cybertoper.com/login.php";
    private  static final String TAG_SUCCESS = "success";

    private Button regButton;
    private EditText username;
    private EditText usermail;
    private EditText pass1;
    private EditText pass2;
    private EditText usercontact;
    public  String gender="male";
    private RadioButton b1,b2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mTool = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        username = (EditText)findViewById(R.id.reg_fullname);
        usermail = (EditText)findViewById(R.id.reg_email);
        pass1 = (EditText)findViewById(R.id.reg_password);
        pass2 = (EditText)findViewById(R.id.reg_con_password);
        usercontact = (EditText)findViewById(R.id.reg_phone);
        b1 = (RadioButton)findViewById(R.id.reg_male);
        b2 = (RadioButton)findViewById(R.id.reg_female);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
            }
        });

        regButton = (Button)findViewById(R.id.btnRegister);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = username.getText().toString();
                final String email = usermail.getText().toString();
                final String password = pass1.getText().toString();
                final String verify = pass2.getText().toString();
                final String contact = usercontact.getText().toString();

                if(name.equals("")){
                    username.setError("enter valid username");
                }
                else if(email.equals("")||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    usermail.setError("enter valid mail address");
                }
                else if(password.equals("")||password.length()<6){
                    pass1.setError("password must have 6 character");
                }
                else if(!password.equals(verify)){
                    pass2.setError("password must match");
                }
                else if(contact.length()!=10){
                    usercontact.setError("enter valid contact");
                }
                else {
                    new CreateUser().execute();
                }

            }
        });

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }


    class CreateUser extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(Register.this);
            diag.setMessage("Registering New User..");
            diag.setIndeterminate(false);
            diag.setCancelable(true);
            diag.show();
        }

        protected String doInBackground(String... args) {
            String name = username.getText().toString();
            String email = usermail.getText().toString();
            String password = pass1.getText().toString();
            String contact = usercontact.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",name));
            params.add(new BasicNameValuePair("email",email));
            params.add(new BasicNameValuePair("pass",password));
            params.add(new BasicNameValuePair("contact",contact));
            params.add(new BasicNameValuePair("gender",gender));

            JSONObject json = jsonparser.makeHttpRequest(link, "POST", params);

            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created a user
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                }
                else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            diag.dismiss();
        }

    }

}
