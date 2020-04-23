package com.example.oopsfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import javax.annotation.Nullable;

public class order_summary extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String delID="qGiznMQC6dSwUDmSqUZFtuhIofJ2";


    TextView oid,name,number,email;
    Button rate;
    String userID;
    public static String nom,num,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        name=findViewById(R.id.d2);
        number=findViewById(R.id.d3);
        email=findViewById(R.id.d4);
        rate=findViewById(R.id.Rating);

        oid=findViewById(R.id.d1);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        oid.setText("Order Id: "+message);
        name.setText("Delivery Executive: "+Price.dom);
        number.setText("Phone Number: "+Price.dum);
        email.setText("Contact: "+Price.dail);




//        DocumentReference reff = fstore.collection("users").document(userID);
//        reff.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                String delguy = documentSnapshot.getString("DeliveryAgentID");
//
//                DocumentReference documentReference = fstore.collection("users").document(delguy);
//                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                         nom = documentSnapshot.getString("Full Name");
//                         num = documentSnapshot.getString("Mobile number");
//                         mail= documentSnapshot.getString("Email");
//
//                        name.setText("Delivery Executive: "+nom);
//                        number.setText("Phone Number: "+num);
//                        email.setText("Contact: "+mail);
//                    }
//                });
//
//            }
//        });

///////////////////////////////////////////////////////////////////////////req
//        DocumentReference documentReference = fstore.collection("users").document(delID);
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                String nom = documentSnapshot.getString("Full Name");
//                String num = documentSnapshot.getString("Mobile number");
//                String mail= documentSnapshot.getString("Email");
//
//                name.setText("Delivery Executive: "+nom);
//                number.setText("Phone Number: "+num);
//                email.setText("Contact: "+mail);
//
//
//            }
//        });


    }

    public void feedback(View v){
        //Toast.makeText(order_summary.this,"hi",Toast.LENGTH_SHORT).show();
        Intent in= new Intent(order_summary.this,rating.class);
        startActivity(in);

    }
}
