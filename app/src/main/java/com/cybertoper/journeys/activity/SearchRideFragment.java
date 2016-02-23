package com.cybertoper.journeys.activity;

/**
 * Created by DRX on 8/5/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.maps.PlaceJSONParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class SearchRideFragment extends Fragment {

    String RIDE_CHECK_URL = "http://www.cybertoper.com/search_ride.php";
    String DateoFRide;
    public static String email = null;
    public String Id = null;
    String mSplitter[];

    UpdateProfileFragment u = new UpdateProfileFragment();

    AutoCompleteTextView atvPlaces1, atvPlaces2;
    PlacesTask placesTask;
    ParserTask parserTask;

    TextView mChoice, mRideDate;

    Button mButton, mButton2, mselectRideDate;

    String s1 = "", s2 = "";
    String part1 = "", part2 = "";
    String[] parts1, parts2;

    private ProgressDialog diag;
    long totalSize = 0;

    ListView mSearchresult;
    String[] values = null; //Array of individual email
    String mServerData = null; // response from server a mail list

    public SearchRideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search_ride, container, false);

        mChoice = (TextView) rootView.findViewById(R.id.search_final_selection);

        values = new String[]{"Ride 1", "Ride 2", "Ride 3", "Ride 4", "Ride 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);

        //Search Result list
        mSearchresult = (ListView) rootView.findViewById(R.id.search_result);
        mSearchresult.setAdapter(adapter);

        mSearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) mSearchresult.getItemAtPosition(position);
                mSplitter = itemValue.split(",");
                email = mSplitter[0];
                Id = mSplitter[1];
                mChoice.setText(itemValue);
            }
        });


        mButton = (Button) rootView.findViewById(R.id.search_ride_search_button);
        mButton2 = (Button) rootView.findViewById(R.id.search_ride_apply_button);

        mselectRideDate = (Button) rootView.findViewById(R.id.search_ride_pick_date);

        mRideDate = (TextView) rootView.findViewById(R.id.search_ride_date);

        atvPlaces1 = (AutoCompleteTextView) rootView.findViewById(R.id.atv_places1);
        atvPlaces1.setThreshold(1);

        atvPlaces2 = (AutoCompleteTextView) rootView.findViewById(R.id.atv_places2);
        atvPlaces2.setThreshold(1);

        atvPlaces1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        atvPlaces2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s1 = atvPlaces1.getText().toString();
                s2 = atvPlaces2.getText().toString();

                parts1 = s1.split(",");
                parts2 = s2.split(",");

                part1 = parts1[0];
                part2 = parts2[0];

                new CheckRides().execute();
            }
        });


        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchRideNext.class);
                i.putExtra("Rideby", email);
                i.putExtra("RideId",Id);
                startActivity(i);
            }
        });

        mselectRideDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                mRideDate.setText(u.getGlobalDate());
                DateoFRide = mRideDate.getText().toString();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            //Android key = AIzaSyDlfDbMF1m9WRpxs57d2a4gih5wJZ8wveE.(in use)
            //New Android key = AIzaSyCog8coqcoGg3v-Lr_PqpowIWBDc4yaCoA.(Not in use)
            //Browser key = AIzaSyCz6hroFC5aGpSq8vUz5ZdNv1lvM90xMHA.
            //Server key = AIzaSyB78Ta4as71E_HZWxEv2SC9Bac-9tFtFmw.

            String key = "key=AIzaSyCz6hroFC5aGpSq8vUz5ZdNv1lvM90xMHA";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces1.setAdapter(adapter);
            atvPlaces2.setAdapter(adapter);
        }
    }

    private class CheckRides extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(getActivity());
            diag.setMessage("Checking for Rides...");
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
            HttpPost httppost = new HttpPost(RIDE_CHECK_URL);

            try {
                MultipartEntity entity = new MultipartEntity();

                // Extra parameters passed to server like email
                entity.addPart("loc1", new StringBody(part1));
                entity.addPart("loc2", new StringBody(part2));
                entity.addPart("date",new StringBody(DateoFRide));
                //entity.addPart("date", new StringBody(addjourneydate1));

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
            values = ExtractData(mServerData);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
            mSearchresult.setAdapter(adapter);
            diag.dismiss();
            //mServerAnswer.setText(result);
            super.onPostExecute(result);
        }
    }

    public static String[] ExtractData(String s) {
        s = s.replace("\"", "");
        String[] extracted = s.split("&");
        return extracted;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}