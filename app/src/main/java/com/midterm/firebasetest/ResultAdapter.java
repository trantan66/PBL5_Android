package com.midterm.firebasetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private ArrayList<ResultModel> list;

    public ResultAdapter(ArrayList<ResultModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResultModel model = list.get(position);

        holder.txtUsername.setText(model.getName() != null ? model.getName().toUpperCase() : "N/A");
        holder.txtName.setText(model.getAlternativename() != null ? model.getAlternativename() : "N/A");

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.img);

        holder.cardViewItem.setOnClickListener(v -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                    .setContentHolder(new ViewHolder(R.layout.activity_detail))
                    .setExpanded(true, 1603)
                    .create();

            View dialogView = dialogPlus.getHolderView();

            ImageView plantImage = dialogView.findViewById(R.id.plant_image);
            TextView txtPlantName = dialogView.findViewById(R.id.plant_name);
            TextView txtPlantAlternativename = dialogView.findViewById(R.id.plant_alternativename);
            TextView txtPlantSciencename = dialogView.findViewById(R.id.plant_sciencename);
            TextView txtPlantFamily = dialogView.findViewById(R.id.plant_family);
            TextView txtPlantPartused = dialogView.findViewById(R.id.plant_partused);
            TextView txtUses = dialogView.findViewById(R.id.uses);

            Glide.with(holder.img.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .error(R.drawable.ic_launcher_foreground)
                    .into(plantImage);

            txtPlantName.setText(model.getName() != null ? model.getName() : "N/A");
            txtPlantAlternativename.setText(model.getAlternativename() != null ? model.getAlternativename() : "N/A");
            txtPlantSciencename.setText(model.getSciencename() != null ? model.getSciencename() : "N/A");
            txtPlantFamily.setText(model.getFamily() != null ? model.getFamily() : "N/A");
            txtPlantPartused.setText(model.getPartused() != null ? model.getPartused() : "N/A");
            txtUses.setText(model.getUses() != null ? model.getUses() : "N/A");

            dialogPlus.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView txtUsername, txtName;
        CardView cardViewItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtName = itemView.findViewById(R.id.txt_name);
            cardViewItem = itemView.findViewById(R.id.card_view_item);
        }
    }
}
