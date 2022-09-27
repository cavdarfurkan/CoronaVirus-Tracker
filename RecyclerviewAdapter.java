package com.furkancavdardev.coronavirustracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> implements Filterable {

    private List<String> dataCountry;
    private List<String> dataActiveByCountry;
    private List<String> dataDeathsByCountry;

    private List<String> dataCountryFull;

    private HashMap<String, String> hashMapCountryActive;
    private HashMap<String, String> hashMapCountryDeaths;

    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public RecyclerviewAdapter(Context context, List<String> dataCountry, List<String> dataActiveByCountry, List<String> dataDeathsByCountry, MainActivity main) {
        this.layoutInflater = LayoutInflater.from(context);

        this.dataCountry = dataCountry;
        this.dataActiveByCountry = dataActiveByCountry;
        this.dataDeathsByCountry = dataDeathsByCountry;

        this.dataCountryFull = new ArrayList<>(this.dataCountry);

        this.hashMapCountryActive = main.getHashMapCountryActive();
        this.hashMapCountryDeaths = main.getHashMapCountryDeaths();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String myDataCountry = dataCountry.get(position);
        String myDataActiveByCountry = dataActiveByCountry.get(position);
        String myDataDeathsByCountry = dataDeathsByCountry.get(position);

        holder.textViewCountry.setText(myDataCountry);
        holder.textViewActiveByCountry.setText(myDataActiveByCountry);
        holder.textViewDeathsByCountry.setText(myDataDeathsByCountry);
    }

    @Override
    public int getItemCount() {
        return dataCountry.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(dataCountryFull);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (String string : dataCountryFull){
                    if(string.toLowerCase().contains(filterPattern)){
                        filteredList.add(string);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataCountry.clear();
            dataActiveByCountry.clear();
            dataDeathsByCountry.clear();

            dataCountry.addAll((List) filterResults.values);

            for (int i = 0; i < dataCountry.size(); i++) {
                dataActiveByCountry.add("Active: " + hashMapCountryActive.get(dataCountry.get(i).toLowerCase()));
                dataDeathsByCountry.add("Deaths: " + hashMapCountryDeaths.get(dataCountry.get(i).toLowerCase()));
            }
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewCountry;
        TextView textViewActiveByCountry;
        TextView textViewDeathsByCountry;

        ViewHolder(View itemView) {
            super(itemView);
            textViewCountry = itemView.findViewById(R.id.textViewList);
            textViewActiveByCountry = itemView.findViewById(R.id.textViewActiveByCountry);
            textViewDeathsByCountry = itemView.findViewById(R.id.textViewDeathsByCountry);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

        String getItem(int id){
            return dataCountry.get(id);
        }

        void setClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }
