package com.midterm.firebasetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends FirebaseRecyclerAdapter<SearchModel, SearchAdapter.myViewHolder> {
    public SearchAdapter(@NonNull FirebaseRecyclerOptions<SearchModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull SearchModel model) {
        holder.txtUsername.setText(model.getName().toUpperCase());
        holder.txtName.setText(model.getAlternativename());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.img);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView txtUsername, txtName;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtName = itemView.findViewById(R.id.txt_name);

        }
    }

}
