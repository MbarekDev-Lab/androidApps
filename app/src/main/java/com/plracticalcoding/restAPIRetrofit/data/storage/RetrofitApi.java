package com.plracticalcoding.restAPIRetrofit.data.storage;

import com.plracticalcoding.restAPIRetrofit.data.model.ModelClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {
    @GET("posts")
    Call<List<ModelClass>> getModelClass();


}
