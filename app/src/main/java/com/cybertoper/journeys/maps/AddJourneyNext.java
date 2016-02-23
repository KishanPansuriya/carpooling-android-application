package com.cybertoper.journeys.maps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.activity.MainActivity;
import com.cybertoper.journeys.authentication.JSONParser;
import com.cybertoper.journeys.authentication.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by DRX on 9/10/2015.
 */
public class AddJourneyNext extends ActionBarActivity {

    Toolbar mTool1;
    TextView t1, t2, t3,t4;
    Double sLat, sLong, eLat, eLong;
    int totalDistance = 0;
    String addressStr = "";
    String text1 = "";
    String text2 = "";
    int success;

    EditText mCharges, mVacantseats, mVehicleNumber;
    Button sendData,setDate;

    String mEmail = "";
    private DatePicker datePicker;
    String addjourneydate1 = "";
    private Calendar calendar;
    private int year, month, day;

    private static String link = "http://www.cybertoper.com/addnewjourney1.php";
    JSONParser jsonparser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    public ProgressDialog diag;

    public Toolbar mTool;

    //-------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------------------------------------------------------------//
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.fragment_addjourney_next);

        SessionManager sm = new SessionManager(getApplicationContext());

        mTool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Journey Details");

        mCharges = (EditText) findViewById(R.id.add_journey_charges);
        mVacantseats = (EditText) findViewById(R.id.add_journey_vacant_seats);
        mVehicleNumber = (EditText) findViewById(R.id.add_journey_vehicle_number);
        sendData = (Button) findViewById(R.id.add_journey_send_final_data);
        setDate=(Button)findViewById(R.id.add_journey_set_date_btn);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        mEmail = sm.pref.getString("email",null);

        t1 = (TextView) findViewById(R.id.add_next_text1);
        t2 = (TextView) findViewById(R.id.add_next_text2);
        t3 = (TextView) findViewById(R.id.add_journey_set_date_view);
        t4 = (TextView)findViewById(R.id.add_journey_total_distance);

        Intent ii = getIntent();
        Bundle b1 = ii.getExtras();

        if (b1 != null) {
            sLat = (Double) b1.get("StartLat");
            sLong = (Double) b1.get("StartLong");
            eLat = (Double) b1.get("EndLat");
            eLong = (Double) b1.get("EndLong");
            myDistance(sLat,sLong,eLat,eLong);
            t4.setText(totalDistance+"  Km");
        }

        text1 = getAddress(sLat, sLong);
        text2 = getAddress(eLat, eLong);

        t1.setText(text1);
        t2.setText(text2);

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                dismissDialog(999);}
                catch (Exception e){}
                new AddNewJourney().execute();
            }
        });
    }
    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            //return new DatePickerDialog(this, myDateListener, year, month, day);
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year, arg2 = month, arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        addjourneydate1 = day + "/" + month + "/" + year;
        t3.setText(addjourneydate1);
        //new StringBuilder().append(day).append("/").append(month).append("/").append(year)
    }
    //------------------------------------------------------------------------------------------------------------------//
    //------------------------------------------------------------------------------------------------------------------//
    public String getAddress(double lat, double lng) {

        Geocoder myLocation = new Geocoder(this, Locale.getDefault());
        String _homeAddress = "";
        try {
            Address address = null;
            List<Address> addresses = myLocation.getFromLocation(lat, lng, 1);
            for (int index = 0; index < addresses.size(); ++index) {
                address = addresses.get(index);
                _homeAddress = address.getLocality();//"Name: " + ... + "\n";
                //_homeAddress += "Sub-Admin Ares: " + address.getSubAdminArea() + "\n";
                //_homeAddress += "Admin Area: " + address.getAdminArea() + "\n";
                //_homeAddress += "Country: " + address.getCountryName() + "\n";
                //_homeAddress += "Country Code: " + address.getCountryCode() + "\n";
                //_homeAddress += "Latitude: " + address.getLatitude() + "\n";
                //_homeAddress += "Longitude: " + address.getLongitude() + "\n\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _homeAddress;
    }

    public  void myDistance(double a,double b,double c,double d) {
        Location locationA = new Location("point A");

        locationA.setLatitude(a);
        locationA.setLongitude(b);

        Location locationB = new Location("point B");

        locationB.setLatitude(c);
        locationB.setLongitude(d);

        double distance = locationA.distanceTo(locationB);
        distance = distance/1000;
        totalDistance = (int)distance;
        if(totalDistance < 20){
            sendData.setEnabled(false);
            Toast.makeText(this,"Distance Must be > 20 KM",Toast.LENGTH_LONG).show();
        }
    }
    //--------------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------//
    class AddNewJourney extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(AddJourneyNext.this);
            diag.setMessage("Sending Data...");
            diag.setIndeterminate(false);
            diag.setCancelable(true);
            diag.show();
        }

        protected String doInBackground(String... args) {

            String email = mEmail;
            String startPlace = text1;
            String endPlace = text2;
            //String startLatitude = sLat.toString();
            //String startLongitude = sLong.toString();
            //String endLatitude = eLat.toString();
            //String endLongitude = eLong.toString();
            String newJourneydate = addjourneydate1;
            String distance1 = Integer.toString(totalDistance);
            String vehicleNumber = mVehicleNumber.getText().toString();
            String charges = mCharges.getText().toString();
            String vacancy = mVacantseats.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            /*
            params.add(new BasicNameValuePair("startLatitude", startLatitude));
            params.add(new BasicNameValuePair("startLongitude", startLongitude));
            params.add(new BasicNameValuePair("endLatitude", endLatitude));
            params.add(new BasicNameValuePair("endLongitude", endLongitude));
            */
            params.add(new BasicNameValuePair("startPlace", startPlace));
            params.add(new BasicNameValuePair("endPlace", endPlace));
            params.add(new BasicNameValuePair("newJourneydate", newJourneydate));
            params.add(new BasicNameValuePair("distance", distance1));
            params.add(new BasicNameValuePair("vehicleNumber", vehicleNumber));
            params.add(new BasicNameValuePair("charges", charges));
            params.add(new BasicNameValuePair("vacantSeats", vacancy));

            JSONObject json = jsonparser.makeHttpRequest(link, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i5 = new Intent(AddJourneyNext.this, MainActivity.class);
                    startActivity(i5);
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
            if (success == 0) {
                Toast.makeText(AddJourneyNext.this, "Failed to Add new Journey! Try Again...", Toast.LENGTH_SHORT).show();
            }
            diag.dismiss();
        }

    }
    //-------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------------------------------------------------------------//

}
