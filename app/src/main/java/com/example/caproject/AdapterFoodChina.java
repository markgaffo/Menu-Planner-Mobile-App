package com.example.caproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFoodChina extends RecyclerView.Adapter<AdapterFoodChina.HolderFoodChina> implements Filterable {
    //declaration
    private Context context;
    public ArrayList<ModelFoodChina> foodList, filterList;
    private FilterFoodChina filter;
    //items on menu are held, this holds the items in a filter for searching
    public AdapterFoodChina(Context context, ArrayList<ModelFoodChina> foodList){
        this.context = context;
        this.foodList = foodList;
        this.filterList = foodList;
    }

    @NonNull
    @Override
    public HolderFoodChina onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating the individual food items onto the page's
        View view = LayoutInflater.from(context).inflate(R.layout.row_food_china, parent, false);
        return new HolderFoodChina(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFoodChina holder, int position) {
        //playing the foods info into the fields for each food item.
        ModelFoodChina modelFoodChina = foodList.get(position);
        String id = modelFoodChina.getFoodId();
        String uid = modelFoodChina.getUid();
        String descritpion = modelFoodChina.getFoodDescritpion();
        String price = modelFoodChina.getFoodPrice();
        String title = modelFoodChina.getFoodTitle();
        String icon = modelFoodChina.getFoodIcon();
        String timestamp = modelFoodChina.getTimestamp();

        holder.titleTv.setText(title);
        holder.descTv.setText(descritpion);
        holder.priceTv.setText("$"+price);
        //adding the picture of a default picture if there is no picture added
        try{
            Picasso.get().load(icon).placeholder(R.drawable.ic_cart_picture).into(holder.foodIconIv);
        }
        catch(Exception e){
            holder.foodIconIv.setImageResource(R.drawable.ic_cart_picture);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
          }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    //called when search bar has text entered in
    @Override
    public Filter getFilter() {
        //populate the filter with the items being searched for
        if(filter==null){
            filter = new FilterFoodChina(this, filterList);
        }
        //returned the filtered items
        return filter;
    }

    class HolderFoodChina extends RecyclerView.ViewHolder {
        //for holding recyclerview
        private ImageView foodIconIv;
        private TextView titleTv, descTv, priceTv;

        public HolderFoodChina(@NonNull View itemView) {
            super(itemView);

            foodIconIv = itemView.findViewById(R.id.foodIconIv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descTv = itemView.findViewById(R.id.descTv);
            priceTv = itemView.findViewById(R.id.priceTv);
        }
    }
}

