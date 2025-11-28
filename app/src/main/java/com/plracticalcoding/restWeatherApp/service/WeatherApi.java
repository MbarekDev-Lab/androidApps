package com.plracticalcoding.restWeatherApp.service;


import com.plracticalcoding.restWeatherApp.model.WeatherModel;
import com.plracticalcoding.restWeatherApp.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET(Constants.SUB_URL)
    Call<WeatherModel> getWeatherByLocation(@Query("lat") double userLatitude, @Query("lon") double userLongitude);

    @GET(Constants.SUB_URL)
    Call<WeatherModel> getWeatherByCityName(@Query("q") String cityName);

}
