package com.ganpat.smartagriculture;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ProductViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Data> dataList;

    //getting the context and product list with constructor
    public DataAdapter(Context mCtx, List<Data> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_data, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the data of the specified position
        Data data = dataList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(data.getTitle());
        holder.textViewShortDesc.setText(data.getShortdesc());
        holder.textViewPrice.setText(data.getSdata());

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(data.getImage()));


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewPrice;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = (TextView) itemView.findViewById(R.id.textViewShortDesc);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
        }
    }
}
