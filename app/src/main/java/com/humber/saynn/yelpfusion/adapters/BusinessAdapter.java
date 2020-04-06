package com.humber.saynn.yelpfusion.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.humber.saynn.yelpfusion.R;
import com.humber.saynn.yelpfusion.entities.Business;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder> {

    private ArrayList<Business> businessArrayList;
    private LayoutInflater layoutInflater;
    public Context ctx;

    public BusinessAdapter(Context ctx, ArrayList<Business> businessArrayList){
        this.ctx = ctx;
        layoutInflater = LayoutInflater.from(ctx);
        this.businessArrayList = businessArrayList;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.business_item,parent,false);
        return new BusinessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        final Business business = businessArrayList.get(position);

        holder.businessName.setText(business.getName());
        holder.businessCats.setText(business.getDescription());
        holder.businessImage.setImageBitmap(business.getImage());
        holder.businessRating.setText(String.valueOf(business.getRating()));
        holder.businessUrl.setText(business.getUrl());
        holder.businessUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(business.getUrl()));
                ctx.startActivity(i);
            }
        });
        holder.businessReviews.setText(String.valueOf(business.getViews()));
        holder.businessLocation.setText(business.getLocation());

    }

    @Override
    public int getItemCount() {
        return businessArrayList.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder{

        ImageView businessImage;
        TextView businessName;
        TextView businessCats;
        TextView businessLocation;
        TextView businessRating;
        TextView businessUrl;
        TextView businessReviews;

        public BusinessViewHolder(@NonNull View itemView) {
            super(itemView);

            businessImage = itemView.findViewById(R.id.businessImage);
            businessName = itemView.findViewById(R.id.businessNameText);
            businessCats = itemView.findViewById(R.id.businessCategoriesText);
            businessLocation = itemView.findViewById(R.id.businessLocationText);
            businessRating = itemView.findViewById(R.id.businessRating);
            businessUrl = itemView.findViewById(R.id.businessURL);
            businessReviews = itemView.findViewById(R.id.businessReviewCount);
        }
    }
}
