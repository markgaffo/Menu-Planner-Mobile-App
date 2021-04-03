package com.example.caproject;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterFoodChina extends Filter {
    private AdapterFoodChina adapter;
    private ArrayList<ModelFoodChina> filterList;

    public FilterFoodChina(AdapterFoodChina adapter, ArrayList<ModelFoodChina> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }
    //search bar filter
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //needed to validate data in search field
        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            //taking foodlist and placing in a local array for searching
            ArrayList<ModelFoodChina> filteredModels = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getFoodTitle().toUpperCase().contains(constraint)){
                    //if the text user entered is in the title of the item on the menu it gets added to the new filtered menu list
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        //no text in search
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        //returns the filteredlist with items searched for
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        adapter.foodList = (ArrayList<ModelFoodChina>) results.values;
        adapter.notifyDataSetChanged();
    }
}
