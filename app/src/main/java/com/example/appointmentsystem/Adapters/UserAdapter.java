package com.example.appointmentsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointmentsystem.InteractionFragmentContainer;
import com.example.appointmentsystem.R;
import com.example.appointmentsystem.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<Users> mUsers;
    private boolean isChat;

    public UserAdapter(Context context, List<Users> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_items,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());
        if(users.getImagelink().equals("default")){
            holder.imageView.setImageResource(R.mipmap.ic_person);
        }else{

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InteractionFragmentContainer.class);
                intent.putExtra("userId",users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView username;
    public ImageView imageView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.user_itemsTextView);
        imageView = itemView.findViewById(R.id.user_itemsImageView);
    }
}
}
