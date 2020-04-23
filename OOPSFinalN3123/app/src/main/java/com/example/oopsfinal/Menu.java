package com.example.oopsfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Menu extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    Button small, medium, large;
    String userID;
    public static float[] ans;
    public static final float[] moneySend = new float[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("WELCOME!!");
        small = findViewById(R.id.CPSmall1);
        medium = findViewById(R.id.CPMedium1);
        large = findViewById(R.id.CPLarge1);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final DocumentReference documentReference = fstore.collection("users").document(userID);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  pricePackage.setText("Buy one package for 50 rupees?");
                // OpenPriceSmall(v);
                float score = 50;
                Intent i1 = new Intent(getApplicationContext(),Price.class);
             //   i1.putExtra("intname1", intval1);
                final float[] distanceFinal = GeocodingLocation.resultsF;
                System.out.println("Final distance " + distanceFinal[0]);
                // distance1.setText("Distance is " + distanceFinal);
                ans= new float[]{score + ((distanceFinal[0] / 1000) * 10)};
                moneySend[0] = (float) (ans[0] * 0.8);

                System.out.println("Amount payable  " + ans[0]);
                startActivity(i1);
                Map<String,Object> data = new HashMap<>();
                data.put("Order",true);
                data.put("Timestamp", FieldValue.serverTimestamp());
                data.put("Drop Address",LocationUser.addDBDis);
                //data.put("DMoney", moneySend[0]);
              //  data.put("NMoney",ans[0]);
                documentReference.update(data);


            }


        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  pricePackage.setText("Buy one package for 50 rupees?");
                // OpenPriceMedium(v);
                //int intval2 = 100;
                float score = 100;
                Intent i1 = new Intent(getApplicationContext(),Price.class);
                //   i1.putExtra("intname1", intval1);
                final float[] distanceFinal = GeocodingLocation.resultsF;
                System.out.println("Final distance " + distanceFinal[0]);
                // distance1.setText("Distance is " + distanceFinal);
                ans = new float[]{score + ((distanceFinal[0] / 1000) * 10)};

                moneySend[0] = (float) (ans[0] * 0.8);
                System.out.println("Amount payable  " + ans[0]);
                Intent i2 = new Intent(getApplicationContext(),Price.class);
               // i2.putExtra("intname1", intval2);
                startActivity(i2);
                Map<String,Object> data = new HashMap<>();
                data.put("Order",true);
                data.put("Timestamp", FieldValue.serverTimestamp());
                data.put("Drop Address",LocationUser.addDBDis);
              //  data.put("DMoney", moneySend[0]);
                //data.put("NMoney",ans[0]);
                documentReference.update(data);



            }


        });

        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  pricePackage.setText("Buy one package for 50 rupees?");
                // OpenPriceLarge(v);
               // int intval3 = 150;
                float score = 150;
                Intent i1 = new Intent(getApplicationContext(),Price.class);
                //   i1.putExtra("intname1", intval1);
                final float[] distanceFinal = GeocodingLocation.resultsF;
                System.out.println("Final distance " + distanceFinal[0]);
                // distance1.setText("Distance is " + distanceFinal);
                ans = new float[]{score + ((distanceFinal[0] / 1000) * 10)};
                moneySend[0] = (float) (ans[0] * 0.8);

                System.out.println("Amount payable  " + ans[0]);
                Intent i3 = new Intent(getApplicationContext(),Price.class);
               // i3.putExtra("intname1", intval3);
                startActivity(i3);
                Map<String,Object> data = new HashMap<>();
                data.put("Order",true);
                data.put("Timestamp", FieldValue.serverTimestamp());
               data.put("Drop Address",LocationUser.addDBDis);
              //  data.put("DMoney", moneySend[0]);
              //  data.put("NMoney",ans[0]);
                documentReference.update(data);



            }


        });

    }



   /* public void OpenPriceSmall(View view) {
        startActivity(new Intent(getApplicationContext(),Price.class));
    }

    public void OpenPriceMedium(View view) {
        startActivity(new Intent(getApplicationContext(),Price.class));
    }

    public void OpenPriceLarge(View view) {
        startActivity(new Intent(getApplicationContext(),Price.class));
    }*/


}
