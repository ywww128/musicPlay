package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * @author ywww
 * @date 2020-12-24 22:04
 */
public class LoginListAdapter extends RecyclerView.Adapter<LoginListAdapter.MyHolder> {
    List<String> ids;
    public LoginListAdapter(List<String> ids){
        this.ids = ids;
    }
    @NonNull
    @Override
    public LoginListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_history_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginListAdapter.MyHolder holder, int position) {
        holder.idView.setText(ids.get(position));
    }

    @Override
    public int getItemCount() {
        if(ids != null){
            return ids.size();
        }
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView idView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            idView = itemView.findViewById(R.id.user_id_history);
        }
    }
}
