package com.cybertoper.journeys.activity;

/**
 * Created by DRX on 8/5/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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


public class HomeFragment extends Fragment {

    String RIDER_CHECK_URL = "http://www.cybertoper.com/getRider.php";
    ListView mSearchresult;
    ProgressDialog diag;
    String mServerData;
    String[] values = null;
    long totalSize = 0;
    String myMail = null;

    Button b1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        b1 = (Button)rootView.findViewById(R.id.home_refresh_button);

        SessionManager sm = new SessionManager(getActivity());
        myMail = sm.pref.getString("email", null);


        values = new String[]{"Ride 1", "Ride 2", "Ride 3", "Ride 4", "Ride 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);

        //Search Result list
        mSearchresult = (ListView) rootView.findViewById(R.id.home_search_result);
        mSearchresult.setAdapter(adapter);

        mSearchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) mSearchresult.getItemAtPosition(position);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckRiders().execute();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


    private class CheckRiders extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            diag = new ProgressDialog(getActivity());
            diag.setMessage("Retrieving List...");
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
            HttpPost httppost = new HttpPost(RIDER_CHECK_URL);

            try {
                MultipartEntity entity = new MultipartEntity();

                // Extra parameters passed to server like email
                entity.addPart("email", new StringBody(myMail));
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