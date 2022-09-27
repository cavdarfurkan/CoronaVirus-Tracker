package com.furkancavdardev.coronavirustracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Repo {
    @SerializedName("Countries")
    @Expose
    public List<Country> countries = null;
    @SerializedName("Date")
    @Expose
    public String date;

    public class Country {
        @SerializedName("Country")
        @Expose
        public String country;
        @SerializedName("Slug")
        @Expose
        public String slug;
        @SerializedName("NewConfirmed")
        @Expose
        public String newConfirmed;
        @SerializedName("TotalConfirmed")
        @Expose
        public String totalConfirmed;
        @SerializedName("NewDeaths")
        @Expose
        public String newDeaths;
        @SerializedName("TotalDeaths")
        @Expose
        public String totalDeaths;
        @SerializedName("NewRecovered")
        @Expose
        public String newRecovered;
        @SerializedName("TotalRecovered")
        @Expose
        public String totalRecovered;
    }
}