package com.example.oopsfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class rating extends AppCompatActivity {
    RatingBar r1,r2,r3;
    Button b1,b2;
    TextView t,t1,t2,t3;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        r1 = findViewById(R.id.R1);
        r2 = findViewById(R.id.R2);
        r3 = findViewById(R.id.R3);
        b1 = findViewById(R.id.brate);
        b2 = findViewById(R.id.logout);
        t=findViewById(R.id.t4);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);


        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = fstore.collection("users").document(userID);

        Map<String,Object> data = new HashMap<>();
        data.put("Order",false);

        documentReference.update(data);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setText("Thanks for your Valuable Feedback");
                float f1= r1.getRating();
                float f2=r2.getRating();
                float f3=r3.getRating();
                float f4=(f1+f2+f3)/3;
                String s = String.valueOf(f4);
                Toast.makeText(rating.this,s+" stars",Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(rating.this,LogIn.class);
                startActivity(intent2);
            }
        });
    }
}
