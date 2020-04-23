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
import android.os.Looper;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class DeliveryMainPage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "DeliveryMainPage";
    EditText rad;
    Button btn_submit;
    FirebaseFirestore fstore;
    String userID;
    TextView txt;
    public  double latPD;
    public int rad123;
    public double lonPD;
    public  static float[] resultsPD;
    int c;
    public static String PDname;
    public static String PDnumber,PDPA,PDDA;
    public static double dabbu;
    public static String CustomerID;



    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    public static double latitudeDB;
    public static double longitudeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main_page);


        txt=findViewById(R.id.delivery);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        rad = findViewById(R.id.radius);
        btn_submit = findViewById(R.id.btn_radius);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    DeliveryMainPage.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );

        } else {
            getCurrentLocation();

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rad.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DeliveryMainPage.this, "Please enter the delivery radius", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    String qwerty = rad.getText().toString();
                     rad123 = Integer.parseInt(qwerty);
                    System.out.println("Radius is " + rad123);
                }

                Query qq= fstore.collection("users").whereEqualTo("Order",true).orderBy("Timestamp",DESCENDING).limit(5);
                qq.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, document.getId() + " => " + document.get("Pickup Food Packet location longitude"));
                                latPD  = (double) document.get("Pickup Food Packet location latitude");
                                lonPD = (double) document.get("Pickup Food Packet location longitude");
                                resultsPD = new float[5];
                                Location.distanceBetween(latPD, lonPD, latitudeDB, longitudeDB, resultsPD);
                                System.out.println("DBlat " + latitudeDB);
                                System.out.println("DBlong " + latitudeDB);
                                System.out.println("PUlat " + latPD);
                                System.out.println("PUlat " + lonPD);

                                System.out.println("Distance delivery " + resultsPD[0]);
                                if ((resultsPD[0]/1000) <= rad123)
                                {
                                     c = 0;
                                    Log.d(TAG, document.getId() + " => " + document.get("Amount"));
                                    CustomerID= document.getId();
                                    PDname = (String) document.get("Full Name");
                                    PDnumber = (String) document.get("Mobile number");
                                    PDPA = (String) document.get("Pickup Address");
                                    PDDA = (String) document.get("Drop Address");
                                    dabbu = (double) document.get("Amount");



                                    startActivity(new Intent(getApplicationContext(),DeliveryConf.class));

                                    break;
                                }
                                 c =1;


                                //  txt.setText(document.getString("name"));
                            }
                            if(c ==1)
                            {
                                Toast.makeText(DeliveryMainPage.this, "Deliveries for specified radius not found", Toast.LENGTH_SHORT).show();
                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





            }
        });

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String nom = documentSnapshot.getString("Full Name");
                txt.setText("Welcome, Delivery Executive  "+nom);


            }
        });

        /*Query qq= fstore.collection("users").whereEqualTo("Order",true).orderBy("Timestamp",ASCENDING).limit(5);
        qq.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Log.d(TAG, document.getId() + " => " + document.get("Pickup Food Packet location longitude"));
                       latPD  = (double) document.get("Pickup Food Packet location latitude");
                       lonPD = (double) document.get("Pickup Food Packet location longitude");
                        resultsPD = new float[5];
                        Location.distanceBetween(latPD, lonPD, latitudeDB, longitudeDB, resultsPD);
                        System.out.println("Distance delivery " + resultsPD[0]);
                        if (resultsPD[0] <= rad123)
                        {
                            startActivity(new Intent(getApplicationContext(),DeliveryConf.class));
                            break;
                        }


                        //  txt.setText(document.getString("name"));
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
*/


    }

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

    private void getCurrentLocation() {
        //  final Location location  = new Location("providerNA");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(DeliveryMainPage.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(DeliveryMainPage.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitudeDB = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitudeDB = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            System.out.println("Delivery boy's lat and long are " + latitudeDB + ' ' + longitudeDB);

                        }
                    }
                }, Looper.getMainLooper());
        //return location;
    }




}

