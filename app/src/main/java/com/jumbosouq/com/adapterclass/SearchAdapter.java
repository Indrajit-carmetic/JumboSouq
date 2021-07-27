package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jumbosouq.com.Modelclass.MobileModel;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Produtsdetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>  {

    private LayoutInflater inflater;
    private ArrayList<SearchModel> searchModelArrayList;
    Context ctx;

    public SearchAdapter(Context ctx, ArrayList<SearchModel> searchModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.searchModelArrayList = searchModelArrayList;
        this.ctx = ctx;

    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_search, parent, false);
        SearchAdapter.MyViewHolder holder = new SearchAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter.MyViewHolder holder, int position) {


        String productname = "";
        if (searchModelArrayList.get(position).getProductname().length() >= 33) {
            productname = searchModelArrayList.get(position).getProductname().substring(0, 30) + "...";
        } else {
            productname = searchModelArrayList.get(position).getProductname();
        }
        holder.tvProductName.setText(productname);
        holder.tvPrice.setText("QAR "+searchModelArrayList.get(position).getPrice());
        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" +
                        searchModelArrayList.get(position).getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.ImgProduct);

        holder.layout_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", searchModelArrayList.get(position).getSkuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImgProduct;
        TextView tvProductName, tvPrice, tvQuantity;
        LinearLayout btnAddtocart;
        LinearLayout layout_rv, ll_counter;
        ImageView ivDecrese;
        ImageView ivIncrease;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ImgProduct = itemView.findViewById(R.id.ImgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddtocart = itemView.findViewById(R.id.btnAddtocart);
            layout_rv = itemView.findViewById(R.id.layout_rv);
            ll_counter = itemView.findViewById(R.id.ll_counter);
            ivDecrese = itemView.findViewById(R.id.ivDecrese);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);

        }
    }
}
