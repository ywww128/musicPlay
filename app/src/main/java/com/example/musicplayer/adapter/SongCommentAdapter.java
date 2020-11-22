package com.example.musicplayer.adapter;

import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.CommunityItemBean;
import com.example.musicplayer.bean.SongCommentItemBean;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.imageview.RadiusImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲评论界面填充数据，点赞按钮监听器
 * @author czc
 */
public class SongCommentAdapter extends BaseAdapter implements View.OnClickListener {
    private List<SongCommentItemBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private CallBack callBack;

    public interface CallBack{
        public void click(View v);
    }
    public SongCommentAdapter(Context context, List<SongCommentItemBean> list, CallBack callBack) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.callBack = callBack;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongCommentAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_song, null);

            viewHolder.avatar = convertView.findViewById(R.id.comment_song_avatar);
            viewHolder.name = convertView.findViewById(R.id.comment_song_name);
            viewHolder.time = convertView.findViewById(R.id.comment_song_time);
            viewHolder.content = convertView.findViewById(R.id.comment_song_content);
            viewHolder.btn_like = convertView.findViewById(R.id.btn_comment_song_like);

            //将converView与viewHolder进行关联，之后可以直接取出viewHolder，取出对应组件
            convertView.setTag(viewHolder);
        }else{
            //有converView的缓存，可以直接取出其中的viewHolder
            viewHolder = (SongCommentAdapter.ViewHolder) convertView.getTag();
        }

        SongCommentItemBean item = list.get(position);

        //设置View中组件的内容
        viewHolder.avatar.setImageResource(item.getAvatar());
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(item.getTime());
        viewHolder.content.setText(item.getContent());

        //设置按钮监听器
        viewHolder.btn_like.setOnClickListener(this);
        viewHolder.btn_like.setTag(position+"");

        return convertView;
    }

    @Override
    public void onClick(View v) {
        callBack.click(v);
    }

    class ViewHolder{
        RadiusImageView avatar;
        TextView name;
        TextView time;
        TextView content;
        ShineButton btn_like;
    }
}
