package com.example.oopsfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.oopsfinal.Price.smsMessage;

public class DeliveryConf extends AppCompatActivity {

    TextView textOne,textTwo,textThree,textFour,textFive;
    Button btn_DL,accept;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_conf);


        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final DocumentReference documentReference = fstore.collection("users").document(DeliveryMainPage.CustomerID);

        textOne = findViewById(R.id.Pname);
        textTwo = findViewById(R.id.Pnumber);
        textThree = findViewById(R.id.PA);
        textFour = findViewById(R.id.DA);
        textFive = findViewById(R.id.monie);
        btn_DL = findViewById(R.id.logoutD);
        accept = findViewById(R.id.Confirm);

        textOne.setText("Customer name " + DeliveryMainPage.PDname);
        textTwo.setText("Customer Contact Number " + DeliveryMainPage.PDnumber);
        textThree.setText("Pick up address " + DeliveryMainPage.PDPA);
        textFour.setText("Drop address " + DeliveryMainPage.PDDA);
        textFive.setText("Please collect " + DeliveryMainPage.dabbu + " rupees from the customer");



        final String finalsms= "Your OTP is "+ smsMessage+".Please do not share this OTP with anyone. ";
        final String phonenumber =  DeliveryMainPage.PDnumber;
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> data = new HashMap<>();
                data.put("DeliveryAgentID",userID);
                data.put("OTP",smsMessage);
                documentReference.update(data);


                if(phonenumber== null || phonenumber.length()==0 || smsMessage == null || smsMessage.length()==0){
                    return;
                }

                if(checkPermssion(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+91 "+phonenumber,null,finalsms,null,null);
                    Toast.makeText(DeliveryConf.this,"OTP Sent successfully",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(DeliveryConf.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_DL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LogIn.class));

            }
        });




    }


    public boolean checkPermssion(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);

    }


}
