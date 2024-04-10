package com.example.ass_api.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ass_api.Handle.Item_Product_Handle;
import com.example.ass_api.Model.Product;
import com.example.ass_api.R;
import com.example.ass_api.Services.HttpRequest;

import java.time.Instant;
import java.util.ArrayList;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder>{

    private Context context;
    private ArrayList<Product> list;
    private Item_Product_Handle handle;
    private HttpRequest httpRequest;

    public AdapterProduct(Context context, ArrayList<Product> list, Item_Product_Handle handle) {
        this.context = context;
        this.list = list;
        this.handle = handle;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        httpRequest = new HttpRequest();
        Product product = list.get(position);
//        holder.img_pro.setImageResource(product.getImage_pro());

        Glide.with(context)
                .load(product.getImage_pro())
                .into(holder.img_pro);
        holder.txtName_pro.setText(product.getName_pro());
        holder.txtWeight_pro.setText(String.valueOf(product.getWeight_pro()));
        holder.txtPrice_pro.setText(String.valueOf(product.getPrice_pro()));
        holder.btn_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xoá không")
                        .setCancelable(false)
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handle.Delete(product.getId());
                                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
        holder.item_pro.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handle.Update(product.getId(),product);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_pro,btn_Del;
        TextView txtName_pro,txtWeight_pro,txtPrice_pro;
        Button btnAddToCart;
        CardView item_pro;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName_pro = itemView.findViewById(R.id.txtNamePro);
            txtWeight_pro = itemView.findViewById(R.id.txtWeightPro);
            txtPrice_pro = itemView.findViewById(R.id.txtPricePro);
            img_pro = itemView.findViewById(R.id.imgPro);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btn_Del = itemView.findViewById(R.id.btnDel);
            item_pro = itemView.findViewById(R.id.item_product);
        }
    }
}
