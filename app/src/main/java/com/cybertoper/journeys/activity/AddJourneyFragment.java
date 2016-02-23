package com.cybertoper.journeys.activity;

/**
 * Created by DRX on 8/5/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.maps.AddJourneyNext;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class AddJourneyFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private MapView mMapView;
    private GoogleMap mMap;
    private Bundle mBundle;

    Button mStart, mEnd;
    ImageView mSendInfo;

    double startLatitude, startLongitude;
    double endLatitude = 0, endLongitude = 0;
    Boolean temp = true;

    MarkerOptions marker = new MarkerOptions();

    public AddJourneyFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_journey, container, false);

        mStart = (Button) rootView.findViewById(R.id.button_starting_point);
        mStart.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Toast.makeText(getActivity(), "Select Starting Place!", Toast.LENGTH_SHORT).show();
                mStart.setBackgroundColor(Color.rgb(135, 206, 250));
                mEnd.setBackgroundColor(Color.RED);
                temp = true;
            }
        });

        mEnd = (Button) rootView.findViewById(R.id.button_end_point);
        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Select Ending Place!", Toast.LENGTH_SHORT).show();
                mStart.setBackgroundColor(Color.RED);
                mEnd.setBackgroundColor(Color.rgb(135, 206, 250));
                temp = false;
            }
        });

        mSendInfo = (ImageView) rootView.findViewById(R.id.button_send_addjourney);
        mSendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLatitude == 0 || startLongitude == 0) {
                    Toast.makeText(getActivity(), "Please select Start and End Destination!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent i = new Intent(getActivity(), AddJourneyNext.class);
                    i.putExtra("StartLat", startLatitude);
                    i.putExtra("StartLong", startLongitude);
                    i.putExtra("EndLat", endLatitude);
                    i.putExtra("EndLong", endLongitude);
                    startActivity(i);
                }
            }
        });

        MapsInitializer.initialize(getActivity());

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);

        setUpMapIfNeeded(rootView);

        return rootView;
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (mMap == null) {
            mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true); //Button that shows your current location.
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    public void onMapClick(LatLng point) {

        try {

            //Shows Marker on Map.
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_point));
            mMap.addMarker(marker.position(point));
            mMap.addMarker(marker);

            //Shows Lat and Loong of selected point.
            Toast.makeText(getActivity(), point.toString(), Toast.LENGTH_SHORT).show();

            if (temp == true) {
                startLatitude = point.latitude;
                startLongitude = point.longitude;
            } else {
                endLatitude = point.latitude;
                endLongitude = point.longitude;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onMapLongClick(LatLng point) {
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            Intent intent = builder.build(getActivity());

            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown");
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            displayPlace(PlacePicker.getPlace(data, getActivity()));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void displayPlace(Place place) {

        if (place == null)
            return;

        String content = "";

        if (!TextUtils.isEmpty(place.getName())) {
            content += "Name: " + place.getName() + "\n";
        }
        if (!TextUtils.isEmpty(place.getAddress())) {
            content += "Address: " + place.getAddress() + "\n";
        }
        if (!TextUtils.isEmpty(place.getPhoneNumber())) {
            content += "Phone: " + place.getPhoneNumber();
        }
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        if (temp == true) {
            startLatitude = place.getLatLng().latitude;
            startLongitude = place.getLatLng().longitude;
        } else {
            endLatitude = place.getLatLng().latitude;
            endLongitude = place.getLatLng().longitude;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
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