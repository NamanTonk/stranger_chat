package com.newEra.strangers.chat.api;

import com.google.gson.JsonObject;
import com.newEra.strangers.chat.model.profile_Model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
@GET("/api/?format=json")
 Call<profile_Model>PROFILE_MODEL_CALL();
}
