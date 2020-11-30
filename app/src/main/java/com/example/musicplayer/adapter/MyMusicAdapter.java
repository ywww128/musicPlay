package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.LocalSong;

import java.util.List;

/**
 * @author ywww
 * @date 2020-11-29 19:16
 */
public class MyMusicAdapter extends RecyclerView.Adapter<MyMusicAdapter.MyMusicHolder> {
    private List<LocalSong> songList;
    private OnItemClickListener onItemClickListener;

    public MyMusicAdapter(List<LocalSong> songList){
        this.songList = songList;
    }

    @NonNull
    @Override
    public MyMusicAdapter.MyMusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_items_view,parent,false);
        return new MyMusicHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMusicAdapter.MyMusicHolder holder, int position) {
        holder.s_name.setText(songList.get(position).getName());
        holder.a_name.setText(songList.get(position).getArtist_name());
    }

    @Override
    public int getItemCount() {
        if(songList!=null){
            return songList.size();
        }
        return 0;
    }

    public class MyMusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView s_name;
        private TextView a_name;
        private OnItemClickListener mListener;
        private ImageView collect;
        private ImageView moreSetting;
        private LinearLayout songLayout;
        public MyMusicHolder(@NonNull View itemView,OnItemClickListener mListener) {
            super(itemView);
            s_name = itemView.findViewById(R.id.song_name_view);
            a_name = itemView.findViewById(R.id.artist_name_view);
            this.mListener = mListener;
            songLayout = itemView.findViewById(R.id.song_message_layout);
            collect = itemView.findViewById(R.id.collect_view);
            moreSetting = itemView.findViewById(R.id.more_setting_view);
            songLayout.setOnClickListener(this);
            collect.setOnClickListener(this);
            moreSetting.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                switch (v.getId()){
                    case R.id.song_message_layout:
                        mListener.onSongClick(songLayout,getLayoutPosition());
                        break;

                    case R.id.collect_view:
                        mListener.onCollectClick(collect,getLayoutPosition());
                        break;

                    case R.id.more_setting_view:
                        mListener.onMoreClick(moreSetting,getLayoutPosition());
                        break;

                    default:
                        break;
                }



            }
        }
    }

    /**
     * 用于设置监听
     */
    public interface OnItemClickListener {
        /**
         * 用于设置歌曲点击播放监听
         * @param view
         * @param position
         */
        void onSongClick(View view,int position);

        /**
         * 用于设置收藏歌曲监听
         * @param view
         * @param position
         */
        void onCollectClick(View view,int position);

        /**
         * 用于点击歌曲更多操作监听
         * @param view
         * @param position
         */
        void onMoreClick(View view,int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     * @param onItemClickListener
     */
    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
