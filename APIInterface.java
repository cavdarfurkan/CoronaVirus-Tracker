package com.furkancavdardev.coronavirustracker;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("/summary")
    Call<Repo> getCountries();
}
