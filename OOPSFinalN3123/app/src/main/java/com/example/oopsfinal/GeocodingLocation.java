package com.example.oopsfinal;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";
    public  Address address1;
    public   static double latlel2,lonlel2,latitude1,longitude1;
    public TextView distance1;
    public  static float[] resultsF;
    //public Address address;


    public  void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        final Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Thread thread = new Thread() {
            @Override
            public void run() {

               String result = null;
                //String result;
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address1 = (Address) addressList.get(0);
                          latlel2 = address1.getLatitude();
                         lonlel2 = address1.getLongitude();
                        System.out.println("Device's lat and long are " + latlel2 + ' ' + lonlel2);
                         resultsF = new float[5];
                        //results = 0;
                        //float results = 0;
                        latitude1 = LocationUser.latitude;
                        longitude1 = LocationUser.longitude;
                        /*Map<String, Object> user = new HashMap<>();

                        SignUp.documentReference.set(user.put("Pickup locaton latitude", latlel2 ));
                        SignUp.documentReference.set(user.put("Pickup locaton longitude", lonlel2 ));*/

                        Location.distanceBetween(latitude1, longitude1, latlel2, lonlel2, resultsF);
                      System.out.println("The lat and long are " + latitude1+ ' ' + longitude1 + ' ' +  latlel2 + ' '  + lonlel2);
                      System.out.println("Distance " + resultsF[0]+'\n' + resultsF[1] );

                        StringBuilder sb = new StringBuilder();
                        sb.append(address1.getLatitude()).append("\n");
                        sb.append(address1.getLongitude()).append("\n");
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress ;
                            //    "\n\nLatitude and Longitude :\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}