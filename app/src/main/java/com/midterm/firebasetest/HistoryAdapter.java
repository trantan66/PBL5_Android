package com.midterm.firebasetest;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends FirebaseRecyclerAdapter<HistoryModel, HistoryAdapter.myViewHolder> {
    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<HistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull HistoryModel model) {
        holder.txtUsername.setText(model.getName().toUpperCase());
        holder.txtName.setText(model.getAlternativename());
        holder.txtTime.setText(model.getTimestamp());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.img);

        holder.cardViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_detail))
                        .setExpanded(true, 1603)
                        .create();


                View view = dialogPlus.getHolderView();

                ImageView plantImage = view.findViewById(R.id.plant_image);
                TextView txtPlantName = view.findViewById(R.id.plant_name);
                TextView txtPlantAlternativename = view.findViewById(R.id.plant_alternativename);
                TextView txtPlantSciencename = view.findViewById(R.id.plant_sciencename);
                TextView txtPlant_family = view.findViewById(R.id.plant_family);
                TextView txtPlantPartused = view.findViewById(R.id.plant_partused);
                TextView txtUses = view.findViewById(R.id.uses);


//                Glide.with(holder.img.getContext())
//                        .load(model.getImage())
//                        .into(plantImage);
                Glide.with(holder.img.getContext())
                        .load(model.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .circleCrop()
                        .error(R.drawable.ic_launcher_foreground)
                        .into(plantImage);

                txtPlantName.setText(model.getName());
                txtPlantAlternativename.setText(model.getAlternativename());
                txtPlantSciencename.setText(model.getSciencename());
                txtPlant_family.setText(model.getFamily());
                txtPlantPartused.setText(model.getPartused());
                txtUses.setText(model.getUses());

                dialogPlus.show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtName.getContext());
                builder.setTitle("Are you sure");
                builder.setMessage("Delete date can't be undo!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("History")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.txtName.getContext(), "Canceled", Toast.LENGTH_SHORT);
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView txtUsername, txtName, txtTime;
        CardView cardViewItem;
        Button btnDelete;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTime = itemView.findViewById(R.id.txt_time);
            cardViewItem = itemView.findViewById(R.id.card_view_item);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

}
