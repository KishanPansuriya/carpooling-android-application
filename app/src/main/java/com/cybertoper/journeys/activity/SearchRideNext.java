package com.cybertoper.journeys.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.authentication.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by DRX on 10/23/2015.
 */
public class SearchRideNext extends ActionBarActivity {

    Toolbar mTool;
    String email = null;
    String Id = null;
    TextView t1;
    ProgressDialog diag;
    String URL1 = "http://www.cybertoper.com/getUserdetail.php";
    long totalSize = 0;
    String mServerData;
    String myMail = null;
    Button b1;
    String msg = "Checking for Rides...";


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.search_ride_next);

        mTool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ride Details");

        SessionManager sm = new SessionManager(this);
        myMail = sm.pref.getString("email", null);

        t1 = (TextView) findViewById(R.id.search_ride_next_email);
        b1 = (Button) findViewById(R.id.search_ride_next_button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL1 = "http://www.cybertoper.com/applyforride.php";
                msg = "Applying!";
                new CheckRides().execute();
            }
        });

        Intent ii = getIntent();
        Bundle b1 = ii.getExtras();

        if (b1 != null) {
            email = (String) b1.get("Rideby");
            Id = (String)b1.get("RideId");
            //t1.setText(email);
            new CheckRides().execute();
        }


    }

    private class CheckRides extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(SearchRideNext.this);
            diag.setMessage(msg);
            diag.setIndeterminate(false);
            diag.setCancelable(true);
            diag.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return Checking();
        }

        @SuppressWarnings("deprecation")
        private String Checking() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL1);

            try {
                MultipartEntity entity = new MultipartEntity();

                // Extra parameters passed to server like email
                entity.addPart("email", new StringBody(email));
                entity.addPart("myEmail", new StringBody(myMail));
                entity.addPart("jid",new StringBody(Id));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            // showing the server response in an alert dialog
            mServerData = result;
            diag.dismiss();
            t1.setText(mServerData);
            //mServerAnswer.setText(result);
            super.onPostExecute(result);
        }
    }
}
