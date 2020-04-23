package com.example.oopsfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//import org.apache.commons.io.IOUtils;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class LocationUser extends AppCompatActivity {

    //For Address
    Button btn_loc_drop;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textAddress123;
   //  private TextView textLatLong;

    private ResultReceiver resultReceiver;
    public static double latitude;
    public double latitude2;

    public static double longitude;
    public double longitude2;
    GeocodingLocation locationAddress;
    public static String address1;
   // public float[] distanceFinal;


    private Button btnErase;

    private TextView textView;
    private EditText editText;

    public FirebaseAuth firebaseAuth;
    /////////////////////////////////////////////////
    // public static DocumentReference documentReference;
    FirebaseFirestore fstore;
  //  public String userID;

    //private FirebaseAuth firebaseAuth;
    //FirebaseFirestore fstore;
    public  static  String addDBDis;
    String userID;
    TextView txt123;


//////////////////////////////////////////////////////////////////////////////////////////////

    public void LocToMenu(View view) {
        startActivity(new Intent(getApplicationContext(), Menu.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_user);

        txt123=findViewById(R.id.customer);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String nom = documentSnapshot.getString("Full Name");
                txt123.setText("Welcome, Customer " + nom + "!");


            }
        });
        locationAddress = new GeocodingLocation();
        /////GetAddress/////
        textView = (TextView) findViewById(R.id.tv);
        // distance1 = findViewById(R.id.dist);

       // firebaseAuth = FirebaseAuth.getInstance();
        //fstore = FirebaseFirestore.getInstance();

        //TextView distance1 = (TextView) findViewById(R.id.dist);

        // btnErase = findViewById(R.id.btnErase);
        editText = (EditText) findViewById(R.id.etAdd);

        /////GetAddress////
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                 address1 = editText.getText().toString();

                locationAddress.getAddressFromLocation(address1,
                        getApplicationContext(), new GeocoderHandler());
            }
        });


        //For Address
        btn_loc_drop = findViewById(R.id.drop);

      //  textLatLong = findViewById(R.id.textLatLong1);
        textAddress123 = findViewById(R.id.textAddress);
        resultReceiver = new LocationUser.AddressResultReceiver(new Handler());

        //Location loc = getCurrentLocation();
        //latitude = loc.getLatitude();
        //longitude = loc.getLongitude();

        // For address
        btn_loc_drop.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Toast.makeText(LocationUser.this, "Clicked!", Toast.LENGTH_SHORT).show();



                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            LocationUser.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );

                } else {
                    getCurrentLocation();

                }


            }

        });


        //  getDistance(latitude, longitude, latitude +1,longitude+1);



    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    //Log.d("latttt",locationAddress);
                    break;
                default:
                    locationAddress = null;
            }
            textView.setText(locationAddress);
        }

    }


    //For address
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location getCurrentLocation() {
          final Location location  = new Location("providerNA");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(LocationUser.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(LocationUser.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            System.out.println("Device lat and long are " + latitude + ' ' + longitude);

                         /* textLatLong.setText(
                                    String.format(
                                            "Latitude: %s\n Longitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );*/


                            location.setLatitude(latitude);
                            location.setLongitude(longitude);

                           String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);


                            Map<String, Object> user = new HashMap<>();
                            user.put("Drop food packet location latitude",latitude);
                            user.put("Drop food packet location longitude", longitude );
                            documentReference.update(user);
                            FetchAddress(location);

                            //Sending latitude and longitude to MapsActivity
                            /////
                           /* Intent lati = new Intent(getApplicationContext(),MapsActivity.class);
                            lati.putExtra("latit", latitude);
                            startActivity(lati);

                            Intent longi = new Intent(getApplicationContext(),MapsActivity.class);
                            longi.putExtra("longit", longitude);
                            startActivity(longi);*/
                            /////
                        }
                    }
                }, Looper.getMainLooper());
        return location;
    }


    private void FetchAddress(Location location) {
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(constants.RECEIVER, resultReceiver);
        intent.putExtra(constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == constants.SUCCESS_RESULT) {
                System.out.println(resultData.getString(constants.RESULT_DATA_KEY));
                addDBDis = resultData.getString(constants.RESULT_DATA_KEY);
                textAddress123.setText(resultData.getString(constants.RESULT_DATA_KEY));

            } else {
                Toast.makeText(LocationUser.this, resultData.getString(constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
