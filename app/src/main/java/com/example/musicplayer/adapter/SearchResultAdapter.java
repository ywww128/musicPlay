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
import com.example.musicplayer.bean.Song;

import java.util.ArrayList;


/**
 * @author ywww
 * @date 2020-11-20 21:54
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyHolder>{
    private ArrayList<Song> songs;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(ArrayList<Song> songs){
        this.songs = songs;
    }

    /**
     * 创建条目界面
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SearchResultAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 下个代码宽高会失效
        // LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_test_item,null)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_items_view,parent,false);
        return new MyHolder(view,onItemClickListener);
    }

    /**
     * 设置（绑定）数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.MyHolder holder, int position) {
        StringBuilder names = new StringBuilder();
        holder.s_name.setText(songs.get(position).name);
        for(int i=0;i<songs.get(position).artists.size();i++){
            names.append(songs.get(position).artists.get(i).name);
            names.append(",");
        }
        names.deleteCharAt(names.length() - 1);
        holder.a_name.setText(names);
        holder.itemView.setTag(songs.get(position));
    }

    /**
     * 获取条目总数
     * @return 条目总数
     */
    @Override
    public int getItemCount() {
        if(songs != null){
            return songs.size();
        }
        return 0;
    }


    /**
     * 条目类
     */
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView s_name;
        private TextView a_name;
        private ImageView collect;
        private ImageView moreSetting;
        private LinearLayout songLayout;
        private OnItemClickListener mListener;
        public MyHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            s_name = itemView.findViewById(R.id.song_name_view);
            a_name = itemView.findViewById(R.id.artist_name_view);
            songLayout = itemView.findViewById(R.id.song_message_layout);
            collect = itemView.findViewById(R.id.collect_view);
            moreSetting = itemView.findViewById(R.id.more_setting_view);
            this.mListener = onItemClickListener;
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
