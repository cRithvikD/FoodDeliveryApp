package com.example.oopsfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    Button btn_login;
    //btn_loc;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;


   /* //For Address
    private static final int  REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView  textLatLong,textAddress;
    private ResultReceiver resultReceiver;
    public static double latitude;
    public static double longitude;
*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("LOG IN!");



        txtEmail= findViewById(R.id.txt_email1);
        txtPassword= findViewById(R.id.txt_password1);
        btn_login = findViewById(R.id.buttonlogin);
        //  btn_loc = findViewById(R.id.getLoc);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        /*//For Address
        textLatLong = findViewById(R.id.textLatLong);
        textAddress = findViewById(R.id.textAddress);
        resultReceiver = new AddressResultReceiver(new Handler());*/




       /* // For address
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(
                            LogIn.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );

                }
                else
                {
                    getCurrentLocation();
                }

            }
        });*/


        // Functionality for login page
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LogIn.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LogIn.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //////////
                                    String userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                                    DocumentReference documentReference = fstore.collection("users").document(userID);
                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                        @Override

                                        public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                                            if (task.isSuccessful()) {

                                                DocumentSnapshot doc = task.getResult();
                                                Object lol = doc.get("User_Type");
                                                Boolean b = doc.getBoolean("Order");
                                                if (lol.equals("Customer"))
                                                {
                                                    if(!b) {
                                                        startActivity(new Intent(getApplicationContext(), LocationUser.class));
                                                    }
                                                    if(b) {
                                                        startActivity(new Intent(getApplicationContext(), Price.class));
                                                    }
                                                }

                                                else{
                                                    startActivity(new Intent(getApplicationContext(),DeliveryMainPage.class));

                                                }


                                            }

                                        }

                                    })

                                            .addOnFailureListener(new OnFailureListener() {

                                                @Override

                                                public void onFailure(@NonNull Exception e) {

                                                }

                                            });




                                } else {

                                    Toast.makeText(LogIn.this, "Login failed or user not found", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });
    }

    //Redirects to sign up page on clicking register button
    public void btn_SignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }



/*
    //For address
    @Override
    public void  onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
            else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.getFusedLocationProviderClient(LogIn.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(LogIn.this).removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textLatLong.setText(
                                    String.format(
                                            "Latitude: %s\n Longitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            FetchAddress(location);

                            //Sending latitude and longitude to MapsActivity
                            /////
                            *//*Intent lati = new Intent(getApplicationContext(),MapsActivity.class);
                            lati.putExtra("latit", latitude);
                            startActivity(lati);

                            Intent longi = new Intent(getApplicationContext(),MapsActivity.class);
                            longi.putExtra("longit", longitude);
                            startActivity(longi);*//*
                            /////
                        }
                    }
                }, Looper.getMainLooper());

    }



    private void FetchAddress(Location location){
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(constants.RECEIVER,resultReceiver);
        intent.putExtra(constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == constants.SUCCESS_RESULT){
                textAddress.setText(resultData.getString(constants.RESULT_DATA_KEY));
            }
            else{
                Toast.makeText(LogIn.this, resultData.getString(constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }*/


}


