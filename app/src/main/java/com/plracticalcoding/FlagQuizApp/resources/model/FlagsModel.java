package com.plracticalcoding.FlagQuizApp.resources.model;

public class FlagsModel {

    public int flagId;
    public String countryName;
    public String flagName;

    public FlagsModel(int flagId, String countryName, String flagName) {
        this.flagId = flagId;
        this.countryName = countryName;
        this.flagName = flagName;
    }

    public int getFlagId() {
        return flagId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getFlagName() {
        return flagName;
    }
}