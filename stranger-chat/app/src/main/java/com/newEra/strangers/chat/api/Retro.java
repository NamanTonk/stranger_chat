package com.newEra.strangers.chat.api;

import com.newEra.strangers.chat.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
public static Api api(){
    return new Retrofit.Builder().baseUrl("https://randomuser.me").addConverterFactory(GsonConverterFactory.create()).build().create(Api.class);
}
}
