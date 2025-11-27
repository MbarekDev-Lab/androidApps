package com.plracticalcoding.restWeatherApp.util;

import android.Manifest;

public class Constants {
    public static final String FINAL_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String FINAL_LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String SharedPrefrencesname = "plracticalcoding.restWeatherApp";
    public static final String keyForAllSharedPrefrencesCount = "deniedAllPermessionsCount";
    public static final String keyForOnlySharedPrefrencesCount = "deniedOnlyFinePermessionsCount";
}
