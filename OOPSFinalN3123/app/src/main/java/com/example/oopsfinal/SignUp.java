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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    EditText txtEmail, txtPassword, txtConfirmPassword, fullName, phoneNumber;
    RadioGroup foodieOrDeli;
    Button btn_register;
    // RadioButton rdbtn1, rdbtn2;
    RadioButton selectedRadioButton;
    RadioButton selectedRadioButtonCOD;

    private ResultReceiver resultReceiver;
    public  static  String addDBDisSU;


    private FirebaseAuth firebaseAuth;
    private Button btnLocSignUp;
    public static double latitudeSignUp;
    public static double longitudeSignUp;


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    /////////////////////////////////////////////////
    // public static DocumentReference documentReference;
    FirebaseFirestore fstore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up!");

        fullName = (EditText) findViewById(R.id.name);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        phoneNumber = (EditText) findViewById(R.id.mobile);
        txtPassword = findViewById(R.id.txt_password);
        txtConfirmPassword = findViewById(R.id.txt_confirm_password);
        btn_register = findViewById(R.id.buttonRegister);

        resultReceiver = new SignUp.AddressResultReceiver(new Handler());



        btnLocSignUp = findViewById(R.id.buttonlocSignIn);
        //rdbtn2 = findViewById(R.id.radioButton2);
        // selectedRadioButton = findViewById(R.id.selectedID);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        ////Loc
        btnLocSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            SignUp.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );

                } else {
                    getCurrentLocation();
                    Toast.makeText(SignUp.this, "Location Obtained!", Toast.LENGTH_SHORT).show();

                }


            }

        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                final String name = fullName.getText().toString().trim();
                final String number = phoneNumber.getText().toString().trim();
                selectedRadioButton = findViewById(R.id.foodieRB);
                foodieOrDeli = findViewById(R.id.foodieOrNot);
                final int selectedCOD = foodieOrDeli.getCheckedRadioButtonId();
                final boolean selectedID = selectedRadioButton.isChecked();
                selectedRadioButtonCOD = findViewById(selectedCOD);


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignUp.this, "Please Enter your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(SignUp.this, "Please Enter your Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedCOD == -1) {
                    Toast.makeText(SignUp.this, "Please choose whether customer or delivery executive", Toast.LENGTH_SHORT).show();
                }

                if (selectedID == true) {
                    Toast.makeText(SignUp.this, "Subscribed to monthly plan!", Toast.LENGTH_SHORT).show();
                }

                if (password.equals(confirmPassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(getApplicationContext(), LogIn.class));
                                        Toast.makeText(SignUp.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                                        DocumentReference documentReference = fstore.collection("users").document(userID);
                                        //.document("userID");

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Full Name", name);
                                        user.put("Mobile number", number);
                                        user.put("Email", email);
                                        user.put("Subscription", selectedID);
                                        //.getText().toString().trim());
                                        user.put("User_Type", selectedRadioButtonCOD.getText().toString().trim());
                                        user.put("Drop food packet location latitude", latitudeSignUp);
                                        user.put("Drop food packet location longitude", longitudeSignUp);
                                        System.out.println("     " + addDBDisSU + ' ' + longitudeSignUp);

                                        user.put("Drop Address",addDBDisSU);
                                        user.put("Order", false);

                                        // user.put("Delivery Executive?",rdbtn2.booleanValue());
                                        // user.put("Customer?",boolean(rdbtn1));
                                        //  user.put("", number);
                                        documentReference.set(user);


                                    } else {
                                        Toast.makeText(SignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });

                }


            }
        });

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

    private Location getCurrentLocation() {
        final Location location = new Location("providerNA");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(SignUp.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SignUp.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitudeSignUp = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitudeSignUp = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            System.out.println("Device lat and long are " + latitudeSignUp + ' ' + longitudeSignUp);

                         /* textLatLong.setText(
                                    String.format(
                                            "Latitude: %s\n Longitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );*/


                            location.setLatitude(latitudeSignUp);
                            location.setLongitude(longitudeSignUp);
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
                addDBDisSU = resultData.getString(constants.RESULT_DATA_KEY);
                System.out.println("Address" + addDBDisSU);
               // textAddress123.setText(resultData.getString(constants.RESULT_DATA_KEY));

            } else {
                Toast.makeText(SignUp.this, resultData.getString(constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
