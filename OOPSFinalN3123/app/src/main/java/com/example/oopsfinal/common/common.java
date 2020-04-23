package com.example.oopsfinal.common;

import com.example.oopsfinalRemote.RetrofitClient;
import com.example.oopsfinalRemote.GoogleAPI;


public class common {
    public static final String  baseURL = "https://maps.googleapis.com";
    public static GoogleAPI  getGoogleAPI()
    {
        return RetrofitClient.getClient(baseURL).create(GoogleAPI.class);
    }

}
