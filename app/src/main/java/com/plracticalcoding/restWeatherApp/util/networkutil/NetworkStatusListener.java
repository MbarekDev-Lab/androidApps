package com.plracticalcoding.restWeatherApp.util.networkutil;

public interface NetworkStatusListener {

    void onNetworkAvailable();
    void onNetworkLost();

}
