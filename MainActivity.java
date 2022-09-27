package com.furkancavdardev.coronavirustracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerviewAdapter.ItemClickListener {

    private TextView textViewActiveByCountry;
    private TextView textViewDeathsByCountry;

    private SearchView searchView;

    private APIInterface apiInterface;

    private RecyclerviewAdapter recyclerviewAdapter;

    private ArrayList<String> arrayListCountries;

    private ArrayList<String> arrayListActiveCases;
    private ArrayList<String> arrayListTotalDeaths;
    private ArrayList<String> arrayListTotalCases;
    private ArrayList<String> arrayListTotalRecovered;

    private ArrayList<String> arrayListActiveCasesByCountry;
    private ArrayList<String> arrayListDeathsByCountry;
    private ArrayList<String> arrayListTotalCasesByCountry;
    private ArrayList<String> arrayListRecoveredByCountry;

    private HashMap<String, String> hashMapCountryActive;
    private HashMap<String, String> hashMapCountryDeaths;

    int totalCases = 0;
    int totalDeathsWorld = 0;
    int totalRecovered = 0;

    int activeCasesWorld = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewActiveByCountry = findViewById(R.id.textViewActiveByCountry);
        this.textViewDeathsByCountry = findViewById(R.id.textViewDeathsByCountry);

        this.searchView = findViewById(R.id.searchView);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try{
                    recyclerviewAdapter.getFilter().filter(s);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        arrayListCountries = new ArrayList<>();

        arrayListActiveCases = new ArrayList<>();
        arrayListTotalDeaths = new ArrayList<>();
        arrayListTotalCases = new ArrayList<>();
        arrayListTotalRecovered = new ArrayList<>();

        arrayListActiveCasesByCountry = new ArrayList<>();
        arrayListDeathsByCountry = new ArrayList<>();
        arrayListTotalCasesByCountry = new ArrayList<>();
        arrayListRecoveredByCountry = new ArrayList<>();

        hashMapCountryActive = new HashMap<>();
        hashMapCountryDeaths = new HashMap<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<Repo> call = apiInterface.getCountries();
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                Log.d("TAG",response.code()+"");
                String displayResponse = "";

                Repo resource = response.body();
                List<Repo.Country> countryList = resource.countries;
                String date = resource.date;

                displayResponse += date + " Date\n";

                for (Repo.Country country : countryList) {
//                    totalCases += country.totalConfirmed;
                    totalDeathsWorld += Integer.parseInt(country.totalDeaths);
//                    totalRecovered += country.totalRecovered;
                    activeCasesWorld += Integer.parseInt(country.totalConfirmed) - (Integer.parseInt(country.totalRecovered) + Integer.parseInt(country.totalDeaths));

                    arrayListCountries.add(country.country);

                    arrayListActiveCasesByCountry.add("Active: " + (Integer.parseInt(country.totalConfirmed) - (Integer.parseInt(country.totalRecovered) + Integer.parseInt(country.totalDeaths))));
                    arrayListDeathsByCountry.add("Deaths: " + country.totalDeaths);
                    arrayListTotalCasesByCountry.add(country.totalConfirmed);
                    arrayListRecoveredByCountry.add(country.totalRecovered);

                    hashMapCountryActive.put("world", String.valueOf(activeCasesWorld));
                    hashMapCountryDeaths.put("world", String.valueOf(totalDeathsWorld));
                    hashMapCountryActive.put(country.country.toLowerCase(), String.valueOf((Integer.parseInt(country.totalConfirmed) - (Integer.parseInt(country.totalRecovered) + Integer.parseInt(country.totalDeaths)))));
                    hashMapCountryDeaths.put(country.country.toLowerCase(), country.totalDeaths);
                }
                init();
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    void init(){
        arrayListCountries.remove(0);
        arrayListCountries.remove(0);

        arrayListActiveCasesByCountry.remove(0);
        arrayListActiveCasesByCountry.remove(0);

        arrayListDeathsByCountry.remove(0);
        arrayListDeathsByCountry.remove(0);

        arrayListTotalCasesByCountry.remove(0);
        arrayListTotalCasesByCountry.remove(0);

        arrayListRecoveredByCountry.remove(0);
        arrayListRecoveredByCountry.remove(0);

        arrayListActiveCasesByCountry.add(0, "Active: " + activeCasesWorld);
        arrayListDeathsByCountry.add(0, "Deaths: " + totalDeathsWorld);

        arrayListCountries.add(0, "World");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewAdapter = new RecyclerviewAdapter(this, arrayListCountries, arrayListActiveCasesByCountry, arrayListDeathsByCountry, this);
        recyclerviewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerviewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + recyclerviewAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    //GETTER - SETTER

    public HashMap<String, String> getHashMapCountryActive() {
        return hashMapCountryActive;
    }

    public HashMap<String, String> getHashMapCountryDeaths() {
        return hashMapCountryDeaths;
    }
}
