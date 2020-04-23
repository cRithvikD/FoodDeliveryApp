package com.example.oopsfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Random;

import javax.annotation.Nullable;

import static com.example.oopsfinal.order_summary.nom;


public class Price extends  AppCompatActivity{
//public class Price extends Menu {
    // Button small, medium, large;
     TextView pricePackage;
    private FirebaseAuth firebaseAuth;
    public  Object lol;
    public  Object bol;
    public static  String dom,dum,dail;
    /////////////////////////////////////////////////
    // public static DocumentReference documentReference;
   FirebaseFirestore fstore;
    String userID;
  //  public static String nom,num,mail;


 public static String smsMessage= new DecimalFormat("000000").format(new Random().nextInt(999999));
    String orderid= new DecimalFormat("000000").format(new Random().nextInt(999999));
    //int cost=500;
    //String food ="One Medium Cheese Pizza";
   // String s= String.valueOf(cost);

    final int SEND_SMS_PERMISSION_REQUEST_CODE=1;
    TextView order;
    TextView price;
    TextView enterotp;
    EditText otp;
    Button send;
    Button confirm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
       // setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("PRICE");

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
       // userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        //DocumentReference documentReference = fstore.collection("users").document(userID);


//        DocumentReference documentReference = fstore.collection("users").document(userID);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                String delguy = documentSnapshot.getString("DeliveryAgentID");
//
//                DocumentReference reff = fstore.collection("users").document(delguy);
//                reff.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        nom = documentSnapshot.getString("Full Name");
//                        num = documentSnapshot.getString("Mobile number");
//                        mail= documentSnapshot.getString("Email");
//
////                        name.setText("Delivery Executive: "+nom);
////                        number.setText("Phone Number: "+num);
////                        email.setText("Contact: "+mail);
//                    }
//                });
//
//            }
//        });


        ////////////////////////////////


        //pricePackage = findViewById(R.id.textPrice);
        order=findViewById(R.id.inputNumber);
        price= findViewById(R.id.inputmessage);
        enterotp = findViewById(R.id.inputotp);
        send = findViewById(R.id.button);
        otp = findViewById(R.id.otptext);
        confirm=findViewById(R.id.button2);
        ///////////////////////////////////////////////////////////////////
        //PRICE CALCULATION
     //  final int  score = getIntent().getIntExtra("intname1", 0);
        final float[] moneyNoDis = Menu.ans;
        final float[] moneyDis = Menu.moneySend;

        /////////////////////////////////////////////////////////////////
        send.setEnabled(true);
        confirm.setEnabled(true);
        if(checkPermssion(Manifest.permission.SEND_SMS)){
            send.setEnabled(true);

        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        }



        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final DocumentReference documentReference = fstore.collection("users").document(userID);




        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Boolean b= documentSnapshot.getBoolean("Subscription");
                if(b){

                    String nom= documentSnapshot.getString("Full Name");
                    order.setText("REGULAR customer "+nom);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Amount", moneyDis[0]);
                    documentReference.update(user);
                    price.setText(String.format("Discounted Price is Rupees: %s",  moneyDis[0]));



                }
                else{
                    String nom= documentSnapshot.getString("Full Name");
                    order.setText("Customer "+nom);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Amount", moneyNoDis[0]);
                    documentReference.update(user);



                    price.setText(String.format("Price is Rupees: %s", (Object) moneyNoDis[0]));


                }
            }
        });
        Map<String, Object> user = new HashMap<>();
        user.put("Pickup Food Packet location latitude", GeocodingLocation.latlel2 );
        user.put("Pickup Food Packet location longitude", GeocodingLocation.lonlel2 );
        user.put("Pickup Address", LocationUser.address1);

       // user.put("Amount", money[0]);
        user.put("Order", true);

        ////////
        documentReference.update(user);




//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//
//            @Override
//
//            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
//
//                if (task.isSuccessful()) {
//
//                    DocumentSnapshot doc = task.getResult();
//                     lol = doc.get("Subscription");
//                     bol=doc.get("Full Name");
//
//                    System.out.println("Value is " + lol);
//                       if (lol.equals(true))
//                        {
//                            ans[0] = (float) (ans[0] *0.8);
//                           // order.setText(bol);
//                            price.setText(String.format("Regular customer! Price for your order is rupees: %s", ans[0]));
//                        }
//                      else{
//                           price.setText(String.format(" Not a regular customer. Price for your order is rupees: %s", ans[0]));
//                       }
//
//                }
//
//            }
//
//        });

//                .addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//
//                });






    }
    public void onSend(View v){

        Toast.makeText(Price.this,"Requesting....",Toast.LENGTH_SHORT).show();

//        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
//        DocumentReference documentReference = fstore.collection("users").document(userID);
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                String msg= documentSnapshot.getString("Mobile number");
//
//                String phonenumber=msg;
//                String finalsms= "Your OTP is "+smsMessage+".Please do not share this OTP with anyone. ";
//
//                if(phonenumber== null || phonenumber.length()==0 || smsMessage == null || smsMessage.length()==0){
//                    return;
//                }
//
//                if(checkPermssion(Manifest.permission.SEND_SMS)){
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("+91 "+phonenumber,null,finalsms,null,null);
//                    Toast.makeText(Price.this,"OTP Sent successfully",Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//                    Toast.makeText(Price.this,"Permission Denied",Toast.LENGTH_SHORT).show();
//                }




//
//            }
//        });

        //String phonenumber = number.getText().toString();
        //String smsMessage = message.getText().toString();
       // String phonenumber=msg;

        //System.out.println(otp);

    }

    public void onSend2(View v){

       //final String name= nom;
        //final String number= order_summary.num;
        //final String email=order_summary.mail;
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final DocumentReference documentReference = fstore.collection("users").document(userID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String msg= documentSnapshot.getString("Mobile number");
              final  String otprec = otp.getText().toString();
               final String phonenumber= msg;
                String delguy = documentSnapshot.getString("DeliveryAgentID");
               final String otpconfirm= documentSnapshot.getString("OTP");
            DocumentReference rer = fstore.collection("users").document(delguy);
                rer.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        dom = documentSnapshot.getString("Full Name");
                         dum = documentSnapshot.getString("Mobile number");
                         dail= documentSnapshot.getString("Email");
                        if(otpconfirm.contentEquals(otprec)) {
                            String smsMessage = " Your Order has been confirmed. "+orderid+" is your orderid.\n Your delivery agent will " +
                                    "be " +dom+"\n Contact:"+ dum+" \n Mail :"+ dail;

                            if (phonenumber == null || phonenumber.length() == 0 || smsMessage == null || smsMessage.length() == 0) {
                                return;
                            }

                            if (checkPermssion(Manifest.permission.SEND_SMS)) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("+91 " + phonenumber, null, smsMessage, null, null);
                                Toast.makeText(Price.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(Price.this, order_summary.class);
                                // myIntent.putExtra("cost",cost);
                                myIntent.putExtra("message", orderid);
                                startActivity(myIntent);

                            } else {
                                Toast.makeText(Price.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Price.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });

        //String phonenumber = number.getText().toString();
        //String otprec = otp.getText().toString();
        //String phonenumber= "9790960605";


    }







    public boolean checkPermssion(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check== PackageManager.PERMISSION_GRANTED);

    }

}
